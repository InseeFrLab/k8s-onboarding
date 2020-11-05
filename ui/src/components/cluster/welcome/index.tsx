import React, { useState } from 'react';
import { Loader } from 'components/commons';
import ReactMarkdown from 'react-markdown';
import { makeStyles } from '@material-ui/core/styles';
import Stepper from '@material-ui/core/Stepper';
import Step from '@material-ui/core/Step';
import StepLabel from '@material-ui/core/StepLabel';
import Button from '@material-ui/core/Button';
import Typography from '@material-ui/core/Typography';
import { useHistory } from 'react-router-dom';
import API from 'api';
import { useKeycloak } from '@react-keycloak/web';
import Credentials from 'model/Credentials';

const useStyles = makeStyles((theme) => ({
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
}));

function getSteps() {
	return [
		'Bienvenue !',
		'Créer mon espace de travail (namespace)',
		'Attribuer les droits à mon utilisateur',
		"C'est prêt !",
	];
}

function getButtonMessage() {
	return [
		"C'est compris, allons y !",
		'Créer mon espace de travail (namespace)',
		'Attribuer les droits à mon utilisateur',
		'Commencer à déployer des applications',
	];
}

function getStepContent(stepIndex: number, cluster: Credentials) {
	switch (stepIndex) {
		case 0:
			return `Bienvenue sur la plateforme Kubernetes ${cluster.clusterName} !  
Cette plateforme est soumise aux conditions d'utilisations suivantes :  
* Aucune garantie de service que ce soit en confidentialité, intégrité ou disponibilité  
* Limité aux applications opensource  
* Limité aux données de test anonymisées  `;
		case 1:
			return `Cette plateforme est partagée avec d'autres utilisateurs, on va donc se créer un espace personnel.  
Pour simplifier, on va s'attribuer le namespace ${cluster.namespace}.  
			Note : dans la vraie vie, c'est équivalent à kubectl create namespace ${cluster.namespace}`;
		case 2:
			return `Maintenant que le namespace a été créé, il nous faut attributer les droits à ${cluster.user}.`;
		case 3:
			return "C'est prêt :)";
		default:
			return '?';
	}
}

export default function Welcome() {
	const classes = useStyles();
	const [activeStep, setActiveStep] = React.useState(0);
	const [credentials, setCredentials] = useState<Credentials>();
	const steps = getSteps();
	const { push } = useHistory();
	const {
		keycloak: { token },
	} = useKeycloak();

	React.useEffect(() => {
		API.cluster(token).then((c) => {
			if (c.onboarded) {
				push('/cluster');
			} else {
				setCredentials(c);
			}
		});
	}, [token, push]);

	const handleNext = () => {
		if (activeStep >= steps.length - 1) {
			push('/cluster');
		} else if (activeStep === 1) {
			API.createNamespace(token, credentials?.namespace).then((c) => {
				setActiveStep((prevActiveStep) => prevActiveStep + 1);
			});
		} else if (activeStep === 2) {
			API.setPermissionsToNamespace(token, credentials?.namespace).then((c) => {
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
		<div className={classes.root}>
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
						<Typography className={classes.instructions}>
							All steps completed
						</Typography>
						<Button onClick={handleReset}>Reset</Button>
					</div>
				) : (
					<div>
						<Typography className={classes.instructions}>
							<ReactMarkdown>
								{getStepContent(activeStep, credentials)}
							</ReactMarkdown>
						</Typography>
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
