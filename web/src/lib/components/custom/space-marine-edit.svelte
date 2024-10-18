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
  import {
    ProductSchema,
    UnitOfMeasure,
    OrganizationType,
    Color,
    Country,
    type OrganizationSchema,
    type PersonSchema
  } from '$lib/types';
  import BaseLabel from '$lib/components/ui/label/label.svelte';
  import moment from 'moment';
  import * as Select from '../ui/select';
  import _ from 'lodash';
  import NumberInput from '../ui/input/number-input.svelte';
  export let data: Infer<typeof ProductSchema>;
  import * as Dialog from '$lib/components/ui/dialog';

  export const onSubmit: (data: Infer<typeof ProductSchema>) => any = (data) => {
    toast.success(`You submitted ${JSON.stringify(data, null, 2)}`);
  };

  const form = superForm(data, {
    SPA: true,
    dataType: 'json',
    validators: zodClient(ProductSchema),
    onUpdated: ({ form: f }) => {
      if (f.valid) {
        $formData = f.data;
        onSubmit(data);
      } else {
        toast.error('Please fix the errors in the form.');
      }
    },
    onSubmit(input) {},

    clearOnSubmit: 'errors',
    multipleSubmits: 'prevent'
  });
  const { form: formData, enhance } = form;

  $: selectedUnitOfMeasure = $formData.unitOfMeasure
    ? {
        label: _.startCase(_.toLower($formData.unitOfMeasure)),
        value: $formData.unitOfMeasure
      }
    : undefined;

  $: selectedOrganizationType = $formData.manufacturer?.type
    ? {
        label: _.startCase(_.toLower($formData.manufacturer.type)),
        value: $formData.manufacturer.type
      }
    : undefined;

  $: selectedEyeColor = $formData.owner.eyeColor
    ? {
        label: _.startCase(_.toLower($formData.owner.eyeColor)),
        value: $formData.owner.eyeColor
      }
    : undefined;

  $: selectedHairColor = $formData.owner.hairColor
    ? {
        label: _.startCase(_.toLower($formData.owner.hairColor)),
        value: $formData.owner.hairColor
      }
    : undefined;

  $: selectedNationality = $formData.owner.nationality
    ? {
        label: _.startCase(_.toLower($formData.owner.nationality)),
        value: $formData.owner.nationality
      }
    : undefined;
</script>

<div class="grid h-full w-full place-items-center items-center justify-items-center">
  <form use:enhance on:submit|preventDefault|stopPropagation class="w-full space-y-6">
    <Card.Header>
      <Card.Title>
        <slot name="title">Edit Product</slot>
      </Card.Title>
    </Card.Header>
    <Card.Content class="w-full">
      <div class="grid w-full grid-cols-3 gap-10">
        <!-- Product Name -->
        <div>
          <Field {form} name="name">
            <Control let:attrs>
              <Label>Name</Label>
              <Input {...attrs} bind:value={$formData.name} />
            </Control>
            <FieldErrors />
          </Field>

          <!-- Coordinates -->
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

          <!-- Price -->
          <Field {form} name="price">
            <Control let:attrs>
              <Label>Price</Label>
              <NumberInput {...attrs} bind:value={$formData.price} />
            </Control>
            <FieldErrors />
          </Field>

          <!-- Manufacture Cost -->
          <Field {form} name="manufactureCost">
            <Control let:attrs>
              <Label>Manufacture Cost</Label>
              <NumberInput {...attrs} bind:value={$formData.manufactureCost} />
            </Control>
            <FieldErrors />
          </Field>

          <!-- Rating -->
          <Field {form} name="rating">
            <Control let:attrs>
              <Label>Rating</Label>
              <NumberInput {...attrs} bind:value={$formData.rating} />
            </Control>
            <FieldErrors />
          </Field>

          <!-- Unit of Measure -->
          <Field {form} name="unitOfMeasure">
            <Control let:attrs>
              <Label>Unit of Measure</Label>
              <Select.Root
                portal={null}
                selected={selectedUnitOfMeasure}
                onSelectedChange={(v) => {
                  v && ($formData.unitOfMeasure = v.value);
                }}
              >
                <Select.Trigger>
                  <Select.Value placeholder="Select Unit of Measure" />
                </Select.Trigger>
                <Select.Content>
                  <Select.Group>
                    <Select.Label>Units</Select.Label>
                    {#each UnitOfMeasure.options as unit}
                      {@const label = _.startCase(_.toLower(unit))}
                      <Select.Item value={unit} {label}>
                        {label}
                      </Select.Item>
                    {/each}
                  </Select.Group>
                </Select.Content>
              </Select.Root>
            </Control>
            <FieldErrors />
          </Field>
        </div>

        <!-- Manufacturer -->
        <div>
          <BaseLabel>Manufacturer</BaseLabel>
          {#if $formData.manufacturer}
            <!-- Manufacturer Fields -->
            <Field {form} name="manufacturer.name">
              <Control let:attrs>
                <Label>Name</Label>
                <Input {...attrs} bind:value={$formData.manufacturer.name} />
              </Control>
              <FieldErrors />
            </Field>
            <Field {form} name="manufacturer.employeesCount">
              <Control let:attrs>
                <Label>Employees Count</Label>
                <NumberInput {...attrs} bind:value={$formData.manufacturer.employeesCount} />
              </Control>
              <FieldErrors />
            </Field>
            <Field {form} name="manufacturer.rating">
              <Control let:attrs>
                <Label>Rating</Label>
                <NumberInput {...attrs} bind:value={$formData.manufacturer.rating} />
              </Control>
              <FieldErrors />
            </Field>
            <!-- Organization Type -->
            <Field {form} name="manufacturer.type">
              <Control let:attrs>
                <Label>Organization Type</Label>
                <Select.Root
                  portal={null}
                  selected={selectedOrganizationType}
                  onSelectedChange={(v) => {
                    v && $formData.manufacturer && ($formData.manufacturer.type = v.value);
                  }}
                >
                  <Select.Trigger>
                    <Select.Value placeholder="Select Organization Type" />
                  </Select.Trigger>
                  <Select.Content>
                    <Select.Group>
                      <Select.Label>Types</Select.Label>
                      {#each OrganizationType.options as type}
                        {@const label = _.startCase(_.toLower(type))}
                        <Select.Item value={type} {label}>
                          {label}
                        </Select.Item>
                      {/each}
                    </Select.Group>
                  </Select.Content>
                </Select.Root>
              </Control>
              <FieldErrors />
            </Field>
            <!-- Remove Manufacturer Button -->
            <Button on:click={() => ($formData.manufacturer = undefined)}>
              Remove Manufacturer
            </Button>
          {:else}
            <!-- Add Manufacturer Button -->
            <Button
              on:click={() =>
                ($formData.manufacturer = {
                  name: '',
                  employeesCount: 0,
                  rating: 0,
                  type: undefined,
                  id: undefined,
                  annualTurnover: undefined,
                  officialAddress: undefined
                })}
            >
              Add Manufacturer
            </Button>
          {/if}
        </div>

        <!-- Owner -->
        <div>
          <BaseLabel>Owner</BaseLabel>
          <Field {form} name="owner.name">
            <Control let:attrs>
              <Label>Name</Label>
              <Input {...attrs} bind:value={$formData.owner.name} />
            </Control>
            <FieldErrors />
          </Field>

          <!-- Owner Eye Color -->
          <Field {form} name="owner.eyeColor">
            <Control let:attrs>
              <Label>Eye Color</Label>
              <Select.Root
                portal={null}
                selected={selectedEyeColor}
                onSelectedChange={(v) => {
                  v && ($formData.owner.eyeColor = v.value);
                }}
              >
                <Select.Trigger>
                  <Select.Value placeholder="Select Eye Color" />
                </Select.Trigger>
                <Select.Content>
                  <Select.Group>
                    <Select.Label>Colors</Select.Label>
                    {#each Color.options as color}
                      {@const label = _.startCase(_.toLower(color))}
                      <Select.Item value={color} {label}>
                        {label}
                      </Select.Item>
                    {/each}
                  </Select.Group>
                </Select.Content>
              </Select.Root>
            </Control>
            <FieldErrors />
          </Field>
          <!-- Owner Hair Color -->
          <Field {form} name="owner.hairColor">
            <Control let:attrs>
              <Label>Hair Color</Label>
              <Select.Root
                portal={null}
                selected={selectedHairColor}
                onSelectedChange={(v) => {
                  v && ($formData.owner.hairColor = v.value);
                }}
              >
                <Select.Trigger>
                  <Select.Value placeholder="Select Hair Color" />
                </Select.Trigger>
                <Select.Content>
                  <Select.Group>
                    <Select.Label>Colors</Select.Label>
                    {#each Color.options as color}
                      {@const label = _.startCase(_.toLower(color))}
                      <Select.Item value={color} {label}>
                        {label}
                      </Select.Item>
                    {/each}
                  </Select.Group>
                </Select.Content>
              </Select.Root>
            </Control>
            <FieldErrors />
          </Field>
          <!-- Owner Nationality -->
          <Field {form} name="owner.nationality">
            <Control let:attrs>
              <Label>Nationality</Label>
              <Select.Root
                portal={null}
                selected={selectedNationality}
                onSelectedChange={(v) => {
                  v && ($formData.owner.nationality = v.value);
                }}
              >
                <Select.Trigger>
                  <Select.Value placeholder="Select Nationality" />
                </Select.Trigger>
                <Select.Content>
                  <Select.Group>
                    <Select.Label>Countries</Select.Label>
                    {#each Country.options as country}
                      {@const label = _.startCase(_.toLower(country))}
                      <Select.Item value={country} {label}>
                        {label}
                      </Select.Item>
                    {/each}
                  </Select.Group>
                </Select.Content>
              </Select.Root>
            </Control>
            <FieldErrors />
          </Field>
          <!-- Owner Birthday -->
          <Field {form} name="owner.birthday">
            <Control let:attrs>
              <Label>Birthday</Label>
              <Input type="date" {...attrs} bind:value={$formData.owner.birthday} />
            </Control>
            <FieldErrors />
          </Field>
          <!-- Owner Location -->
          <BaseLabel>Owner Location</BaseLabel>
          <div class="gap-10px grid grid-cols-3">
            <Field {form} name="owner.location.x">
              <Control let:attrs>
                <Label>x</Label>
                <NumberInput {...attrs} bind:value={$formData.owner.location.x} />
              </Control>
              <FieldErrors />
            </Field>
            <Field {form} name="owner.location.y">
              <Control let:attrs>
                <Label>y</Label>
                <NumberInput {...attrs} bind:value={$formData.owner.location.y} />
              </Control>
              <FieldErrors />
            </Field>
            <Field {form} name="owner.location.z">
              <Control let:attrs>
                <Label>z</Label>
                <NumberInput {...attrs} bind:value={$formData.owner.location.z} />
              </Control>
              <FieldErrors />
            </Field>
          </div>

          <!-- Creation Date -->
          <BaseLabel>
            Created {moment($formData.creationDate).fromNow()}
          </BaseLabel>
        </div>
      </div>
    </Card.Content>
    <Card.Footer>
      <div class="flex w-full">
        <Button class="ml-auto" type="submit">
          <slot name="button">Update</slot>
        </Button>
      </div>
    </Card.Footer>
  </form>
</div>
<!-- <SuperDebug data={$formData} /> -->
