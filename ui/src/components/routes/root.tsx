import { useKeycloak } from '@react-keycloak/web';
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

const Root = () => {
	const { initialized } = useKeycloak();

	if (!initialized) {
		return <Loader />;
	}

	return (
		<Router>
			<Header />
			<Switch>
				<Route exact path="/" component={Home} />
				<PrivateRoute exact path="/cluster" component={Cluster} />
				<Redirect to="/" />
			</Switch>
		</Router>
	);
};

export default Root;
