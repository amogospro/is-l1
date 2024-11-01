<script lang="ts">
  import api from '$lib/api';
  import Button from '$lib/components/ui/button/button.svelte';
  import * as Card from '$lib/components/ui/card';
  import NumberInput from '$lib/components/ui/input/number-input.svelte';
  import * as Select from '$lib/components/ui/select/index.js';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';

  let value: any;

  const increase = async () => {
    if (value == null) {
      toast.error('Please select price');
      return;
    }
    const { data } = await api.get(`/products/increase-price?percentage=${value}`);
    toast.info('Price increased');
  };
</script>

<Card.Root class="w-full">
  <Card.Header>
    <Card.Title>Increase price</Card.Title>
    <Card.Description>
      Increase the price of all products by the specified percentage.
    </Card.Description>
  </Card.Header>
  <Card.Content>
    <div class="gap-10px grid w-full grid-cols-2">
      <NumberInput bind:value></NumberInput>
      <Button on:click={() => increase()}>Increase</Button>
    </div>
  </Card.Content>
</Card.Root>
