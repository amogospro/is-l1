import type { Product } from './types';

export const products: Product[] = [
  {
    id: 1,
    name: 'Super Widget',
    coordinates: {
      x: 500, // Within the maximum value of 864
      y: 300,
    },
    creationDate: new Date(), // Auto-generated
    unitOfMeasure: 'METERS',
    manufacturer: {
      id: 1,
      name: 'Acme Corporation',
      officialAddress: {
        zipCode: '12345',
        town: {
          x: 10,
          y: 20,
          z: 30,
        },
      },
      annualTurnover: 1_000_000, // Value > 0
      employeesCount: 250, // Value > 0
      rating: 5, // Value > 0
      type: 'COMMERCIAL',
    },
    price: 150, // Value > 0
    manufactureCost: 75,
    rating: 4, // Value > 0
    owner: {
      name: 'Alice Johnson',
      eyeColor: 'BLUE',
      hairColor: 'YELLOW', // Valid enum value
      location: {
        x: 5,
        y: 10,
        z: 15,
      },
      birthday: new Date('1990-01-01'),
      nationality: 'USA',
    },
  },
  {
    id: 2,
    name: 'Mega Gadget',
    coordinates: {
      x: 800,
      y: 600,
    },
    creationDate: new Date(),
    unitOfMeasure: 'GRAMS',
    manufacturer: {
      id: 2,
      name: 'Globex Industries',
      officialAddress: {
        zipCode: '67890',
        town: {
          x: 40,
          y: 50,
          z: 60,
        },
      },
      annualTurnover: 2_500_000,
      employeesCount: 500,
      rating: 8,
      type: 'PUBLIC',
    },
    price: 250,
    manufactureCost: 125,
    rating: 5,
    owner: {
      name: 'Bob Smith',
      eyeColor: 'GREEN',
      hairColor: 'ORANGE',
      location: {
        x: 15,
        y: 25,
        z: 35,
      },
      birthday: new Date('1985-05-15'),
      nationality: 'FRANCE',
    },
  },
  {
    id: 3,
    name: 'Ultra Tool',
    coordinates: {
      x: 300,
      y: 450,
    },
    creationDate: new Date(),
    unitOfMeasure: 'MILLILITERS',
    manufacturer: {
      id: 3,
      name: 'Initech',
      officialAddress: {
        zipCode: '54321',
        town: {
          x: 70,
          y: 80,
          z: 90,
        },
      },
      annualTurnover: 3_750_000,
      employeesCount: 750,
      rating: 9,
      type: 'TRUST',
    },
    price: 350,
    manufactureCost: 175,
    rating: 6,
    owner: {
      name: 'Carol Williams',
      eyeColor: 'YELLOW',
      hairColor: 'WHITE',
      location: {
        x: 25,
        y: 35,
        z: 45,
      },
      birthday: new Date('1995-10-20'),
      nationality: 'THAILAND',
    },
  },
];
