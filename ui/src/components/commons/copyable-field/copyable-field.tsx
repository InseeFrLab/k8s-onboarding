import React, { useRef } from 'react';
import {
	InputAdornment,
	Input,
	InputLabel,
	Typography,
} from '@material-ui/core';
import FormControl from '@material-ui/core/FormControl';
import * as clipboard from 'clipboard-polyfill';
import { IconButton } from '@material-ui/core';
import FileCopy from '@material-ui/icons/FileCopy';
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
		clipboard.writeText(value);
		return false;
	};
	return (
		<div className={`row ${row % 2 !== 0 ? '' : 'row-major'}`}>
			{description && (
				<Typography variant="body1" align="left">
					{description}
				</Typography>
			)}
			<FormControl style={{ width: '100%' }}>
				{label ? <InputLabel>{label}</InputLabel> : null}
				<Input
					ref={ref}
					type="text"
					fullWidth
					value={value}
					endAdornment={
						<InputAdornment position="end">
							{copy ? (
								<IconButton aria-label={D.copy} onClick={onCopy}>
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
