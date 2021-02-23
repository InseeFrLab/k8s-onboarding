import Credentials from 'model/Credentials';

export const exportTypes = [
	{
		id: 'commandline',
		label: 'init-kub.sh',
		fileName: 'init-kub.sh',
		text: ({
			apiserverUrl,
			user,
			token,
			namespace,
			clusterName,
			insecure,
		}: Credentials) =>
			`#!/bin/sh
kubectl config set-cluster ${clusterName} --server=${apiserverUrl} ${
				insecure ? '--insecure-skip-tls-verify=true' : ''
			}
kubectl config set-credentials ${user} --token ${token} 
kubectl config set-context ${clusterName} --user=${user} --cluster=${clusterName} --namespace=${namespace}
kubectl config use-context ${clusterName}`,
	},
];
