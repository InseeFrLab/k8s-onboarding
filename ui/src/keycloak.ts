import Keycloak from 'keycloak-js';

const builDkeycloak = ({ authUrl: url, realm, resource: clientId }: any) =>
	Keycloak({
		url,
		realm,
		clientId,
	});

export default builDkeycloak;
