import React from 'react';
import { useReactOidc } from '@axa-fr/react-oidc-context';
import { useHistory } from 'react-router-dom';
import { Button } from '@material-ui/core/';
import D from 'i18n';
import PersonOutlineSharpIcon from '@material-ui/icons/PersonOutlineSharp';
import './appbar.scss';

const AppBarBtn = () => {
	const { oidcUser, logout } = useReactOidc();

	const { push } = useHistory();

	const action: any = () => {
		if (oidcUser) {
			logout();
		} else {
			push('/cluster');
		}
	};
	const label: string = oidcUser ? D.logout : D.login;

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
