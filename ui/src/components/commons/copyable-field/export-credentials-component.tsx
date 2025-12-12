import React, { useState } from 'react';
import { Typography, Grid, Select, MenuItem } from '@mui/material';
import { ExportFileButton, CopyButton } from '../buttons';

const ExportCredentialsField = ({ credentials, exportTypes, text }: any) => {
	const [exportTypeId, changeExportType] = useState(exportTypes[0].id);
	const exportType = exportTypes.find((type: any) => type.id === exportTypeId);

	const handleChange = (e: any) => changeExportType(e.target.value);
	return (
		<React.Fragment>
			<Grid
				container
				direction="row"
				justifyContent="space-between"
				alignItems="baseline"
			>
				<Grid container size={{ xs: 10}} justifyContent="flex-start">
					<Grid size={{ xs: 7}}>
						<Typography variant="body1">{text}</Typography>
					</Grid>
					<Grid>
						<Select
							style={{ minWidth: 240 }}
							value={exportTypeId}
							onChange={handleChange}
							variant="standard"
						>
							{exportTypes.map(({ id, label }: any) => (
								<MenuItem key={id} value={id}>
									{label}
								</MenuItem>
							))}
						</Select>
					</Grid>
				</Grid>
				<Grid container size={{ xs: 2}} justifyContent="flex-end">
					<Grid>
						<ExportFileButton
							fileName={exportType.fileName}
							content={exportType.text(credentials)}
						/>
					</Grid>
					<Grid>
						<CopyButton content={exportType.text(credentials)} />
					</Grid>
				</Grid>
			</Grid>
		</React.Fragment>
	);
};

export default ExportCredentialsField;
