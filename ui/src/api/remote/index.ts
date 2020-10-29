interface API {
	conf: Promise<any>;
}

const get = (url: string) => fetch(url).then((r) => r.json());

const api: API = {
	conf: get(`${window.location.origin}/api/XXX`),
};

export default api;
