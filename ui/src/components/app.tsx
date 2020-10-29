import React, { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import API from 'api';
import { confState } from 'store';
import D from 'i18n';
import Root from './root';

const App = () => {
	const setConf = useRecoilState(confState)[1];

	useEffect(() => {
		API.conf.then((r) => {
			setConf(r);
		});
	}, [setConf]);

	return (
		<div className="app" data-testid="app">
			{D.appTitle}
			<Root />
		</div>
	);
};

export default App;
