import getEnv from 'env';
import { API } from '../api';

const buildUrl = (url: string) =>
	`${getEnv('REACT_APP_API_BASE_URL') || window.location.origin}/${url}`;

const get = (url: string) => (token: string) => {
	const headers = { Accept: 'application/json' };
	return fetch(buildUrl(url), {
		headers: token ? { ...headers, Authorization: `Bearer ${token}` } : headers,
	} as any).then((r) => r.json());
};

const api: API = {
	conf: get(`api/public/configuration`),
	cluster: (token: string) => get(`api/cluster`)(token),
};

export default api;
