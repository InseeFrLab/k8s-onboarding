import {
	Route,
	Routes as Switch,
	Navigate,
	useLocation,
} from 'react-router-dom';
import { withOidcSecure } from '@axa-fr/react-oidc';
import Cluster from './../cluster';

const Routes = () => {
	const { pathname } = useLocation();
	return (
		<Switch>
			<Route path="/" element={<div />} />
			<Route path="/cluster" element={withOidcSecure(Cluster)({})} />
			{!pathname.startsWith('/authentication') && (
				<Route element={<Navigate to="/" />} />
			)}
		</Switch>
	);
};

export default Routes;
