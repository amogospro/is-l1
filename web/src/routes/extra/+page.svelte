<script lang="ts">
  import { toast } from 'svelte-sonner';
  import GetAverageRating from './get-average-rating.svelte';
  import GetUniqueOwners from './get-unique-owners.svelte';
  import GetRatingAbove from './rating-above.svelte';
  import RemoveByMelee from './remove-by-melee.svelte';
  import PriceRange from './price-range.svelte';
</script>

<div class="grid grid-cols-4 gap-10">
  <RemoveByMelee />
  <GetAverageRating />
  <div class="col-span-2">
    <GetUniqueOwners />
  </div>
  <div class="col-span-2">
    <GetRatingAbove />
  </div>
  <div class="col-span-2">
    <PriceRange />
  </div>
</div>

<!-- Добавить десантника в указанный орден. -->

<!-- <Card.Root class="w-full max-w-[500px]">
  <Card.Header>
    <Card.Title>Remove Space Marine by Melee Weapon</Card.Title>
    <Card.Description>
      Delete one (any) object whose Melee Weapon field value is equivalent to the specified one
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <Select.Root portal={null} bind:selected={melee_weapon}>
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
      <Button on:click={() => removeMeleeWeapon(melee_weapon?.value)}>Remove</Button>
    </div>
  </Card.Content>
  <Card.Footer></Card.Footer>
</Card.Root> -->

<!-- Распустить указанный орден космодесанта. -->

<!-- <Card.Root class="w-full max-w-[500px]">
  <Card.Header>
    <Card.Title>Remove Space Marine by Melee Weapon</Card.Title>
    <Card.Description>
      Delete one (any) object whose Melee Weapon field value is equivalent to the specified one
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <Select.Root portal={null} bind:selected={melee_weapon}>
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
      <Button on:click={() => removeMeleeWeapon(melee_weapon?.value)}>Remove</Button>
    </div>
  </Card.Content>
  <Card.Footer></Card.Footer>
</Card.Root> -->
