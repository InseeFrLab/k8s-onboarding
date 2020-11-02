import React from 'react';
import ReactLoading from 'react-loading';
import D from 'i18n';
import { mainColor } from 'components/material-ui-theme';
import './loader.scss';

const Loader = () => (
	<>
		<div className="loader-img">
			<ReactLoading
				type={'bubbles'}
				color={mainColor}
				height={250}
				width={250}
			/>
		</div>
		<h1 className="loader-txt">{D.loading}</h1>
	</>
);

export default Loader;
