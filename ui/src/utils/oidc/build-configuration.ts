import { OIDCBaseConfig, OIDCCustomConfig } from 'model/Oidc';

export const buildOidcConfiguration =
	(baseConfig: OIDCBaseConfig) => (customConfig: OIDCCustomConfig) => {
		const { authority, clientId } = customConfig;
		const { origin } = window.location;
		const updatedBaseConfigConfig = Object.entries(baseConfig.config).reduce(
			(acc, [k, v]) => ({
				...acc,
				[k]: typeof v === 'string' ? v.replace('my_origin', origin) : v,
			}),
			{}
		);
		const configuration = {
			...baseConfig,
			origin,
			config: { ...updatedBaseConfigConfig, authority, client_id: clientId },
		};
		return configuration;
	};
