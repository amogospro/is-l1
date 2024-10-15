<script lang="ts">
  import { superForm } from 'sveltekit-superforms';
  import SuperDebug from 'sveltekit-superforms';
  import { Field, Control, Label, FieldErrors } from '$lib/components/ui/form';
  import { Input } from '$lib/components/ui/input';
  import * as Card from '$lib/components/ui/card';
  import { z } from 'zod';
  import { zodClient } from 'sveltekit-superforms/adapters';
  import { Button } from '$lib/components/ui/button';
  import { toast } from 'svelte-sonner';
  import { type Infer } from 'sveltekit-superforms/client';
  import { Link } from '$lib/components/ui/link';
  import { Categories, MeleeWeapons, SpaceMarineScheme, Weapons } from '$lib/types';
  import BaseLabel from '$lib/components/ui/label/label.svelte';
  import moment from 'moment';
  import * as Select from '../ui/select';
  import _ from 'lodash';
  import NumberInput from '../ui/input/number-input.svelte';
  export let data: Infer<typeof SpaceMarineScheme>;

  const form = superForm(data, {
    SPA: true,
    dataType: 'json',
    validators: zodClient(SpaceMarineScheme),
    onUpdated: ({ form: f }) => {
      if (f.valid) {
        $formData = f.data;
        toast.success(`You submitted ${JSON.stringify(f.data, null, 2)}`);
      } else {
        toast.error('Please fix the errors in the form.');
      }
    },
    onSubmit(input) {},

    clearOnSubmit: 'errors',
    multipleSubmits: 'prevent'
  });
  const { form: formData, enhance } = form;

  $: selectedCategory = $formData.category
    ? {
        label: _.startCase(_.toLower($formData.category)),
        value: $formData.category
      }
    : undefined;

  $: selectedWeaponType = $formData.weaponType
    ? {
        label: _.startCase(_.toLower($formData.weaponType)),
        value: $formData.weaponType
      }
    : undefined;

  $: selectedMeleeWeapon = $formData.meleeWeapon
    ? {
        label: _.startCase(_.toLower($formData.meleeWeapon)),
        value: $formData.meleeWeapon
      }
    : undefined;
</script>

<div class="grid h-full w-full place-items-center items-center justify-items-center">
  <form use:enhance on:submit|preventDefault|stopPropagation class="w-full space-y-6">
    <!-- <Card.Root class="w-full"> -->
    <Card.Header>
      <Card.Title>Edit Space Marine</Card.Title>
    </Card.Header>
    <Card.Content class="w-full">
      <div class="grid w-full grid-cols-2 gap-10">
        <div>
          <img src="/space-marine.png" alt="space marine" />
        </div>
        <div>
          <Field {form} name="name">
            <Control let:attrs>
              <Label>Name</Label>
              <Input {...attrs} bind:value={$formData.name} />
            </Control>
            <FieldErrors />
          </Field>

          <BaseLabel>Coordinates</BaseLabel>
          <div class="gap-10px grid grid-cols-2">
            <Field {form} name="coordinates.x">
              <Control let:attrs>
                <Label>x</Label>
                <NumberInput {...attrs} bind:value={$formData.coordinates.x} />
              </Control>
              <FieldErrors />
            </Field>
            <Field {form} name="coordinates.y">
              <Control let:attrs>
                <Label>y</Label>
                <NumberInput {...attrs} bind:value={$formData.coordinates.y} />
              </Control>
              <FieldErrors />
            </Field>
          </div>

          <BaseLabel>Chapter</BaseLabel>
          {#if $formData.chapter}
            <div class="gap-10px grid grid-cols-2">
              <Field {form} name="chapter.name">
                <Control let:attrs>
                  <Label>Name</Label>
                  {#if $formData.chapter}
                    <Input {...attrs} bind:value={$formData.chapter.name} />
                  {/if}
                </Control>
                <FieldErrors />
              </Field>
              <Field {form} name="chapter.world">
                <Control let:attrs>
                  <Label>World</Label>
                  {#if $formData.chapter}
                    <Input {...attrs} bind:value={$formData.chapter.world} />
                  {/if}
                </Control>
                <FieldErrors />
              </Field>
            </div>
          {:else}
            <Button on:click={() => ($formData.chapter = { name: '', world: '' })}>
              Enable chapter
            </Button>
          {/if}

          <Field {form} name="health">
            <Control let:attrs>
              <Label>Health</Label>
              <NumberInput {...attrs} bind:value={$formData.health} />
            </Control>
            <FieldErrors />
          </Field>

          <Field {form} name="category">
            <Control let:attrs>
              <Label>Category</Label>

              <Select.Root
                portal={null}
                selected={selectedCategory}
                onSelectedChange={(v) => {
                  // @ts-ignore
                  v && ($formData.category = v.value);
                }}
              >
                <Select.Trigger>
                  <Select.Value placeholder="Select Category" />
                </Select.Trigger>
                <Select.Content>
                  <Select.Group>
                    <Select.Label>Category</Select.Label>
                    {#each Categories as category}
                      {@const label = _.startCase(_.toLower(category))}
                      <Select.Item value={category} {label}>
                        {label}
                      </Select.Item>
                    {/each}
                  </Select.Group>
                </Select.Content>
              </Select.Root>
            </Control>
            <FieldErrors />
          </Field>

          <Field {form} name="weaponType">
            <Control let:attrs>
              <Label>Weapon</Label>

              <Select.Root
                portal={null}
                selected={selectedWeaponType}
                onSelectedChange={(v) => {
                  // @ts-ignore
                  v && ($formData.weaponType = v.value);
                }}
              >
                <Select.Trigger>
                  <Select.Value placeholder="Select Weapon" />
                </Select.Trigger>
                <Select.Content>
                  <Select.Group>
                    <Select.Label>Weapons</Select.Label>
                    {#each Weapons as weapon}
                      {@const label = _.startCase(_.toLower(weapon))}
                      <Select.Item value={weapon} {label}>
                        {label}
                      </Select.Item>
                    {/each}
                  </Select.Group>
                </Select.Content>
              </Select.Root>
            </Control>
            <FieldErrors />
          </Field>

          <Field {form} name="meleeWeapon">
            <Control let:attrs>
              <Label>Melee Weapon</Label>

              <Select.Root
                portal={null}
                selected={selectedMeleeWeapon}
                onSelectedChange={(v) => {
                  // @ts-ignore
                  v && ($formData.meleeWeapon = v.value);
                }}
              >
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
              </Select.Root>
            </Control>
            <FieldErrors />
          </Field>

          <BaseLabel>Created {moment($formData.creationDate).fromNow()}</BaseLabel>

          <!-- <Field {form} name="coordinates">
          <Field {form} name="coordinates">
            <Control let:attrs>
              <Label>name</Label>
              <Input {...attrs} bind:value={$formData.name} />
            </Control>
            <FieldErrors />
          </Field>
          <Control let:attrs>
            <Label>name</Label>
            <Input {...attrs} bind:value={$formData.name} />
          </Control>
          <FieldErrors />
        </Field> -->
        </div>
      </div>
    </Card.Content>
    <Card.Footer>
      <div class="flex w-full">
        <Button class="ml-auto" type="submit">Update</Button>
      </div>
    </Card.Footer>
    <!-- </Card.Root> -->
  </form>
</div>
<SuperDebug data={$formData} />
