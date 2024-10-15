<script lang="ts">
  import { createTable, Render, Subscribe, createRender } from 'svelte-headless-table';
  import {
    addPagination,
    addSortBy,
    addTableFilter,
    addHiddenColumns,
    addSelectedRows
  } from 'svelte-headless-table/plugins';
  import { readable } from 'svelte/store';
  import ArrowUpDown from 'lucide-svelte/icons/arrow-up-down';
  import ChevronDown from 'lucide-svelte/icons/chevron-down';
  import * as Table from '$lib/components/ui/table';
  import DataTableActions from './data-table-actions.svelte';
  import { Button } from '$lib/components/ui/button';
  import { Input } from '$lib/components/ui/input';
  import * as DropdownMenu from '$lib/components/ui/dropdown-menu';
  import DataTableCheckbox from './data-table-checkbox.svelte';
  import moment from 'moment';
  import type { SpaceMarine, SpaceMarineScheme } from '$lib/types';
  import { spaceMarines } from '$lib/data';
  import _ from 'lodash';

  const data: SpaceMarine[] = JSON.parse(JSON.stringify(spaceMarines));

  const table = createTable(readable(data), {
    page: addPagination(),
    sort: addSortBy({ disableMultiSort: true }),
    filter: addTableFilter({
      includeHiddenColumns: true,
      fn: ({ filterValue, value }) => !filterValue || value === filterValue
    }),
    hide: addHiddenColumns(),
    select: addSelectedRows()
  });

  const columns = table.createColumns([
    table.column({
      accessor: 'id',
      header: (_, { pluginStates }) => {
        const { allPageRowsSelected } = pluginStates.select;
        return createRender(DataTableCheckbox, {
          checked: allPageRowsSelected
        });
      },
      cell: ({ row }, { pluginStates }) => {
        const { getRowState } = pluginStates.select;
        const { isSelected } = getRowState(row);

        return createRender(DataTableCheckbox, {
          checked: isSelected
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
    }),
    table.column({
      accessor: 'name',
      header: 'Name'
      // plugins: {
      //   sort: {
      //     disable: true
      //   },
      //   filter: {
      //     exclude: true
      //   }
      // }
    }),
    table.column({
      accessor: 'coordinates',
      header: 'Coordinates',
      cell: ({ value }) => {
        // const formatted = new Intl.NumberFormat('en-US', {
        //   style: 'currency',
        //   currency: 'USD'
        // }).format(value);
        return `x: ${value.x}, y: ${value.y}`;
      },
      plugins: {
        sort: {
          // disable: true
        },
        filter: {
          // exclude: true
        }
      }
    }),
    table.column({
      accessor: (item) => item,

      header: 'Chapter Name',
      cell: ({ value }) => {
        return value.chapter?.name ?? '';
      }
    }),
    table.column({
      accessor: (item) => item,

      header: 'Chapter World',
      cell: ({ value }) => {
        return value.chapter?.world ?? '';
      }
    }),
    table.column({
      accessor: 'health',
      header: 'Health',
      cell: ({ value }) => {
        return `${value} hp`;
      },
      plugins: {
        sort: {
          // disable: true
        },
        filter: {
          // exclude: true
        }
      }
    }),
    table.column({
      accessor: 'category',
      header: 'Category',
      cell: ({ value }) => {
        return value ? _.startCase(_.toLower(value)) : '';
      }
    }),
    table.column({
      accessor: 'weaponType',
      header: 'Weapon Type',
      cell: ({ value }) => {
        return value ? _.startCase(_.toLower(value)) : '';
      }
    }),
    table.column({
      accessor: 'meleeWeapon',
      header: 'Melee Weapon',
      cell: ({ value }) => {
        return value ? _.startCase(_.toLower(value)) : '';
      }
    }),
    table.column({
      accessor: 'creationDate',
      header: 'Creation Date',
      cell: ({ value }) => {
        const formatted = moment(value).fromNow();
        return formatted;
      }
    }),
    table.column({
      accessor: (item) => item,
      header: '',
      cell: (item, state) => {
        return createRender(DataTableActions, { id: String(item.value.id), data: item.value });
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

  const { headerRows, pageRows, tableAttrs, tableBodyAttrs, pluginStates, flatColumns, rows } =
    table.createViewModel(columns);

  const { pageIndex, hasNextPage, hasPreviousPage } = pluginStates.page;
  const { filterValue } = pluginStates.filter;
  const { hiddenColumnIds } = pluginStates.hide;
  const { selectedDataIds } = pluginStates.select;

  const ids = flatColumns.map((col) => col.id);
  let hideForId = Object.fromEntries(ids.map((id) => [id, true]));

  $: $hiddenColumnIds = Object.entries(hideForId)
    .filter(([, hide]) => !hide)
    .map(([id]) => id);

  const hidableCols = ['name'];
</script>

<div>
  <div class="flex items-center py-4">
    <Input class="max-w-sm" placeholder="Filter by name..." type="text" bind:value={$filterValue} />
    <DropdownMenu.Root>
      <DropdownMenu.Trigger asChild let:builder>
        <Button variant="outline" class="ml-auto" builders={[builder]}>
          Columns <ChevronDown class="ml-2 h-4 w-4" />
        </Button>
      </DropdownMenu.Trigger>
      <DropdownMenu.Content>
        {#each flatColumns as col}
          {#if hidableCols.includes(col.id)}
            <DropdownMenu.CheckboxItem bind:checked={hideForId[col.id]}>
              {col.header}
            </DropdownMenu.CheckboxItem>
          {/if}
        {/each}
      </DropdownMenu.Content>
    </DropdownMenu.Root>
  </div>
  <div class="rounded-md border">
    <Table.Root {...$tableAttrs}>
      <Table.Header>
        {#each $headerRows as headerRow}
          <Subscribe rowAttrs={headerRow.attrs()}>
            <Table.Row>
              {#each headerRow.cells as cell (cell.id)}
                <Subscribe attrs={cell.attrs()} let:attrs props={cell.props()} let:props>
                  <Table.Head {...attrs} class="[&:has([role=checkbox])]:pl-3">
                    {#if cell.id === 'amount'}
                      <div class="text-right">
                        <Render of={cell.render()} />
                      </div>
                    {:else if cell.id === 'name'}
                      <p>amogus</p>
                    {:else}
                      <Render of={cell.render()} />
                    {/if}
                  </Table.Head>
                </Subscribe>
              {/each}
            </Table.Row>
          </Subscribe>
        {/each}
      </Table.Header>
      <Table.Body {...$tableBodyAttrs}>
        {#each $pageRows as row (row.id)}
          <Subscribe rowAttrs={row.attrs()} let:rowAttrs>
            <Table.Row {...rowAttrs} data-state={$selectedDataIds[row.id] && 'selected'}>
              {#each row.cells as cell (cell.id)}
                <Subscribe attrs={cell.attrs()} let:attrs>
                  <Table.Cell {...attrs} class="[&:has([role=checkbox])]:pl-3">
                    {#if cell.id === 'amount'}
                      <div class="text-right font-medium">
                        <Render of={cell.render()} />
                      </div>
                    {:else if cell.id === 'status'}
                      <div class="capitalize">
                        <Render of={cell.render()} />
                      </div>
                    {:else if cell.id === 'status'}
                      <div class="capitalize">
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
  <div class="flex items-center justify-end space-x-4 py-4">
    <div class="text-muted-foreground flex-1 text-sm">
      {Object.keys($selectedDataIds).length} of{' '}
      {$rows.length} row(s) selected.
    </div>
    <Button
      variant="outline"
      size="sm"
      on:click={() => ($pageIndex = $pageIndex - 1)}
      disabled={!$hasPreviousPage}>Previous</Button
    >
    <Button
      variant="outline"
      size="sm"
      disabled={!$hasNextPage}
      on:click={() => ($pageIndex = $pageIndex + 1)}>Next</Button
    >
  </div>
</div>
