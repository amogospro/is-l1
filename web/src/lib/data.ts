import type { SpaceMarineScheme } from './types';

export const spaceMarines: SpaceMarine[] = [
  {
    id: 1,
    name: 'Marine A',
    coordinates: { x: 150, y: 100 },
    creationDate: new Date(),
    health: 100,
    chapter: {
      name: 'sus',
      world: 'amogus'
    },
    weaponType: 'BOLT_RIFLE',
    meleeWeapon: 'POWER_FIST'
  },
  {
    id: 2,
    name: 'Marine B',
    coordinates: { x: 200, y: 200 },
    creationDate: new Date(),
    health: 150,
    category: 'DREADNOUGHT',
    weaponType: 'PLASMA_GUN',
    meleeWeapon: 'MANREAPER'
  },
  {
    id: 3,
    name: 'Marine C',
    coordinates: { x: 300, y: 300 },
    creationDate: new Date(),
    health: 120,
    weaponType: 'GRAV_GUN',
    meleeWeapon: 'POWER_BLADE'
  }
];
