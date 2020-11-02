import getEnv from 'env';
import { API } from '../api';

const buildUrl = (url: string) =>
	`${getEnv('REACT_APP_API_BASE_URL') || window.location.origin}/${url}`;

const request = (url: string) => (token: string) => (method: string) => {
	const headers = { Accept: 'application/json' };
	return fetch(buildUrl(url), {
		headers: token ? { ...headers, Authorization: `Bearer ${token}` } : headers,
		method: method,
	} as any).then((r) => r.json());
};

// TODO : add body content
const post = (url: string) => (token: string) => request(url)(token)('POST');
const get = (url: string) => (token: string) => request(url)(token)('GET');
const api: API = {
	conf: get(`api/public/configuration`),
	cluster: (token: string) => get(`api/cluster`)(token),
	createNamespace: (token: string, namespaceId: string) =>
		post(`api/cluster/namespace/${namespaceId}`)(token),
	setPermissionsToNamespace: (token: string, namespaceId: string) =>
		post(`api/cluster/namespace/${namespaceId}/permissions`)(token),
};

export default api;
