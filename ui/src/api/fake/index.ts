import getEnv from 'env';
import { API } from '../api';

const get = (data: any) => () => Promise.resolve(data);

const api: API = {
	conf: get({
		authority: getEnv('REACT_APP_OIDC_AUTHORITY'),
		clientId: getEnv('REACT_APP_OIDC_CLIENT_ID'),
	}),
	cluster: get({
		apiserverUrl: 'api_server_url',
		namespace: 'my_namespace',
		token: 'my_token',
		user: 'user',
	}),
	createNamespace: get({}),
	setPermissionsToNamespace: get({}),
};

export default api;
