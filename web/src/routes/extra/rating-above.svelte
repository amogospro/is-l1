<script lang="ts">
  import api from '$lib/api';
  import DataTable from '$lib/components/custom/table/data-table.svelte';
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';
  import NumberInput from '$lib/components/ui/input/number-input.svelte';

  import * as Select from '$lib/components/ui/select/index.js';
  import { type Product } from '$lib/types';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';
  import { writable } from 'svelte/store';

  let value: any;

  let products = writable<Product[]>([]);
  const getProducts = async () => {
    if (value == null) {
      toast.error('Please select rating');
      return;
    }
    const { data } = await api.get(`/products/rating-above?minRating=${value}`);
    products.set(data);
  };
</script>

<!-- Вернуть массив объектов, значение поля meleeWeapon которых меньше заданного. -->

<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>Get Products with with rating above given</Card.Title>
    <Card.Description>
      Return an array of objects whose rating field value is greater than the specified one.
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <NumberInput bind:value></NumberInput>
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
