import { AgencyLocation } from './agency-location';

export const AGENCYLOCATIONS: AgencyLocation[] = [
	{ locationId: 8210, agencyId: 'SDP', locationType: 'CLAS', description: 'SDP-SCHOOL', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8211, agencyId: 'SDP', locationType: 'CLAS', description: 'SDP-SCHOOL-LIBRARY', parentLocationId: 8210, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8212, agencyId: 'SDP', locationType: 'CLAS', description: 'SDP-SCHOOL-CLASSRM', parentLocationId: 8210, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8294, agencyId: 'SDP', locationType: 'EXER', description: 'SDP-REC BUILDING-CRAFT RM', parentLocationId: 8289, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8295, agencyId: 'SDP', locationType: 'EXER', description: 'SDP-REC BUILDING-BATHROOM', parentLocationId: 8289, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8296, agencyId: 'SDP', locationType: 'EXER', description: 'SDP-REC BUILDING-CONTROL RM', parentLocationId: 8289, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8290, agencyId: 'SDP', locationType: 'EXER', description: 'SDP-REC BUILDING-HALLWAY', parentLocationId: 8289, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8291, agencyId: 'SDP', locationType: 'EXER', description: 'SDP-REC BUILDING-WEIGHT RM', parentLocationId: 8289, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8292, agencyId: 'SDP', locationType: 'EXER', description: 'SDP-REC BUILDING-MUSIC', parentLocationId: 8289, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8293, agencyId: 'SDP', locationType: 'EXER', description: 'SDP-REC BUILDING-GYM', parentLocationId: 8289, currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8724, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-PEN-DEATH ROW', parentLocationId: 8723, operationalCapacity: 1, currentOccupancy: 1, livingUnit: true, housingUnitType: 'DR',  },
	{ locationId: 7018, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-WEST-SOUTH 1', parentLocationId: 6921, operationalCapacity: 26, currentOccupancy: 19, livingUnit: true, housingUnitType: 'SPEC NEEDS',  },
	{
		locationId: 7032,
		agencyId: 'SDP',
		locationType: 'TIER',
		description: 'SDP-WEST-SOUTH 2',
		parentLocationId: 6921,
		operationalCapacity: 34,
		currentOccupancy: 26,
		livingUnit: true,
		housingUnitType: 'SPEC NEEDS',
		assignedInmates: [
			{
				inmateId: 38268,
				bookingId: '2015-037258',
				offenderId: '16480',
				firstName: 'Giovani',
				middleName: 'Louis',
				lastName: 'Jenkins'
			},
			{ inmateId: 38269, bookingId: '2015-037259', offenderId: '39105', firstName: 'Alan', lastName: 'Goodwin',  },
			{ inmateId: 38393, bookingId: '2015-037382', offenderId: '39208', firstName: 'Marquise', lastName: 'Mullen',  },
			{ inmateId: 38433, bookingId: '2015-037422', offenderId: '39268', firstName: 'Kendall', lastName: 'Patterson',  },
			{ inmateId: 38293, bookingId: '2015-037282', offenderId: '39108', firstName: 'Arjun', lastName: 'Davenport',  },
			{ inmateId: 38294, bookingId: '2015-037283', offenderId: '39110', firstName: 'Rhett', lastName: 'Barrett',  },
			{ inmateId: 38354, bookingId: '2015-037343', offenderId: '39168', firstName: 'Ryan', lastName: 'Hughes',  },
			{ inmateId: 38373, bookingId: '2015-037362', offenderId: '39188', firstName: 'Lorenzo', lastName: 'Palmer',  },
			{ inmateId: 38415, bookingId: '2015-037404', offenderId: '39248', firstName: 'Maxwell', lastName: 'Larsen',  },
			{ inmateId: 38414, bookingId: '2015-037403', offenderId: '5671', firstName: 'Houston', lastName: 'Long',  },
			{ inmateId: 38272, bookingId: '2015-037262', offenderId: '13179', firstName: 'Uriel', lastName: 'Brandt',  },
			{ inmateId: 38333, bookingId: '2015-037322', offenderId: '39148', firstName: 'Matteo', lastName: 'Watson',  },
		],
	},
	{ locationId: 7110, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SHU-UPPER EAST', parentLocationId: 7099, operationalCapacity: 18, currentOccupancy: 10, livingUnit: true, housingUnitType: 'DISG SEG',  },
	{ locationId: 7120, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SHU-LOW WEST', parentLocationId: 7099, operationalCapacity: 18, currentOccupancy: 13, livingUnit: true, housingUnitType: 'DISG SEG',  },
	{ locationId: 7130, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SHU-UPPER WEST', parentLocationId: 7099, operationalCapacity: 22, currentOccupancy: 14, livingUnit: true, housingUnitType: 'DISG SEG',  },
	{ locationId: 6341, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-EAST 1', parentLocationId: 5001, operationalCapacity: 40, currentOccupancy: 49, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 11734, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-TMPA-TMPA', parentLocationId: 11733, operationalCapacity: 0, currentOccupancy: 9, livingUnit: true, housingUnitType: 'GEN',  },
	{ locationId: 7061, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-WEST-SOUTH 3', parentLocationId: 6921, operationalCapacity: 36, currentOccupancy: 31, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6651, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-FEDERAL 3', parentLocationId: 5001, operationalCapacity: 20, currentOccupancy: 18, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6662, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-FEDERAL 4', parentLocationId: 5001, operationalCapacity: 20, currentOccupancy: 20, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 7080, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-WEST-SOUTH 4', parentLocationId: 6921, operationalCapacity: 36, currentOccupancy: 27, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 7100, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SHU-LOW EAST', parentLocationId: 7099, operationalCapacity: 9, currentOccupancy: 8, livingUnit: true, housingUnitType: 'DISG SEG',  },
	{ locationId: 6999, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-WEST-NORTH 4', parentLocationId: 6921, operationalCapacity: 36, currentOccupancy: 30, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6362, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-EAST 2', parentLocationId: 5001, operationalCapacity: 40, currentOccupancy: 38, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6383, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-EAST 3', parentLocationId: 5001, operationalCapacity: 40, currentOccupancy: 39, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6624, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-FEDERAL 2', parentLocationId: 5001, operationalCapacity: 20, currentOccupancy: 20, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6600, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-EAST 5', parentLocationId: 6483, operationalCapacity: 40, currentOccupancy: 38, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6673, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-FEDERAL 5', parentLocationId: 5001, operationalCapacity: 20, currentOccupancy: 17, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6684, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-FEDERAL 1', parentLocationId: 6483, operationalCapacity: 4, currentOccupancy: 4, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6689, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-FEDERAL 2', parentLocationId: 6483, operationalCapacity: 20, currentOccupancy: 18, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6421, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-EAST 4', parentLocationId: 5001, operationalCapacity: 40, currentOccupancy: 40, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6442, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-EAST 5', parentLocationId: 5001, operationalCapacity: 40, currentOccupancy: 38, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6484, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-EAST 1', parentLocationId: 6483, operationalCapacity: 40, currentOccupancy: 35, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6505, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-EAST 2', parentLocationId: 6483, operationalCapacity: 40, currentOccupancy: 26, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6526, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-EAST 3', parentLocationId: 6483, operationalCapacity: 40, currentOccupancy: 39, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6547, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-EAST 4', parentLocationId: 6483, operationalCapacity: 40, currentOccupancy: 37, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6700, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-FEDERAL 3', parentLocationId: 6483, operationalCapacity: 20, currentOccupancy: 18, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6711, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-FEDERAL 4', parentLocationId: 6483, operationalCapacity: 20, currentOccupancy: 15, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6722, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-SOUTH-FEDERAL 5', parentLocationId: 6483, operationalCapacity: 20, currentOccupancy: 13, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6482, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-NORTH-FEDERAL 1', parentLocationId: 5001, operationalCapacity: 8, currentOccupancy: 9, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6947, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-WEST-NORTH 1', parentLocationId: 6921, operationalCapacity: 25, currentOccupancy: 22, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6961, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-WEST-NORTH 2', parentLocationId: 6921, operationalCapacity: 36, currentOccupancy: 30, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 6980, agencyId: 'SDP', locationType: 'TIER', description: 'SDP-WEST-NORTH 3', parentLocationId: 6921, operationalCapacity: 36, currentOccupancy: 29, livingUnit: true, housingUnitType: 'GP',  },
	{ locationId: 10148, agencyId: 'SDP', locationType: 'VISIT', description: 'SDP-VISIT_ROOM_HOLIDAY', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 10151, agencyId: 'SDP', locationType: 'VISIT', description: 'SDP-CLASS_II_HOLIDAY', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 11562, agencyId: 'SDP', locationType: 'VISIT', description: 'SDP-CONV1', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8381, agencyId: 'SDP', locationType: 'VISIT', description: 'SDP-CLASS_II_VISIT', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8207, agencyId: 'SDP', locationType: 'VISIT', description: 'SDP-VISIT_ROOM', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8203, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-KITCHEN', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8209, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-LAUNDRY', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8281, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-LICENSE_PLATES', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8282, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-BRAILLE', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8283, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-CARPENTRY', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8284, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-PRINT_SHOP', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8285, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-DOT_SIGN_SHOP', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8286, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-MACHINE_SHOP', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8287, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-WHEELCHAIR', currentOccupancy: 0, livingUnit: false,  },
	{ locationId: 8288, agencyId: 'SDP', locationType: 'WORK', description: 'SDP-MCI', currentOccupancy: 0, livingUnit: false,  },
];
