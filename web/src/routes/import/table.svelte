<script lang="ts">
  import { createTable, Render, Subscribe, createRender } from 'svelte-headless-table';
  import {
    addPagination,
    addSortBy,
    addTableFilter,
    addHiddenColumns,
    addSelectedRows,
    type SortKey
  } from 'svelte-headless-table/plugins';
  import * as Dialog from '$lib/components/ui/dialog';
  import { readable, writable } from 'svelte/store';
  import ArrowUp from 'lucide-svelte/icons/arrow-up';
  import ArrowDown from 'lucide-svelte/icons/arrow-down';
  import ArrowUpDown from 'lucide-svelte/icons/arrow-up-down';
  import ChevronDown from 'lucide-svelte/icons/chevron-down';
  import * as Table from '$lib/components/ui/table';
  import { Button } from '$lib/components/ui/button';
  import { Input } from '$lib/components/ui/input';
  import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
  import moment from 'moment';
  import type { Product } from '$lib/types';
  // import { products } from '$lib/data';
  import _ from 'lodash';
  import { toast } from 'svelte-sonner';
  import api, { createProduct, products, getProducts, refresh_signal } from '$lib/api';
  import { cn } from '$lib/utils';
  import { onMount } from 'svelte';
  import FileAction from './file-action.svelte';

  type ImportRes = {
    id: number;
    fileName: string;
    userName: string;
    timestamp: string;
    status: string;
    importedObjectsCount: number;
  };
  let data = writable<ImportRes[]>([]);

  // const importHistory: ImportRes[] = [
  //   { id: 1, status: 'completed', userName: 'user1', importedObjectsCount: 150 },
  //   { id: 2, status: 'failed', userName: 'user2', importedObjectsCount: 0 },
  //   { id: 3, status: 'completed', userName: 'admin1', importedObjectsCount: 200 },
  //   { id: 4, status: 'completed', userName: 'user1', importedObjectsCount: 120 },
  //   { id: 5, status: 'failed', userName: 'user3', importedObjectsCount: 0 },
  //   { id: 6, status: 'completed', userName: 'user4', importedObjectsCount: 90 },
  //   { id: 7, status: 'completed', userName: 'admin1', importedObjectsCount: 50 },
  //   { id: 8, status: 'completed', userName: 'user2', importedObjectsCount: 135 },
  //   { id: 9, status: 'failed', userName: 'user4', importedObjectsCount: 0 },
  //   { id: 10, status: 'completed', userName: 'user5', importedObjectsCount: 85 }
  // ];

  const refresh = async () => {
    data.set((await api.get('/import')).data);
    // data.set((await api.get('/auth/pending')).data);
    console.log($data);
  };
  onMount(async () => {
    await refresh();
  });
  // data = $products

  // products.subscribe((products) => {
  //   console.log(products);
  //   data = products;
  // });

  const table = createTable(data, {});

  const statusToEmoji = (status: string) => {
    switch (status) {
      case 'SUCCESS':
        return '✅'; // Emoji for completed
      case 'FAIL':
        return '❌'; // Emoji for failed
      default:
        return '🔄'; // Emoji for in-progress or unknown
    }
  };

  const columns = table.createColumns([
    table.column({
      accessor: 'id',
      header: 'ID'
    }),
    table.column({
      accessor: 'status',
      header: 'Status',
      cell: ({ value }) => {
        return `${statusToEmoji(value)}`;
      }
    }),
    table.column({
      accessor: 'userName',
      header: 'Username'
    }),
    table.column({
      accessor: 'importedObjectsCount',
      header: 'Added items'
    }),
    table.column({
      accessor: 'fileName',
      header: 'File',
      cell: (cell) => {
        return createRender(FileAction, {
          file: cell.value
        });
      }
    }),
    table.column({
      accessor: 'timestamp',
      header: 'When',
      cell: ({ value }) => {
        const formatted = moment.utc(value).fromNow();
        return formatted;
      }
    })
  ]);

  const { headerRows, tableAttrs, tableBodyAttrs, rows } = table.createViewModel(columns);
</script>

<div>
  <div class="grid w-full">
    <h1 class="text-xl">Import history</h1>
    <br />
    <div class=" w-full rounded-md border">
      <Table.Root {...$tableAttrs}>
        <Table.Header>
          {#each $headerRows as headerRow}
            <Subscribe rowAttrs={headerRow.attrs()}>
              <Table.Row>
                {#each headerRow.cells as cell (cell.id)}
                  <Subscribe attrs={cell.attrs()} let:attrs props={cell.props()} let:props>
                    <Table.Head {...attrs} class="[&:has([role=checkbox])]:pl-3">
                      <Render of={cell.render()} />
                    </Table.Head>
                  </Subscribe>
                {/each}
              </Table.Row>
            </Subscribe>
          {/each}
        </Table.Header>
        <Table.Body {...$tableBodyAttrs}>
          {#each $rows as row (row.id)}
            <Subscribe rowAttrs={row.attrs()} let:rowAttrs>
              <Table.Row {...rowAttrs}>
                {#each row.cells as cell (cell.id)}
                  <Subscribe attrs={cell.attrs()} let:attrs>
                    <Table.Cell {...attrs} class="[&:has([role=checkbox])]:pl-3">
                      {#if cell.id === 'userName'}
                        <div class="w-150px">
                          <Render of={cell.render()} />
                        </div>
                      {:else}
                        <Render of={cell.render()} />
                      {/if}
                    </Table.Cell>
                  </Subscribe>
                {/each}
              </Table.Row>
            </Subscribe>
          {/each}
        </Table.Body>
      </Table.Root>
    </div>
  </div>
</div>
