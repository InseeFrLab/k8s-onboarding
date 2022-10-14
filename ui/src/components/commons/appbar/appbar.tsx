import D from 'i18n';
import React from 'react';
import { AppBar, Typography, Grid } from '@mui/material/';
import Toolbar from '@mui/material/Toolbar';
import AppBarBtn from './appbar-btn';
import './appbar.scss';

const MyAppBar = ({ displayBtn }: { displayBtn?: boolean }) => (
	<AppBar>
		<Toolbar variant="dense">
			<Grid
				container
				direction="row"
				justifyContent="space-between"
				alignItems="center"
			>
				<Typography variant="h4" className="appbar_title">
					{D.appTitle}
				</Typography>
				{displayBtn && <AppBarBtn />}
			</Grid>
		</Toolbar>
	</AppBar>
);

export default MyAppBar;
