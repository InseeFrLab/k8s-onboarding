import React, { useState, useEffect } from 'react';
import { useKeycloak } from '@react-keycloak/web';
import Divider from '@material-ui/core/Divider';
import Grid from '@material-ui/core/Grid';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardHeader from '@material-ui/core/CardHeader';
import Box from '@material-ui/core/Box';
import { Loader } from 'components/commons';
import API from 'api';
import D from 'i18n';
import './cluster.scss';

const Cluster = () => {
	const [cluster, setCluster] = useState({});
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
			<h2 className="name">{name}</h2>
			<p className="email">{email}</p>
			<Divider />
			<Box m={4} />
			<Grid container className="cards" spacing={2}>
				<Grid item lg={1} />
				<Grid item lg={6} md={8} xs={12}>
					<Card className="card">
						<CardHeader title={D.cardIdTitle} className="card-title" />
						<Divider />
						<CardContent>
							{Object.entries(cluster).map((c: any) => (
								<p key={c[0]}>{`${c[0]} : ${c[1]}`}</p>
							))}
						</CardContent>
					</Card>
				</Grid>
				<Grid item lg={4} md={4} xs={12}>
					<Card className="card">
						<CardHeader title={D.cardDocTitle} className="card-title" />
						<Divider />
						<CardContent>...</CardContent>
					</Card>
				</Grid>
				<Grid item lg={1} />
			</Grid>
		</>
	);
};

export default Cluster;
