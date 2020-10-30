import Keycloak from 'keycloak-js';

const builDkeycloak = ({ authUrl: url, realm, clientId }: any) =>
	Keycloak({
		url,
		realm,
		clientId,
	});

export default builDkeycloak;
