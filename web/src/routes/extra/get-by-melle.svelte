<script lang="ts">
  import DataTable from '$lib/components/custom/table/data-table.svelte';
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';

  import * as Select from '$lib/components/ui/select/index.js';
  import { MeleeWeapons } from '$lib/types';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';

  let melee_weapon_lower: any;

  const getSpaceMarines = async (melee_weapon: string) => {
    toast.info('will getSpaceMarines ');
  };
</script>

<!-- Вернуть массив объектов, значение поля meleeWeapon которых меньше заданного. -->

<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>Get Space Marines with lower Melee Weapon</Card.Title>
    <Card.Description>
      Return an array of objects whose melee Weapon field value is less than the specified value.
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <Select.Root portal={null} bind:selected={melee_weapon_lower}>
        <Select.Trigger>
          <Select.Value placeholder="Select Melee Weapon" />
        </Select.Trigger>
        <Select.Content>
          <Select.Group>
            <Select.Label>Melee Weapons</Select.Label>
            {#each MeleeWeapons as melee_weapon}
              {@const label = _.startCase(_.toLower(melee_weapon))}
              <Select.Item value={melee_weapon} {label}>
                {label}
              </Select.Item>
            {/each}
          </Select.Group>
        </Select.Content>
        <Select.Input name="favoriteFruit" />
      </Select.Root>
      <Button on:click={() => getSpaceMarines(melee_weapon_lower?.value)}>Get Space Marines</Button>
    </div>
  </Card.Content>
  <Card.Footer>
    <div class="w-full">
      <DataTable />
    </div>
  </Card.Footer>
</Card.Root>
