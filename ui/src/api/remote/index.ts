import getEnv from 'env';
import { API } from '../api';

const buildUrl = (url: string) =>
	`${getEnv('REACT_APP_API_BASE_URL') || window.location.origin}/${url}`;

const get = (url: string) => (token: string) =>
	fetch(buildUrl(url), {
		headers: { Accept: 'application/json', Authorization: `Bearer ${token}` },
	} as any).then((r) => r.json());

const api: API = {
	conf: get(`api/public/configuration`),
	cluster: (token: string) => get(`api/cluster`)(token),
};

export default api;
