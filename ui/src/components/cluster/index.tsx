import { useReactOidc } from '@axa-fr/react-oidc-context';
import { Tab, Tabs } from '@material-ui/core';
import Box from '@material-ui/core/Box';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import API from 'api';
import { CopyableField, ExportCredentials, Loader } from 'components/commons';
import D from 'i18n';
import Credentials from 'model/Credentials';
import React, { useEffect, useState } from 'react';
import { exportTypes } from 'utils';
import './cluster.scss';
import Welcome from './welcome';
import { filterGroups } from 'utils/filter-groups';
import { OIDCCustomConfig } from 'model/Oidc';

function TabPanel(props: any) {
	const { children, value, index, ...other } = props;

	return (
		<div
			role="tabpanel"
			hidden={value !== index}
			id={`scrollable-auto-tabpanel-${index}`}
			aria-labelledby={`scrollable-auto-tab-${index}`}
			{...other}
		>
			{value === index && (
				<>
					<Box p={3} />
					{children}
				</>
			)}
		</div>
	);
}

const Content = ({ token, group }: { token?: string; group?: string }) => {
	const [cluster, setCluster] = useState<Credentials>();
	const [loading, setLoading] = useState(true);

	const getCredentials = (token?: string, group?: string) => {
		API.cluster(token, group).then((c) => {
			setCluster(c);
			setLoading(false);
		});
	};

	useEffect(() => {
		getCredentials(token, group);
	}, [token, group]);

	if (loading) return <Loader />;

	if (!cluster?.onboarded)
		return (
			<Welcome
				group={group}
				credentials={cluster}
				onFinish={() => getCredentials(token, group)}
			/>
		);

	return (
		<Grid container className="cards" spacing={2}>
			<Grid item lg={1} />
			<Grid item lg={6} md={8} xs={12}>
				<Card className="card" elevation={16}>
					<CardHeader title={D.cardIdTitle} className="card-title" />
					<Divider />
					<CardContent>
						{fields.map(({ accessor, label }: any, i) => (
							<CopyableField
								key={accessor}
								row={i}
								label={label}
								value={(cluster as any)[accessor]}
								copy
							/>
						))}

						<Divider />
						<div className="row row-major">
							<ExportCredentials
								text={D.exportKubLabel}
								exportTypes={exportTypes}
								credentials={cluster}
							/>
						</div>
					</CardContent>
				</Card>
			</Grid>
			<Grid item lg={4} md={4} xs={12}>
				<Card className="card" elevation={16}>
					<CardHeader title={D.cardDocTitle} className="card-title" />
					<Divider />
					<CardContent>
						<p>{D.docDescription}</p>
						<a href="https://kubernetes.io/fr/docs/home/" className="git-link">
							https://kubernetes.io/fr/docs/home/
						</a>
					</CardContent>
				</Card>
			</Grid>
			<Grid item lg={1} />
		</Grid>
	);
};

const fields = [
	{
		accessor: 'apiserverUrl',
		label: 'API server URL (--server x)',
	},
	{ accessor: 'namespace', label: 'Namespace (--namespace x)' },
	{ accessor: 'token', label: 'Token (--token x)' },
];

const Cluster = () => {
	const [activePanel, setActivePanel] = useState(0);
	const [groupFilter, setGroupFilter] = useState('');

	const {
		oidcUser: { access_token: token, profile: tokenParsed },
	} = useReactOidc();

	useEffect(() => {
		API.conf()
			.then((r: any) => {
				setGroupFilter((r as OIDCCustomConfig).groupFilter || groupFilter);
			})
			.catch(() => {
				console.error('error while fetch configuration');
			});
	}, [setGroupFilter, groupFilter]);

	const { name, preferred_username, email, groups } = tokenParsed as any;

	return (
		<>
			<h2 className="name">{`${name} (${preferred_username})`}</h2>
			<p className="email">{email}</p>
			<Divider />
			<Tabs
				onChange={(e, newValue) => setActivePanel(newValue)}
				value={activePanel}
				indicatorColor="primary"
				textColor="primary"
				variant="scrollable"
				scrollButtons="auto"
				aria-label="scrollable auto tabs example"
			>
				<Tab label={preferred_username} value={0} />
				{groups &&
					filterGroups(
						groups,
						groupFilter
					).map((group: string, index: number) => (
						<Tab key={`tab-group-${index}`} label={group} value={index + 1} />
					))}
			</Tabs>
			<Box m={4} />
			<TabPanel value={activePanel} index={0}>
				<Content token={token} />
			</TabPanel>
			{groups &&
				filterGroups(groups, groupFilter).map(
					(group: string, index: number) => (
						<TabPanel
							key={`panel-group-${index}`}
							value={activePanel}
							index={index + 1}
						>
							<Content token={token} group={group} />
						</TabPanel>
					)
				)}
		</>
	);
};

export default Cluster;
