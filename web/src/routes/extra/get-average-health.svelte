<script lang="ts">
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';

  let averageHealth = 2;

  const getAverageHealth = async () => {
    toast.info('will getAverageHealth ');
  };
</script>

<!-- Рассчитать среднее значение поля health для всех объектов. -->
<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>Average health</Card.Title>
    <Card.Description>
      Calculate the average value of the health field for all objects
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <Button on:click={() => getAverageHealth()}>Calculate</Button>
      <div class="grid items-center justify-items-center">
        <p>
          {averageHealth} hp
        </p>
      </div>
    </div>
  </Card.Content>
  <Card.Footer></Card.Footer>
</Card.Root>
