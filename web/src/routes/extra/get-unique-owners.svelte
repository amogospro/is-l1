<script lang="ts">
  import api from '$lib/api';
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';
  import type { Person } from '$lib/types';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';

  let uniqueOwners: Person[] = [];

  const getUniqueOwners = async () => {
    const { data } = await api.get('/products/unique-owners');
    uniqueOwners = data;
  };
</script>

<!-- Рассчитать среднее значение поля health для всех объектов. -->
<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>Unique owners</Card.Title>
    <Card.Description
      >Return an array of unique values of the owner field for all objects.</Card.Description
    >
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <div class="grid items-center justify-items-center">
        <div class="flex flex-wrap gap-[10px]">
          {#each uniqueOwners as person}
            <p>
              {person.name}
            </p>
          {/each}
        </div>
      </div>
      <Button on:click={() => getUniqueOwners()}>Get</Button>
    </div>
  </Card.Content>
</Card.Root>
