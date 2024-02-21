import { FileCopy, Save } from '@mui/icons-material';
import { IconButton } from '@mui/material';
import { saveAs } from 'file-saver';
import { useTranslation } from 'react-i18next';

const CopyButton = ({ content }: any) => {
	const { t } = useTranslation();
	const copy = () => {
		navigator.clipboard.writeText(content);
		return false;
	};
	return (
		<IconButton aria-label={t('btnSaveAsLabel')} onClick={copy}>
			<FileCopy />
		</IconButton>
	);
};

const ExportFileButton = ({ fileName, content }: any) => {
	const { t } = useTranslation();
	const save = () => {
		var blob = new Blob([content], {
			type: 'text/plain;charset=utf-8',
		});
		saveAs(blob, fileName);
		return false;
	};
	return (
		<IconButton aria-label={t('btnCopyLabel')} onClick={save}>
			<Save />
		</IconButton>
	);
};

export { ExportFileButton, CopyButton };
