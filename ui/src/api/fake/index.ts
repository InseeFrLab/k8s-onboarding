import { API } from '../api';

const get = (data: any) => () => Promise.resolve(data);

const api: API = {
	conf: get({ test: 'test' }),
	cluster: get({
		name: 'google-cloud-demo',
		'api-server': 'https://oidc.dev.insee.io',
		token: 'my-token',
		script: 'script content',
	}),
};

export default api;
