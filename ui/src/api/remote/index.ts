import getEnv from 'env';
import { API } from '../api';

const buildUrl = (url: string) =>
	`${getEnv('REACT_APP_API_BASE_URL') || window.location.origin}/${url}`;

const get = (url: string) => () => fetch(buildUrl(url)).then((r) => r.json());

const api: API = {
	conf: get(`api/configuration`),
	cluster: get(`api/cluster`),
};

export default api;
