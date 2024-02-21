import { useOidcAccessToken } from '@axa-fr/react-oidc';
import Button from '@mui/material/Button';
import Step from '@mui/material/Step';
import StepLabel from '@mui/material/StepLabel';
import Stepper from '@mui/material/Stepper';
import { useTheme } from '@mui/material/styles';
import Typography from '@mui/material/Typography';
import API from './../../../api';
import { Loader } from './../../../components/commons';
import Credentials from './../../../model/Credentials';
import { AllowedGroup } from './../../../model/Group';
import React from 'react';
import ReactMarkdown from 'react-markdown';

function getSteps(isGroup: boolean) {
	return [
		'Bienvenue !',
		isGroup
			? "Créer l'espace de travail du groupe (namespace)"
			: 'Créer mon espace de travail (namespace)',
		isGroup
			? 'Attribuer les droits au groupe'
			: 'Attribuer les droits à mon utilisateur',
		"C'est prêt !",
	];
}

function getButtonMessage() {
	return [
		"C'est compris, allons y !",
		"Créer l'espace de travail",
		'Attribuer les droits',
		'Commencer à y déployer des applications',
	];
}

function getStepContent(
	stepIndex: number,
	cluster: Credentials,
	allowedGroup?: string
) {
	const isGroup = Boolean(allowedGroup);
	switch (stepIndex) {
		case 0:
			return `Bienvenue sur la plateforme Kubernetes ${cluster.clusterName} !  
Cette plateforme est soumise aux conditions d'utilisations suivantes :  
* Aucune garantie de service que ce soit en confidentialité, intégrité ou disponibilité  
* Limité aux applications opensource  
* Limité aux données de test anonymisées  `;
		case 1:
			return `Cette plateforme est partagée avec d'autres utilisateurs, on va donc ${isGroup
				? ' créer un espace réservé pour le groupe ' + allowedGroup
				: 'se créer un espace personnel'
				}.  
Pour simplifier, on va attribuer le namespace ${cluster.namespace}.  
			Note : dans la vraie vie, c'est équivalent à kubectl create namespace ${cluster.namespace
				}`;
		case 2:
			return `Maintenant que le namespace a été créé, il nous faut attributer les droits ${isGroup
				? 'au groupe ' + allowedGroup
				: "à l'utilisateur " + cluster.user
				}`;
		case 3:
			return "C'est prêt :)";
		default:
			return '?';
	}
}

export default function Welcome({
	allowedGroup,
	credentials,
	onFinish,
}: {
	allowedGroup?: AllowedGroup;
	credentials?: Credentials;
	onFinish?: () => void;
}) {
	const theme = useTheme();

	const style = {
		root: {
			width: '100%',
		},
		backButton: {
			marginRight: theme.spacing(1),
		},
		instructions: {
			marginTop: theme.spacing(1),
			marginBottom: theme.spacing(1),
		},
	};

	const [activeStep, setActiveStep] = React.useState(0);
	const steps = getSteps(Boolean(allowedGroup));

	const { accessToken: token } = useOidcAccessToken();

	const handleNext = () => {
		if (activeStep >= steps.length - 1) {
			if (onFinish) {
				onFinish();
			}
		} else if (activeStep === 1) {
			API.createNamespace(token, allowedGroup?.group).then(() => {
				setActiveStep((prevActiveStep) => prevActiveStep + 1);
			});
		} else if (activeStep === 2) {
			API.setPermissionsToNamespace(token, allowedGroup?.group).then(() => {
				setActiveStep((prevActiveStep) => prevActiveStep + 1);
			});
		} else {
			setActiveStep((prevActiveStep) => prevActiveStep + 1);
		}
	};

	const handleBack = () => {
		setActiveStep((prevActiveStep) => prevActiveStep - 1);
	};

	const handleReset = () => {
		setActiveStep(0);
	};

	if (!credentials) return <Loader />;

	return (
		<div style={style.root}>
			<Stepper activeStep={activeStep} alternativeLabel>
				{steps.map((label) => (
					<Step key={label}>
						<StepLabel>{label}</StepLabel>
					</Step>
				))}
			</Stepper>
			<div>
				{activeStep === steps.length ? (
					<div>
						<Typography sx={style.instructions}>All steps completed</Typography>
						<Button onClick={handleReset}>Reset</Button>
					</div>
				) : (
					<div>
						<ReactMarkdown>
							{getStepContent(activeStep, credentials, allowedGroup?.namespace)}
						</ReactMarkdown>
						<div>
							{activeStep !== 0 ? (
								<Button
									variant="contained"
									color="secondary"
									onClick={handleBack}
								>
									{'Précédent'}
								</Button>
							) : (
								<></>
							)}

							<Button variant="contained" color="primary" onClick={handleNext}>
								{getButtonMessage()[activeStep]}
							</Button>
						</div>
					</div>
				)}
			</div>
		</div>
	);
}
