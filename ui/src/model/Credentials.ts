export default interface Credentials {
	apiserverUrl: string;
	namespace: string;
	token: string;
	user: string;
	onboarded: boolean;
	clusterName: string;
	insecure?: boolean;
}
