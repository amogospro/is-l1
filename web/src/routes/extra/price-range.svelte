<script lang="ts">
  import api from '$lib/api';
  import DataTable from '$lib/components/custom/table/data-table.svelte';
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';
  import NumberInput from '$lib/components/ui/input/number-input.svelte';
  import Label from '$lib/components/ui/label/label.svelte';

  import * as Select from '$lib/components/ui/select/index.js';
  import { type Product } from '$lib/types';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';
  import { writable } from 'svelte/store';

  let min: any;
  let max: any;

  let products = writable<Product[]>([]);
  const getProducts = async () => {
    if (min == null) {
      toast.error('Please select min price');
      return;
    }
    if (max == null) {
      toast.error('Please select max price');
      return;
    }
    const { data } = await api.get(`/products/price-range?minPrice=${min}&maxPrice=${max}`);
    products.set(data);
  };
</script>

<!-- Вернуть массив объектов, значение поля meleeWeapon которых меньше заданного. -->

<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>PriceRange</Card.Title>
    <Card.Description>
      Select all products that fall within the specified price range.
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-1">
      <div class="gap-10px flex w-full items-center">
        <Label>Min</Label>
        <NumberInput bind:value={min}></NumberInput>
        <Label>Max</Label>
        <NumberInput bind:value={max}></NumberInput>
      </div>
      <Button on:click={() => getProducts()}>Get Products</Button>
    </div>
  </Card.Content>
  {#if $products.length}
    <Card.Footer>
      <div class="w-full">
        <DataTable {products} readonly />
      </div>
    </Card.Footer>
  {/if}
</Card.Root>
