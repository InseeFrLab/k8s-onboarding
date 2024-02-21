import getEnv from './../../env';
import { API } from '../api';

const buildUrl = (url: string) =>
	`${getEnv('VITE_API_BASE_URL') || window.location.origin}/${url}`;

const request = (url: string) => (token: string) => (method: string) => {
	const headers = { Accept: 'application/json' };
	return fetch(buildUrl(url), {
		headers: token ? { ...headers, Authorization: `Bearer ${token}` } : headers,
		method: method,
	} as any).then((r) => r.json());
};

const requestNoData = (url: string) => (token: string) => (method: string) => {
	const headers = { Accept: 'application/json' };
	return fetch(buildUrl(url), {
		headers: token ? { ...headers, Authorization: `Bearer ${token}` } : headers,
		method: method,
	} as any);
};

// TODO : add body content
const post = (url: string) => (token: string) =>
	requestNoData(url)(token)('POST');
const get = (url: string) => (token: string) => request(url)(token)('GET');
const api: API = {
	conf: get(`api/public/configuration`),
	cluster: (token: string, group?: string) =>
		get(`api/cluster${group ? '/credentials/' + group : ''}`)(token),
	createNamespace: (token: string, group?: string) =>
		post(`api/cluster/namespace${group ? '/' + group : ''}`)(token),
	setPermissionsToNamespace: (token: string, group?: string) =>
		post(`api/cluster/permissions${group ? '/' + group : ''}`)(token),
	groups: (token: string) => get(`api/cluster/groups`)(token),
};

export default api;
