import React, { useState, useEffect } from 'react';
import { ReactKeycloakProvider } from '@react-keycloak/web';
import { Loader } from 'components/commons';
import API from 'api';
import builDkeycloak from 'keycloak';
import Root from './routes/root';

const App = () => {
	const [conf, setConf] = useState({});
	const [loading, setLoading] = useState(true);

	useEffect(() => {
		API.conf()
			.then((r: any) => {
				setConf(r);
				setLoading(false);
			})
			.catch(() => {
				console.error('error while fetch configuration');
			});
	}, [setConf]);

	if (loading) return <Loader />;

	return (
		<div className="app" data-testid="app">
			<ReactKeycloakProvider authClient={builDkeycloak(conf)}>
				<Root />
			</ReactKeycloakProvider>
		</div>
	);
};

export default App;
