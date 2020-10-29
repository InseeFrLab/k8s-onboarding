import React, { useEffect } from 'react';
import { Redirect, useLocation } from 'react-router-dom';

import { useKeycloak } from '@react-keycloak/web';

const LoginPage = () => {
	const location = useLocation();
	const currentLocationState: any = location.state;

	const { keycloak } = useKeycloak();

	useEffect(() => {
		keycloak?.login();
	}, [keycloak]);

	if (keycloak?.authenticated)
		return <Redirect to={currentLocationState?.from as string} />;

	return null;
};

export default LoginPage;
