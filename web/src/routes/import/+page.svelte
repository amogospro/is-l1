<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import { toast } from 'svelte-sonner';
  import api from '$lib/api';
  import { Input } from '$lib/components/ui/input/index.js';
  import { Label } from '$lib/components/ui/label/index.js';
  import Table from './table.svelte';

  const handleFileUpload = async (event: Event) => {
    let obj = {};
    let filename = 'file.json';

    try {
      const input = event.target as HTMLInputElement;
      const file = input.files?.[0];
      if (!file) {
        toast.error('No file selected');
        return;
      }

      const str = await file.text();
      obj = JSON.parse(str);
      filename = file.name;
    } catch (e: any) {
      toast.error(e.message ?? e);
      return;
    }

    const { data } = await api.post(`/import?fileName=${filename}`, obj);
    toast.success('File uploaded successfully');
  };
</script>

<!-- <SuperDebug data={$formData} /> -->

<div class="container mx-auto grid w-full py-10">
  <div class="grid grid-cols-3 gap-10">
    <div class="grid-col-span-2">
      <Table />
    </div>

    <div>
      <Card.Root class="w-full max-w-[500px]">
        <Card.Header>
          <Card.Title>Import data from file</Card.Title>
        </Card.Header>
        <Card.Content>
          <div class="grid w-full max-w-sm items-center gap-1.5">
            <Label for="picture">File</Label>
            <Input id="picture" type="file" on:change={handleFileUpload} accept=".json" />
          </div>
        </Card.Content>
      </Card.Root>
    </div>
  </div>
</div>
