import fake from './fake';
import remote from './remote';

const getAPIPaths = () => {
	if (process.env.REACT_APP_API_MODE === 'fake') return fake;
	return remote;
};

export default getAPIPaths();
