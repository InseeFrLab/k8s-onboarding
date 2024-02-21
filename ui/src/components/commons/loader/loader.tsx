import ReactLoading from 'react-loading';
import { mainColor } from './../../../components/material-ui-theme';
import './loader.scss';
import { useTranslation } from 'react-i18next';

const Loader = () => {
	const { t } = useTranslation();
	return (
		<>
			<div className="loader-img">
				<ReactLoading
					type={'bubbles'}
					color={mainColor}
					height={250}
					width={250}
				/>
			</div>
			<h1 className="loader-txt">{t('loading')}</h1>
		</>
	);
}

export default Loader;
