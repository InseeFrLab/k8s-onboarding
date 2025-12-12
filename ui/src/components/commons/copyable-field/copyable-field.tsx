import { useRef } from 'react';
import { InputAdornment, Input, InputLabel, Typography, FormControl,IconButton } from '@mui/material';
import FileCopy from '@mui/icons-material/FileCopy';
import './copyable-field.scss';
import { useTranslation } from 'react-i18next';
interface Props {
	value: string;
	description?: string;
	label: string;
	copy?: boolean;
	row: number;
}

const CopyableField = ({ value, description, label, copy, row }: Props) => {
	const { t } = useTranslation();
	const ref = useRef("");
	const onCopy = () => {
		navigator.clipboard.writeText(value);
		return false;
	};
	return (
		<div className={`row ${row % 2 !== 0 ? '' : 'row-major'}`}>
			{description && (
				<Typography variant="body1" justifyContent="left">
					{description}
				</Typography>
			)}
			<FormControl fullWidth variant="standard">
				{label ? <InputLabel>{label}</InputLabel> : null}
				<Input
					ref={ref}
					type="text"
					fullWidth
					value={value}
					endAdornment={
						<InputAdornment position="end">
							{copy ? (
								<IconButton aria-label={t('btnCopyLabel')} onClick={onCopy}>
									<FileCopy />
								</IconButton>
							) : null}
						</InputAdornment>
					}
				/>
			</FormControl>
		</div>
	);
};

export default CopyableField;
