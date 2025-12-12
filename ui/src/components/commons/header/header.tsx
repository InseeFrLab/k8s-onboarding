import { Divider, Typography, Grid, Box } from '@mui/material';
import AppBar from '../appbar';
import './header.scss';
import { useTranslation } from 'react-i18next';

const Header = () => {
	const { t } = useTranslation();
	return (
		<Grid
			container
			direction="column"
			justifyContent="center"
			alignItems="baseline">
			<Grid size={{ xs: 12 }}>
				<AppBar displayBtn={true} />
			</Grid>
			<Grid size={{ xs: 12 }}>
				<Box m={10} />
				<Grid
					container
					justifyContent="center"
					alignItems="baseline"
					direction="column"
				>
					<Grid size={{xs:12}}>
						<Typography variant="body1" gutterBottom className="description">
							{t('appDescription')}
						</Typography>
					</Grid>
					<Box m={2} />
				</Grid>
				<Divider className="divider" />
				<Box m={2} />
			</Grid>
		</Grid>
	);
};

export default Header;
