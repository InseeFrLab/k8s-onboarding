import { createMuiTheme, responsiveFontSizes } from '@material-ui/core/';

export const mainColor = '#3078E4';
export const secondaryColor = '#FFFFFF';

const theme = createMuiTheme({
	palette: {
		primary: { main: mainColor },
		secondary: { main: secondaryColor },
	},
});

export default responsiveFontSizes(theme);
