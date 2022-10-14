import React from 'react';
import { useOidc } from '@axa-fr/react-oidc';
import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material/';
import D from 'i18n';
import PersonOutlineSharpIcon from '@mui/icons-material/PersonOutlineSharp';
import './appbar.scss';

const AppBarBtn = () => {
	const { logout, isAuthenticated } = useOidc();

	const navigate = useNavigate();

	const action: any = () => {
		if (isAuthenticated) {
			logout().then(() => navigate('/'));
		} else {
			navigate('/cluster');
		}
	};
	const label: string = isAuthenticated ? D.logout : D.login;

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
