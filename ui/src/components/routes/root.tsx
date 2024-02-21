import { ThemeProvider } from '@mui/material/styles';
import { BrowserRouter as Router } from 'react-router-dom';
import Routes from './routes';
import { Header } from './../commons';
import Theme from './../material-ui-theme';

// TODO
const Root = () => (
	<ThemeProvider theme={Theme}>
		<Router>
			<Header />
			<Routes />
		</Router>
	</ThemeProvider>
);

export default Root;
