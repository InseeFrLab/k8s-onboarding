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

const Root = () => {
	const { initialized } = useKeycloak();

	if (!initialized) {
		return <div>Loading...</div>;
	}

	return (
		<Router>
			<Switch>
				<Route exact path="/" component={Home} />
				<PrivateRoute exact path="/cluster" component={Cluster} />
				<Redirect to="/" />
			</Switch>
		</Router>
	);
};

export default Root;
