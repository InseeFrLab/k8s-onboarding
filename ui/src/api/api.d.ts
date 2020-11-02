export interface API {
	conf: (string?) => Promise<any>;
	cluster: (string?) => Promise<any>;
	createNamespace: (string?, string?) => Promise<any>;
	setPermissionsToNamespace: (string?, string?) => Promise<any>;
}
