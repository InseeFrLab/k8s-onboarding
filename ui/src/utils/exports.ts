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
		}: Credentials) =>
			`#!/bin/sh
kubectl config set-cluster ${clusterName} --server=${apiserverUrl}
kubectl config set-credentials ${user} --token ${token} 
kubectl config set-context ${clusterName} --user=${user} --cluster=${clusterName} --namespace=${namespace}
kubectl config use-context ${clusterName}`,
	},
];
