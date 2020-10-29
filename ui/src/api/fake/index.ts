interface API {
	conf: Promise<any>;
}

const api: API = {
	conf: Promise.resolve({ test: 'test' }),
};

export default api;
