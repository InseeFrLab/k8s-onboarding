import { useKeycloak } from '@react-keycloak/web';
import D from 'i18n';
import React from 'react';
import { useHistory } from 'react-router-dom';
import { AppBar } from '@material-ui/core/';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';

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
			<AppBar position="static">
				<Toolbar variant="dense">
					<Typography variant="h6" color="inherit">
						{D.appTitle}
					</Typography>
				</Toolbar>
			</AppBar>
			<button type="button" onClick={action}>
				{label}
			</button>
			<h2>{D.appTitle}</h2>
			<p>{D.appDescription}</p>
		</>
	);
};

export default Header;
