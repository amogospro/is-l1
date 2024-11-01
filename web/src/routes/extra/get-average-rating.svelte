<script lang="ts">
  import api from '$lib/api';
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';

  let averageRating: string | null = null;

  const getAverageRating = async () => {
    const { data } = await api.get('/products/average-rating');
    averageRating = data;
  };
</script>

<!-- Рассчитать среднее значение поля health для всех объектов. -->
<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>Average rating</Card.Title>
    <Card.Description>Calculate the average rating of all objects</Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <div class="grid items-center justify-items-center">
        <p>
          {#if averageRating}
            {Math.round(Number(averageRating) * 100) / 100}
          {:else}{/if}
        </p>
      </div>
      <Button on:click={() => getAverageRating()}>Calculate</Button>
    </div>
  </Card.Content>
</Card.Root>
