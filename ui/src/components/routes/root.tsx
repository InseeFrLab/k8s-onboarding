import { MuiThemeProvider } from '@material-ui/core';
import React from 'react';
import { BrowserRouter as Router } from 'react-router-dom';
import Routes from './routes';
import { Header } from 'components/commons';
import Theme from './../material-ui-theme';

// TODO
const Root = () => (
	<MuiThemeProvider theme={Theme}>
		<Router>
			<Header />
			<Routes />
		</Router>
	</MuiThemeProvider>
);

export default Root;
