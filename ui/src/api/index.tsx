import fake from './fake';
import remote from './remote';

const getAPIPaths = () => {
	if (import.meta.env.VITE_API_MODE === 'fake') return fake;
	return remote;
};

export default getAPIPaths();
