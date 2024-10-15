import { z } from 'zod';

// export type SpaceMarine = {
//   id: number; // Auto-generated and unique
//   name: string; // Non-null and not empty
//   coordinates: Coordinates; // Non-null
//   creationDate: Date; // Auto-generated
//   chapter?: Chapter; // Optional
//   health: number; // Must be greater than 0
//   category?: AstartesCategory; // Optional
//   weaponType: Weapon; // Non-null
//   meleeWeapon: MeleeWeapon; // Non-null
// };

// export type Coordinates = {
//   x: number; // Must be greater than -714, non-null
//   y: number;
// };

// export type Chapter = {
//   name: string; // Non-null, not empty
//   world?: string; // Optional
// };

// type AstartesCategory = 'DREADNOUGHT' | 'TACTICAL' | 'CHAPLAIN';

// type Weapon = 'BOLT_RIFLE' | 'PLASMA_GUN' | 'GRAV_GUN' | 'INFERNO_PISTOL' | 'HEAVY_FLAMER';

// type MeleeWeapon = 'MANREAPER' | 'POWER_BLADE' | 'POWER_FIST';

// export type User = {
//   id: number;
//   username: string;
//   password: string; // Consider handling password securely
//   role: UserRole;
// };

const AstartesCategory = z.union([
  z.literal('DREADNOUGHT'),
  z.literal('TACTICAL'),
  z.literal('CHAPLAIN')
]);

export const Categories: z.infer<typeof AstartesCategory>[] = [
  'DREADNOUGHT',
  'TACTICAL',
  'CHAPLAIN'
];

const Weapon = z.union([
  z.literal('BOLT_RIFLE'),
  z.literal('PLASMA_GUN'),
  z.literal('GRAV_GUN'),
  z.literal('INFERNO_PISTOL'),
  z.literal('HEAVY_FLAMER')
]);

export const Weapons: z.infer<typeof Weapon>[] = [
  'BOLT_RIFLE',
  'PLASMA_GUN',
  'GRAV_GUN',
  'INFERNO_PISTOL',
  'HEAVY_FLAMER'
];

const MeleeWeapon = z.union([
  z.literal('MANREAPER'),
  z.literal('POWER_BLADE'),
  z.literal('POWER_FIST')
]);

export const MeleeWeapons: z.infer<typeof MeleeWeapon>[] = [
  'MANREAPER',
  'POWER_BLADE',
  'POWER_FIST'
];

const Coordinates = z.object({
  x: z.number().min(-713, { message: 'X coordinate must be greater than -714' }),
  y: z.number()
});

const Chapter = z.object({
  name: z.string().min(1, { message: 'Chapter name cannot be empty' }),
  world: z.string().optional()
});

export const SpaceMarineScheme = z.object({
  id: z.number().positive({ message: 'ID must be a positive number' }),
  name: z.string().min(1, { message: 'Name cannot be empty' }),
  coordinates: Coordinates,
  creationDate: z.union([z.date(), z.string()]),
  chapter: Chapter.optional(),
  health: z.number().positive({ message: 'Health must be greater than 0' }),
  category: AstartesCategory.optional(),
  weaponType: Weapon,
  meleeWeapon: MeleeWeapon
});

export type SpaceMarine = z.infer<typeof SpaceMarineScheme>;

// type UserRole = 'GUEST' | 'USER' | 'ADMIN';
