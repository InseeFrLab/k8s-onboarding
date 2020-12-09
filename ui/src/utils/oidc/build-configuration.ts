import { OIDCBaseConfig, OIDCCustomConfig } from 'model/Oidc';

export const buildOidcConfiguration = (baseConfig: OIDCBaseConfig) => (
	customConfig: OIDCCustomConfig
) => {
	const { authority, clientId } = customConfig;
	const configuration = {
		...baseConfig,
		origin: window.location.origin,
		config: {
			...baseConfig.config,
			authority,
			client_id: clientId,
			redirect_uri: window.location.origin,
		},
	};
	return configuration;
};
