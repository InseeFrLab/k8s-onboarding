import React from 'react';
import { Route, Switch, Redirect, useLocation } from 'react-router-dom';
import { withOidcSecure } from '@axa-fr/react-oidc-context';
import Cluster from 'components/cluster';

const Routes = () => {
	const { pathname } = useLocation();
	return (
		<Switch>
			<Route exact path="/" component={() => <div />} />
			<Route exact path="/cluster" component={withOidcSecure(Cluster)} />
			{!pathname.startsWith('/authentication') && <Redirect to="/" />}
		</Switch>
	);
};

export default Routes;
