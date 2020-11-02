export const exportTypes = [
	{
		id: 'commandline',
		label: 'init-kub.sh',
		fileName: 'init-kub.sh',
		text: ({ apiserverUrl, user, token, namespace }: any) =>
			`#!/bin/sh
kubectl config set-cluster MY_KUB_SERVER_DEV --server=${apiserverUrl}
kubectl config set-credentials ${user} --token ${token} 
kubectl config set-context MY_KUB_SERVER_DEV --user=${user} --cluster=MY_KUB_SERVER_DEV --namespace=${namespace}
kubectl config use-context MY_KUB_SERVER_DEV`,
	},
];
