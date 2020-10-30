import React from 'react';
import { useHistory } from 'react-router-dom';
import { useKeycloak } from '@react-keycloak/web';
import D from 'i18n';

const Header = () => {
	const {
		keycloak: { authenticated, logout },
	} = useKeycloak();

	const { push } = useHistory();

	const action: any = () => {
		if (authenticated) {
			push('/');
			logout();
		} else {
			push('/cluster');
		}
	};
	const label: string = authenticated ? D.logout : D.login;

	return (
		<>
			<div>{D.appTitle}</div>
			<button type="button" onClick={action}>
				{label}
			</button>
			<h2>{D.appTitle}</h2>
			<p>{D.appDescription}</p>
		</>
	);
};

export default Header;
