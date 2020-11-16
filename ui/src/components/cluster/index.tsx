import React, { useState, useEffect } from 'react';
import { useKeycloak } from '@react-keycloak/web';
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
import Box from '@material-ui/core/Box';
import { Loader, CopyableField, ExportCredentials } from 'components/commons';
import API from 'api';
import { exportTypes } from 'utils';
import D from 'i18n';
import './cluster.scss';
import { Tabs, Tab, Typography } from '@material-ui/core';
import Welcome from './welcome';
import Credentials from 'model/Credentials';

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
				<Box p={3}>
					<Typography>{children}</Typography>
				</Box>
			)}
		</div>
	);
}

const Content = ({
	token,
	namespace,
}: {
	token: string | undefined;
	namespace: string;
}) => {
	const [cluster, setCluster] = useState<Credentials>();
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		API.cluster(token).then((c) => {
			setCluster(c);
			setLoading(false);
		});
	}, [token]);

	if (loading) return <Loader />;

	if (!cluster?.onboarded) return <Welcome />;

	return (
		<Grid container className="cards" spacing={2}>
			<Grid item lg={1} />
			<Grid item lg={6} md={8} xs={12}>
				<Card className="card" elevation={16}>
					<CardHeader
						title={D.cardIdTitle + namespace}
						className="card-title"
					/>
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

	const {
		keycloak: { token, tokenParsed },
	} = useKeycloak();

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
				<Tab label="User" value={0} />
				{groups &&
					groups.map((group: string, index: number) => (
						<Tab label={group} value={index + 1} />
					))}
			</Tabs>
			<Box m={4} />
			<TabPanel value={activePanel} index={0}>
				<Content token={token} namespace={'User'} />
			</TabPanel>
			{groups &&
				groups.map((group: string, index: number) => (
					<TabPanel value={activePanel} index={index + 1}>
						<Content token={token} namespace={group} />
					</TabPanel>
				))}
		</>
	);
};

export default Cluster;
