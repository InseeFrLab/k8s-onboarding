import React, { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import API from 'api';
import { confState } from 'store';

const App = () => {
	const [conf, setConf] = useRecoilState(confState);

	useEffect(() => {
		API.conf.then((r) => {
			setConf(r);
		});
	}, [setConf]);
	console.log(conf);
	return (
		<div className="app" data-testid="app">
			Hello
		</div>
	);
};

export default App;
