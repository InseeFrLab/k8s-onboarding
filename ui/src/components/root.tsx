import { useKeycloak } from '@react-keycloak/web';
import React from 'react';
import {
	BrowserRouter as Router,
	Switch,
	Route,
	Redirect,
} from 'react-router-dom';
import Cluster from './cluster/cluster';
import PrivateRoute from './commons/private-route';
import Home from './home/home';
import LoginPage from './Login/login';

const Root = () => {
	const { initialized } = useKeycloak();

	if (!initialized) {
		return <div>Loading...</div>;
	}

	return (
		<Router>
			<Switch>
				<PrivateRoute exact path="/cluster" component={Cluster} />
				<Route exact path="/" component={Home} />
				<Route exact path="/login" component={LoginPage} />
				<Redirect to="/" />
			</Switch>
		</Router>
	);
};

export default Root;
