import React from 'react';
import App from './components/app';
import { createRoot } from 'react-dom/client';
import './index.scss';
import './i18n';

const container = document.getElementById('root');
const root = createRoot(container!);
root.render(
	<React.StrictMode>
		<App />
	</React.StrictMode>
);
