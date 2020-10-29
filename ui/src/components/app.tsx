import React, { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import API from 'api';
import { confState } from 'store';
import D from 'i18n';

const App = () => {
	const [conf, setConf] = useRecoilState(confState);

	useEffect(() => {
		API.conf.then((r) => {
			setConf(r);
		});
	}, [setConf]);

	return (
		<div className="app" data-testid="app">
			{D.appTitle}
		</div>
	);
};

export default App;
