import { useKeycloak } from '@react-keycloak/web';
import { MuiThemeProvider } from '@material-ui/core';
import React from 'react';
import {
	BrowserRouter as Router,
	Switch,
	Route,
	Redirect,
} from 'react-router-dom';
import PrivateRoute from './private-route';
import Home from 'components/home';
import Cluster from 'components/cluster';
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
					<Route exact path="/" component={Home} />
					<PrivateRoute exact path="/cluster" component={Cluster} />
					<Redirect to="/" />
				</Switch>
			</Router>
		</MuiThemeProvider>
	);
};

export default Root;
