import React, { useEffect } from 'react';
import {
	Route,
	Redirect,
	RouteComponentProps,
	useLocation,
} from 'react-router-dom';
import type { RouteProps } from 'react-router-dom';

import { useKeycloak } from '@react-keycloak/web';

interface PrivateRouteParams extends RouteProps {
	component:
		| React.ComponentType<RouteComponentProps<any>>
		| React.ComponentType<any>;
}

const PrivateRoute = ({
	component: Component,
	...rest
}: PrivateRouteParams) => {
	const { keycloak } = useKeycloak();

	useEffect(() => {
		keycloak?.login();
	}, [keycloak]);

	if (keycloak?.authenticated)
		return (
			<Route
				{...rest}
				render={(props: any) =>
					keycloak?.authenticated ? (
						<Component {...props} />
					) : (
						<Redirect
							to={{
								pathname: '/login',
								state: { from: props.location },
							}}
						/>
					)
				}
			/>
		);
	return <div>loading...</div>;
};

export default PrivateRoute;
