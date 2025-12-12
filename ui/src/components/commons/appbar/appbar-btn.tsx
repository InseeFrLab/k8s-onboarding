import { useOidc } from '@axa-fr/react-oidc';
import { useNavigate } from 'react-router-dom';
import { Button } from '@mui/material';
import PersonOutlineSharpIcon from '@mui/icons-material/PersonOutlineSharp';
import './appbar.scss';
import { useTranslation } from 'react-i18next';

const AppBarBtn = () => {
	const { logout, isAuthenticated } = useOidc();

	const navigate = useNavigate();
	const { t } = useTranslation();
	const action: any = () => {
		if (isAuthenticated) {
			logout().then(() => navigate('/'));
		} else {
			navigate('/cluster');
		}
	};
	const label: string = isAuthenticated ? t('logout') : t('login');

	return (
		<Button
			variant="outlined"
			color="secondary"
			startIcon={<PersonOutlineSharpIcon />}
			onClick={action}
		>
			{label}
		</Button>
	);
};

export default AppBarBtn;
