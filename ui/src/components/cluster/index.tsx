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

const fields = [
	{
		accessor: 'apiserverUrl',
		label: 'API server URL (--server x)',
	},
	{ accessor: 'namespace', label: 'Namespace (--namespace x)' },
	{ accessor: 'token', label: 'Token (--token x)' },
];

const Cluster = () => {
	const [cluster, setCluster] = useState<any>({});
	const [loading, setLoading] = useState(true);

	const {
		keycloak: { token, tokenParsed },
	} = useKeycloak();

	const { name, email } = tokenParsed as any;

	useEffect(() => {
		API.cluster(token).then((c) => {
			setCluster(c);
			setLoading(false);
		});
	}, [token]);

	if (loading) return <Loader />;

	return (
		<>
			<h2 className="name">{`${name} (${cluster['user']})`}</h2>
			<p className="email">{email}</p>
			<Divider />
			<Box m={4} />
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
									value={cluster[accessor]}
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
							<a
								href="https://kubernetes.io/fr/docs/home/"
								className="git-link"
							>
								https://kubernetes.io/fr/docs/home/
							</a>
						</CardContent>
					</Card>
				</Grid>
				<Grid item lg={1} />
			</Grid>
		</>
	);
};

export default Cluster;
