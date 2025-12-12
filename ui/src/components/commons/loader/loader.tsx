import './loader.scss';
import { useTranslation } from 'react-i18next';

const Loader = () => {
	const { t } = useTranslation();
	return (
		<>
			<div className="loader-img">
				Loading ...
			</div>
			<h1 className="loader-txt">{t('loading')}</h1>
		</>
	);
}

export default Loader;
