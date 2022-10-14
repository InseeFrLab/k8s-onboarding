import { createTheme, responsiveFontSizes } from '@mui/material';

export const mainColor = '#3078E4';
export const secondaryColor = '#FFFFFF';
export const thirdColor = '9BBFDF';

const theme = createTheme({
	palette: {
		primary: { main: mainColor },
		secondary: { main: secondaryColor },
	},
});

export default responsiveFontSizes(theme);
