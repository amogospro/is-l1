import { z } from 'zod';

// Enums
export const UnitOfMeasure = z.enum(['METERS', 'MILLILITERS', 'GRAMS', 'MILLIGRAMS']);
export const OrganizationType = z.enum([
  'COMMERCIAL',
  'PUBLIC',
  'TRUST',
  'PRIVATE_LIMITED_COMPANY',
  'OPEN_JOINT_STOCK_COMPANY'
]);
export const Color = z.enum(['GREEN', 'BLUE', 'YELLOW', 'ORANGE', 'WHITE']);
export const Country = z.enum(['USA', 'FRANCE', 'THAILAND']);

// Schemas
export const LocationSchema = z.object({
  x: z.number().int(),
  y: z.number(),
  z: z.number()
});

export const AddressSchema = z.object({
  zipCode: z.string().optional().nullable(), // Can be null
  town: LocationSchema.optional().nullable() // Can be null
});

export const CoordinatesSchema = z.object({
  x: z.number().int().max(864), // Max value: 864
  y: z.number().int() // Cannot be null
});

export const OrganizationSchema = z.object({
  id: z.number().int().positive().optional(), // Auto-generated, cannot be null, value > 0
  name: z.string().min(1), // Cannot be null, cannot be empty
  officialAddress: AddressSchema.optional().nullable(), // Can be null
  annualTurnover: z.number().positive().optional().nullable(), // Can be null, value > 0
  employeesCount: z.number().int().positive(), // Value > 0
  rating: z.number().int().positive(), // Value > 0
  type: OrganizationType.optional() // Can be null
});
export type Organization = z.infer<typeof OrganizationSchema>;

export const PersonSchema = z.object({
  id: z.number().int().positive().optional(), // Auto-generated, cannot be null, value > 0
  name: z.string().min(1), // Cannot be null, cannot be empty
  eyeColor: Color, // Cannot be null
  hairColor: Color, // Cannot be null
  location: LocationSchema, // Cannot be null
  birthday: z.coerce.date(), // Cannot be null
  nationality: Country // Cannot be null
});
export type Person = z.infer<typeof PersonSchema>;

export const ProductSchema = z.object({
  id: z.number().int().positive().optional(), // Auto-generated, value > 0
  name: z.string().min(1), // Cannot be null, cannot be empty
  coordinates: CoordinatesSchema, // Cannot be null
  creationDate: z.coerce.date(), // Auto-generated, cannot be null
  unitOfMeasure: UnitOfMeasure.optional(), // Can be null
  manufacturer: OrganizationSchema.optional(), // Can be null
  price: z.number().positive(), // Value > 0
  manufactureCost: z.number(), // No constraints specified
  rating: z.number().int().positive().optional().nullable(), // Can be null, value > 0
  owner: PersonSchema // Cannot be null
});
export type Product = z.infer<typeof ProductSchema>;

export const ProductEditSchema = z.object({
  id: z.number().int().positive().optional(), // Auto-generated, value > 0
  name: z.string().min(1), // Cannot be null, cannot be empty
  coordinates: CoordinatesSchema, // Cannot be null
  creationDate: z.coerce.date(), // Auto-generated, cannot be null
  unitOfMeasure: UnitOfMeasure.optional(), // Can be null
  manufacturer: z.union([z.object({ id: z.number().optional() }), OrganizationSchema]).nullable(), // Can be null
  price: z.number().positive(), // Value > 0
  manufactureCost: z.number(), // No constraints specified
  rating: z.number().int().positive().optional().nullable(), // Can be null, value > 0
  owner: z.union([z.object({ id: z.number().optional() }), PersonSchema]) // Cannot be null
});
export type ProductEdit = z.infer<typeof ProductEditSchema>;

// type UserRole = 'GUEST' | 'USER' | 'ADMIN';
