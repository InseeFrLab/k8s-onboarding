import { createMuiTheme, responsiveFontSizes } from '@material-ui/core/';

const theme = createMuiTheme({
	palette: {
		primary: { main: '#3078E4' },
		secondary: { main: '#FFFFFF' },
	},
});

export default responsiveFontSizes(theme);
