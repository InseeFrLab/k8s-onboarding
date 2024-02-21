import { Divider, Typography, Grid, Box } from '@mui/material/';
import AppBar from '../appbar';
import './header.scss';
import { useTranslation } from 'react-i18next';

const Header = () => {
	const { t } = useTranslation();
	return (
		<>
			<AppBar displayBtn />
			<Box m={10} />
			<Grid
				container
				justifyContent="center"
				alignItems="baseline"
				direction="column"
			>
				<Grid>
					<Typography variant="body1" gutterBottom className="description">
						{t('appDescription')}
					</Typography>
					{/*<Box m={2} />
					 <Button
						variant="outlined"
						color="primary"
						className="tutorial_button"
					>
						{D.header_button_tutorial}
					</Button> */}
				</Grid>
				<Box m={2} />
			</Grid>
			<Divider className="divider" />
			<Box m={2} />
		</>
	);
};

export default Header;
