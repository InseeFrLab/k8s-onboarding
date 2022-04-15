export const filterGroups = (groups: string[], filter: string) => {
	if (filter.length > 0) {
		var filterRegex = new RegExp(filter, 'i');
		return groups.filter((value) => filterRegex.test(value));
	}
	return groups;
};
