import { useKeycloak } from '@react-keycloak/web';
import D from 'i18n';
import React from 'react';
import { useHistory } from 'react-router-dom';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import IconButton from '@material-ui/core/IconButton';
import MenuIcon from '@material-ui/icons/Menu';

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
					<IconButton edge="start" color="inherit" aria-label="menu">
						<MenuIcon />
					</IconButton>
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
