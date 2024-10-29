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
  import DataTableActions from './data-table-actions.svelte';
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

  let data = writable<{ id: number; username: string }[]>([]);

  const refresh = async () => {
    data.set((await api.get('/auth/pending')).data);
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

  const columns = table.createColumns([
    // Selection Checkbox
    // table.column({
    //   accessor: 'id',
    //   header: (_, { pluginStates }) => {
    //     const { allPageRowsSelected } = pluginStates.select;
    //     return createRender(DataTableCheckbox, {
    //       checked: allPageRowsSelected
    //     });
    //   },
    //   cell: ({ row }, { pluginStates }) => {
    //     const { getRowState } = pluginStates.select;
    //     const { isSelected } = getRowState(row);

    //     return createRender(DataTableCheckbox, {
    //       checked: isSelected
    //     });
    //   },
    //   plugins: {
    //     sort: {
    //       disable: true
    //     },
    //     filter: {
    //       exclude: true
    //     }
    //   }
    // }),
    // Name Column
    // table.column({
    //   accessor: 'id',
    //   header: 'ID'
    // }),
    // Name Column
    table.column({
      accessor: 'username',
      header: 'Username'
    }),
    // Coordinates Column
    // Actions Column
    table.column({
      accessor: (item) => item,
      header: '',
      cell: (item) => {
        return createRender(DataTableActions, {
          id: String(item.value.id),
          refresh
        });
      },
      plugins: {
        sort: {
          disable: true
        },
        filter: {
          exclude: true
        }
      }
    })
  ]);

  const { headerRows, tableAttrs, tableBodyAttrs, rows } = table.createViewModel(columns);
</script>

<div>
  <div class="grid w-full justify-items-center">
    <h1 class="text-xl">Approve requests</h1>
    <br />
    <div class="max-w-300px w-full rounded-md border">
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
                      {#if cell.id === 'username'}
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
