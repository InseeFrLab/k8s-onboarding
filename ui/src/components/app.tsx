import React, { useState, useEffect } from 'react';
import useSWR from 'swr';
import { Redirect } from 'react-router-dom';
import {
	AuthenticationProvider,
	InMemoryWebStorage,
} from '@axa-fr/react-oidc-context';
import { Loader } from 'components/commons';
import API from 'api';
import AppBar from 'components/commons/appbar';
import Root from './routes/root';
import { buildOidcConfiguration } from 'utils/oidc/build-configuration';
import { OIDCCustomConfig } from 'model/Oidc';

const App = () => {
	const [conf, setConf] = useState<OIDCCustomConfig>({});
	const [loading, setLoading] = useState(true);

	const { data: oidcBaseConfig } = useSWR(
		`${window.location.origin}/oidc.json`
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
	const oidcConfiguration = buildOidcConfiguration(oidcBaseConfig)(conf);

	return (
		<div className="app" data-testid="app">
			<AuthenticationProvider
				configuration={oidcConfiguration.config}
				isEnabled={oidcConfiguration.isEnabled}
				UserStore={InMemoryWebStorage}
				callbackComponentOverride={Loader}
				authenticating={Loader}
				sessionLostComponent={() => <Redirect to="/" />}
			>
				<Root />
			</AuthenticationProvider>
		</div>
	);
};

export default App;
