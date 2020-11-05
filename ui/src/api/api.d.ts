import Credentials from 'model/Credentials';

export interface API {
	conf: (string?) => Promise<any>;
	cluster: (string?) => Promise<Credentials>;
	createNamespace: (string?, string?) => Promise<any>;
	setPermissionsToNamespace: (string?, string?) => Promise<any>;
}
