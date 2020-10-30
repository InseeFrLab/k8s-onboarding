export interface API {
	conf: (string?) => Promise<any>;
	cluster: (string?) => Promise<any>;
}
