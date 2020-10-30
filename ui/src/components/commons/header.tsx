import { useKeycloak } from '@react-keycloak/web';
import D from 'i18n';
import React from 'react';
import { useHistory } from 'react-router-dom';
import {
	AppBar,
	Button,
	Divider,
	Typography,
	Grid,
	Container,
	Box,
} from '@material-ui/core/';
import Toolbar from '@material-ui/core/Toolbar';
import PersonOutlineSharpIcon from '@material-ui/icons/PersonOutlineSharp';
import './header.scss';

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
			<AppBar>
				<Toolbar variant="dense">
					<Grid
						container
						direction="row"
						justify="space-between"
						alignItems="center"
					>
						<Typography variant="h4" className="appbar_title">
							{D.appTitle}
						</Typography>
						<Button
							variant="outlined"
							color="secondary"
							startIcon={<PersonOutlineSharpIcon />}
							onClick={action}
						>
							{label}
						</Button>
					</Grid>
				</Toolbar>
			</AppBar>
			<Box m={10} />
			<Grid
				container
				className="mycontainer"
				justify="center"
				alignItems="baseline"
				direction="column"
			>
				<Grid>
					<Typography variant="h2" gutterBottom className="title">
						{D.appTitle}
					</Typography>
					<Typography variant="body1" gutterBottom className="description">
						{D.appDescription}
					</Typography>
					<Box m={2} />
					<Button
						variant="outlined"
						color="primary"
						className="tutorial_button"
					>
						{D.header_button_tutorial}
					</Button>
				</Grid>
				<Box m={2} />
			</Grid>
			<Divider className="divider" />
			<Box m={2} />
		</>
	);
};

export default Header;
