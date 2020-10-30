import getEnv from 'env';
import { API } from '../api';

const get = (data: any) => () => Promise.resolve(data);

const api: API = {
	conf: get({
		authUrl: getEnv('REACT_APP_KEYCLOAK_URL'),
		realm: getEnv('REACT_APP_KEYCLOAK_REALM'),
		resource: getEnv('REACT_APP_KEYCLOAK_RESOURCE'),
	}),
	cluster: get({
		name: 'google-cloud-demo',
		'api-server': 'https://oidc.dev.insee.io',
		token: 'my-token',
		script: 'script content',
	}),
};

export default api;
