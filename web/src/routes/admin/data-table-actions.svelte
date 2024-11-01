<script lang="ts">
  import { Button } from '$lib/components/ui/button';
  import api, { deleteProduct, updateProduct } from '$lib/api';
  import { toast } from 'svelte-sonner';
  import * as AlertDialog from '$lib/components/ui/alert-dialog/index.js';  

  export let id: string;

  export let refresh: () => any;

  const approve = async (id: string | number) => {
    const { data } = await api.post(`/auth/approve-admin/${id}`);
    toast.info('Request approved!');
    await refresh();
  };
</script>

<div class="gap-10px flex items-center justify-items-center">
  <AlertDialog.Root>
    <AlertDialog.Trigger asChild let:builder>
      <Button builders={[builder]} variant="outline">Approve</Button>
    </AlertDialog.Trigger>
    <AlertDialog.Content>
      <AlertDialog.Header>
        <AlertDialog.Title>Are you absolutely sure?</AlertDialog.Title>
        <AlertDialog.Description>This action cannot be undone.</AlertDialog.Description>
      </AlertDialog.Header>
      <AlertDialog.Footer>
        <AlertDialog.Cancel>Cancel</AlertDialog.Cancel>
        <AlertDialog.Action on:click={() => approve(id)}>Approve</AlertDialog.Action>
      </AlertDialog.Footer>
    </AlertDialog.Content>
  </AlertDialog.Root>
</div>
