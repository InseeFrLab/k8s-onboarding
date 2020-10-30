import React, { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import API from 'api';
import { confState } from 'store';
import Root from './routes/root';

const App = () => {
	const setConf = useRecoilState(confState)[1];

	useEffect(() => {
		API.conf()
			.then((r: any) => {
				setConf(r);
			})
			.catch(() => {
				console.error('fetch configuration');
			});
	}, [setConf]);

	return (
		<div className="app" data-testid="app">
			<Root />
		</div>
	);
};

export default App;
