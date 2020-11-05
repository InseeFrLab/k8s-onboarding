import { useKeycloak } from '@react-keycloak/web';
import D from 'i18n';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { Button } from '@material-ui/core/';
import PersonOutlineSharpIcon from '@material-ui/icons/PersonOutlineSharp';
import './appbar.scss';

const AppBarBtn = () => {
	const {
		keycloak: { authenticated, logout },
	} = useKeycloak();

	const { push } = useHistory();

	const action: any = () => {
		if (authenticated) {
			push('/');
			logout();
		} else {
			push('/welcome');
		}
	};
	const label: string = authenticated ? D.logout : D.login;

	return (
		<Button
			variant="outlined"
			color="secondary"
			startIcon={<PersonOutlineSharpIcon />}
			onClick={action}
		>
			{label}
		</Button>
	);
};

export default AppBarBtn;
