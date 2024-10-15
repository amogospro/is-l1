<script lang="ts">
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';
  import * as Select from '$lib/components/ui/select/index.js';
  import { MeleeWeapons } from '$lib/types';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';

  let melee_weapon_remove: any;

  const removeMeleeWeapon = async (melee_weapon: string) => {
    console.log(melee_weapon);
    if (!melee_weapon) {
      toast.info('Please select Melee Weapon!');
      return;
    }
    toast.info('will remove ' + melee_weapon);
  };
</script>

<!-- Удалить один (любой) объект, значение поля meleeWeapon которого эквивалентно заданному. -->

<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>Remove Space Marine by Melee Weapon</Card.Title>
    <Card.Description>
      Delete one (any) object whose Melee Weapon field value is equivalent to the specified one
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <Select.Root portal={null} bind:selected={melee_weapon_remove}>
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
      <Button on:click={() => removeMeleeWeapon(melee_weapon_remove?.value)}>Remove</Button>
    </div>
  </Card.Content>
  <Card.Footer></Card.Footer>
</Card.Root>
