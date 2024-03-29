import Credentials from 'model/Credentials';

export interface API {
	conf: (string?) => Promise<any>;
	cluster: (string?, string?) => Promise<Credentials>;
	createNamespace: (string?, string?) => Promise<any>;
	setPermissionsToNamespace: (string?, string?) => Promise<any>;
	groups: (string?) => Promise<any>;
}
