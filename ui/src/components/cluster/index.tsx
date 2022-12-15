import { useOidcAccessToken } from '@axa-fr/react-oidc';
import { Tab, Tabs } from '@mui/material';
import Box from '@mui/material/Box';
import Card from '@mui/material/Card';
import CardContent from '@mui/material/CardContent';
import CardHeader from '@mui/material/CardHeader';
import Divider from '@mui/material/Divider';
import Grid from '@mui/material/Grid';
import API from 'api';
import { CopyableField, ExportCredentials, Loader } from 'components/commons';
import D from 'i18n';
import Credentials from 'model/Credentials';
import React, { useEffect, useState } from 'react';
import { exportTypes } from 'utils';
import Welcome from './welcome';
import { UIProperties } from 'model/Oidc';
import './cluster.scss';

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

const DocCard = () => {
	return (
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
	);
};

const Content = ({
	token,
	group,
	namespaceCreationAllowed,
}: {
	token?: string;
	group?: string;
	namespaceCreationAllowed: boolean;
}) => {
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
		return namespaceCreationAllowed ? (
			<Welcome
				group={group}
				credentials={cluster}
				onFinish={() => getCredentials(token, group)}
			/>
		) : (
			<NoopContent message="Pour ce cluster, pas de création de nouveaux namespace" />
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
				<DocCard />
			</Grid>
			<Grid item lg={1} />
		</Grid>
	);
};

const NoopContent = ({ message }: { message: string }) => {
	return (
		<Grid container className="cards" spacing={2}>
			<Grid item lg={1} />
			<Grid item lg={6} md={8} xs={12}>
				<Card className="card" elevation={16}>
					<CardHeader title="Information" className="card-title" />
					<Divider />
					<CardContent>{message}</CardContent>
				</Card>
			</Grid>
			<Grid item lg={4} md={4} xs={12}>
				<DocCard />
			</Grid>
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
	const [config, setConfig] = useState({
		groupFilter: '',
		userEnabled: false,
		userCanCreateNs: false,
	});
	const { groupFilter, userEnabled, userCanCreateNs } = config;
	const { accessToken, accessTokenPayload } = useOidcAccessToken();
	const [groups, setGroups] = useState<string[]>([]);
	useEffect(() => {
		API.conf()
			.then((r: UIProperties) => {
				setConfig({
					groupFilter: r.groupFilter || groupFilter,
					userEnabled: r.userNamespaceEnabled || userEnabled,
					userCanCreateNs: r.userCanCreateNs || userCanCreateNs,
				});
			})
			.catch(() => {
				console.error('error while fetch configuration');
			});
		API.groups(accessToken)
			.then((groups: string[]) => {
				setGroups(groups);
			})
			.catch(() => {
				console.error('error while fetching available groups');
			});
	}, [
		setConfig,
		groupFilter,
		userEnabled,
		accessToken,
		setGroups,
		userCanCreateNs,
	]);

	const { name, preferred_username, email } = accessTokenPayload as any;

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

				{groups.map((group: string, index: number) => (
					<Tab key={`tab-group-${index}`} label={group} value={index + 1} />
				))}
			</Tabs>
			<Box m={4} />
			<TabPanel value={activePanel} index={0}>
				{userEnabled ? (
					<Content
						token={accessToken}
						namespaceCreationAllowed={userCanCreateNs}
					/>
				) : (
					<NoopContent message="Pour ce cluster, aucune opération n'est possible côté namespace utilisateur" />
				)}
			</TabPanel>
			{groups.map((group: string, index: number) => (
				<TabPanel
					key={`panel-group-${index}`}
					value={activePanel}
					index={index + 1}
				>
					<Content
						token={accessToken}
						group={group}
						namespaceCreationAllowed={userCanCreateNs}
					/>
				</TabPanel>
			))}
		</>
	);
};

export default Cluster;
