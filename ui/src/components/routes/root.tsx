import { useKeycloak } from '@react-keycloak/web';
import { MuiThemeProvider } from '@material-ui/core';
import React from 'react';
import {
	BrowserRouter as Router,
	Route,
	Switch,
	Redirect,
} from 'react-router-dom';
import PrivateRoute from './private-route';
import Cluster from 'components/cluster';
import Welcome from 'components/cluster/welcome';
import { Header, Loader } from 'components/commons';
import Theme from './../material-ui-theme';

const Root = () => {
	const { initialized } = useKeycloak();

	if (!initialized) {
		return <Loader />;
	}

	return (
		<MuiThemeProvider theme={Theme}>
			<Router>
				<Header />
				<Switch>
					<PrivateRoute exact path="/welcome" component={Welcome} />
					<PrivateRoute exact path="/cluster" component={Cluster} />
					<Route exact path="/" component={() => <div />} />
					<Redirect to="/" />
				</Switch>
			</Router>
		</MuiThemeProvider>
	);
};

export default Root;
