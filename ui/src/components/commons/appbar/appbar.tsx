import { AppBar, Typography, Grid } from '@mui/material/';
import Toolbar from '@mui/material/Toolbar';
import AppBarBtn from './appbar-btn';
import './appbar.scss';
import { useTranslation } from 'react-i18next';

const MyAppBar = ({ displayBtn }: { displayBtn?: boolean }) => {

	const { t } = useTranslation();
	return (
		<AppBar>
			<Toolbar variant="dense">
				<Grid
					container
					direction="row"
					justifyContent="space-between"
					alignItems="center"
				>
					<Typography variant="h4" className="appbar_title">
						{t('appTitle')}
					</Typography>
					{displayBtn && <AppBarBtn />}
				</Grid>
			</Toolbar>
		</AppBar>)
}

export default MyAppBar;
