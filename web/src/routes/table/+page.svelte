<script lang="ts">
  import SpaceMarineEdit from '$lib/components/custom/space-marine-edit.svelte';
  import DataTable from '$lib/components/custom/table/data-table.svelte';
  import { spaceMarines } from '$lib/data';
</script>

<div class="container mx-auto grid w-full py-10">
  <DataTable />
</div>
