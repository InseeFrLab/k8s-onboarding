export interface OIDCBaseConfig {
	origin: string;
	isEnabled: boolean;
	config: {
		authority: string;
		client_id: string;
		redirect_uri: string;
		response_type: string;
		post_logout_redirect_uri: string;
		scope: string;
		silent_redirect_uri: string;
		automaticSilentRenew: boolean;
		loadUserInfo: boolean;
	};
}

export interface OIDCCustomConfig {
	authority?: string;
	clientId?: string;
	groupFilter?: string;
}

export interface UIProperties extends OIDCCustomConfig {
	userNamespaceEnabled?: boolean;
	userCanCreateNs?: boolean;
}
