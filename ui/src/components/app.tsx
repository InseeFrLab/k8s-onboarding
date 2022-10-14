import React, { useState, useEffect } from 'react';
import useSWR from 'swr';
import { Navigate } from 'react-router-dom';
import { OidcProvider } from '@axa-fr/react-oidc';
import { Loader } from 'components/commons';
import API from 'api';
import AppBar from 'components/commons/appbar';
import Root from './routes/root';
import { buildOidcConfiguration } from 'utils/oidc/build-configuration';
import { OIDCBaseConfig, OIDCCustomConfig } from 'model/Oidc';
import fetcher from 'utils/fetcher';

const App = () => {
	const [conf, setConf] = useState<OIDCCustomConfig>({});
	const [loading, setLoading] = useState(true);

	const { data: oidcBaseConfig } = useSWR(
		`${window.location.origin}/oidc.json`,
		fetcher
	);

	useEffect(() => {
		API.conf()
			.then((r: any) => {
				setConf(r);
				setLoading(false);
			})
			.catch(() => {
				console.error('error while fetch configuration');
			});
	}, []);

	if (loading || !oidcBaseConfig)
		return (
			<>
				<AppBar />
				<Loader />
			</>
		);
	const oidcConfiguration = buildOidcConfiguration(
		oidcBaseConfig as OIDCBaseConfig
	)(conf);

	return (
		<div className="app" data-testid="app">
			<OidcProvider
				configuration={oidcConfiguration.config as any}
				loadingComponent={Loader}
				sessionLostComponent={() => <Navigate to="/" />}
			>
				<Root />
			</OidcProvider>
		</div>
	);
};

export default App;
