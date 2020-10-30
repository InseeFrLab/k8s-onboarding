import { createMuiTheme, responsiveFontSizes } from '@material-ui/core/';

const theme = createMuiTheme({
	palette: {
		primary: { main: '#3078E4' },
	},
});

export default responsiveFontSizes(theme);
