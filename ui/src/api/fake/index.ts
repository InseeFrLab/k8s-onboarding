import getEnv from 'env';
import { API } from '../api';

const get = (data: any) => () => Promise.resolve(data);

const api: API = {
	conf: get({
		authority: getEnv('REACT_APP_OIDC_AUTHORITY'),
		clientId: getEnv('REACT_APP_OIDC_CLIENT_ID'),
		redirect_uri: getEnv('REACT_APP_OIDC_REDIRECT_URI'),
		response_type: getEnv('REACT_APP_OIDC_RESPONSE_TYPE'),
		post_logout_redirect_uri: getEnv('REACT_APP_OIDC_POST_LOGOUT_URI'),
		scope: getEnv('REACT_APP_OIDC_SCOPE'),
		silent_redirect_uri: getEnv('REACT_APP_OIDC_SILENT_REDIRECT_URI'),
		automaticSilentRenew: getEnv('REACT_APP_OIDC_AUTOMATIC_SILENT_RENEW'),
		loadUserInfo: getEnv('REACT_APP_OIDC_LOAD_USER_INFO'),
	}),
	cluster: get({
		apiserverUrl: 'api_server_url',
		namespace: 'my_namespace',
		token: 'my_token',
		user: 'user',
		onboarded: false,
		clusterName: 'clusterName',
	}),
	createNamespace: get({}),
	setPermissionsToNamespace: get({}),
};

export default api;
