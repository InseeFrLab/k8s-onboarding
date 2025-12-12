import { AppBar, Typography, Toolbar } from '@mui/material';
import AppBarBtn from './appbar-btn';
import { useTranslation } from 'react-i18next';

const MyAppBar = ({ displayBtn }: { displayBtn?: boolean }) => {

	const { t } = useTranslation();
	return (
		<AppBar>
			<Toolbar>
				<Typography variant="h4" component="div" sx={{ flexGrow: 1 }}>
					{t('appTitle')}
				</Typography>
				{displayBtn && <AppBarBtn />}
			</Toolbar>
		</AppBar>)
}

export default MyAppBar;
