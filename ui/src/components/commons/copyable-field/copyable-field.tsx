import React, { useRef } from 'react';
import { InputAdornment, Input, InputLabel, Typography } from '@mui/material';
import FormControl from '@mui/material/FormControl';
import { IconButton } from '@mui/material';
import FileCopy from '@mui/icons-material/FileCopy';
import D from 'i18n';
import './copyable-field.scss';
interface Props {
	value: string;
	description?: string;
	label: string;
	copy?: boolean;
	row: number;
}

const CopyableField = ({ value, description, label, copy, row }: Props) => {
	const ref = useRef();
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
								<IconButton aria-label={D.btnCopyLabel} onClick={onCopy}>
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
