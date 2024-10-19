<script lang="ts">
  import Ellipsis from 'lucide-svelte/icons/ellipsis';
  import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
  import { Button } from '$lib/components/ui/button';
  import * as Dialog from '$lib/components/ui/dialog';
  import SpaceMarineEdit from '../product-form.svelte';
  import type { Product } from '$lib/types';
  import Icon from 'svelte-radix/Pencil1.svelte';

  export let data: Product;

  export let id: string;
</script>

<!-- <DropdownMenu.Root>
  <DropdownMenu.Trigger asChild let:builder>
    <Button variant="ghost" builders={[builder]} size="icon" class="relative h-8 w-8 p-0">
      <span class="sr-only">Open menu</span>
      <Ellipsis class="h-4 w-4" />
    </Button>
  </DropdownMenu.Trigger>
  <DropdownMenu.Content>
    <DropdownMenu.Group>
      <DropdownMenu.Label>Actions</DropdownMenu.Label>
      <DropdownMenu.Item on:click={() => navigator.clipboard.writeText(id)}>
        Copy payment ID
      </DropdownMenu.Item>
    </DropdownMenu.Group>
    <DropdownMenu.Separator />
    <DropdownMenu.Item></DropdownMenu.Item>
    <DropdownMenu.Item>View payment details</DropdownMenu.Item>
  </DropdownMenu.Content>
</DropdownMenu.Root> -->

<Dialog.Root>
  <Dialog.Trigger>
    <Icon class="h-[1.2rem] w-[1.2rem]" />
  </Dialog.Trigger>
  <Dialog.Content class="w-full max-w-[1500px]">
    <!-- <Dialog.Header>
      <Dialog.Title>Are you sure absolutely sure?</Dialog.Title>
      <Dialog.Description>
        This action cannot be undone. This will permanently delete your account and remove your data
        from our servers.
      </Dialog.Description>
    </Dialog.Header> -->

    <SpaceMarineEdit {data} />
  </Dialog.Content>
</Dialog.Root>
