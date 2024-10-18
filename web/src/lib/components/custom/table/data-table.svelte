<script lang="ts">
  import { createTable, Render, Subscribe, createRender } from 'svelte-headless-table';
  import {
    addPagination,
    addSortBy,
    addTableFilter,
    addHiddenColumns,
    addSelectedRows
  } from 'svelte-headless-table/plugins';
  import * as Dialog from '$lib/components/ui/dialog';
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
  import type { Product } from '$lib/types';
  import { products } from '$lib/data';
  import _ from 'lodash';
  import SpaceMarineEdit from '../space-marine-edit.svelte';

  const data: Product[] = JSON.parse(JSON.stringify(products));

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
    // Selection Checkbox
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
    // Name Column
    table.column({
      accessor: 'name',
      header: 'Name'
    }),
    // Coordinates Column
    table.column({
      accessor: 'coordinates',
      header: 'Coordinates',
      cell: ({ value }) => {
        return `x: ${value.x}, y: ${value.y}`;
      }
    }),
    // Price Column
    table.column({
      accessor: 'price',
      header: 'Price',
      cell: ({ value }) => `$${value.toFixed(2)}`
    }),
    // Manufacture Cost Column
    table.column({
      accessor: 'manufactureCost',
      header: 'Manufacture Cost',
      cell: ({ value }) => `$${value.toFixed(2)}`
    }),
    // Rating Column
    table.column({
      accessor: 'rating',
      header: 'Rating',
      cell: ({ value }) => (value != null ? value : 'N/A')
    }),
    // Unit of Measure Column
    table.column({
      accessor: 'unitOfMeasure',
      header: 'Unit of Measure',
      cell: ({ value }) => (value ? _.startCase(_.toLower(value)) : '')
    }),
    // Creation Date Column
    table.column({
      accessor: 'creationDate',
      header: 'Creation Date',
      cell: ({ value }) => {
        const formatted = moment(value).fromNow();
        return formatted;
      }
    }),
    // Manufacturer Name Column
    table.column({
      accessor: (item) => item,
      header: 'Manufacturer',
      cell: ({ value }) => {
        return value.manufacturer ? value.manufacturer.name : 'N/A';
      }
    }),
    // Owner Name Column
    table.column({
      accessor: 'owner',
      header: 'Owner'
    }),
    // Actions Column
    table.column({
      accessor: (item) => item,
      header: '',
      cell: (item) => {
        return createRender(DataTableActions, {
          id: String(item.value.id),
          data: item.value
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

  const hidableCols = [
    'name',
    'price',
    'manufactureCost',
    'rating',
    'unitOfMeasure',
    'manufacturer',
    'owner.name'
  ];
</script>

<div>
  <div class="gap-10px flex items-center py-4">
    <Input class="max-w-sm" placeholder="Filter by name..." type="text" bind:value={$filterValue} />

    <Dialog.Root>
      <Dialog.Trigger class="ml-auto">
        <!-- <Icon class="h-[1.2rem] w-[1.2rem]" /> -->
        <Button>New Product</Button>
      </Dialog.Trigger>
      <Dialog.Content class="w-full max-w-[1500px]">
        <!-- <Dialog.Header>
      <Dialog.Title>Are you sure absolutely sure?</Dialog.Title>
      <Dialog.Description>
        This action cannot be undone. This will permanently delete your account and remove your data
        from our servers.
      </Dialog.Description>
    </Dialog.Header> -->

        <SpaceMarineEdit
          data={{
            id: 1,
            name: 'Super Widget',
            coordinates: {
              x: 500, // Within the maximum value of 864
              y: 300
            },
            creationDate: new Date(), // Auto-generated
            unitOfMeasure: 'METERS',
            manufacturer: {
              id: 1,
              name: 'Acme Corporation',
              officialAddress: {
                zipCode: '12345',
                town: {
                  x: 10,
                  y: 20,
                  z: 30
                }
              },
              annualTurnover: 1_000_000, // Value > 0
              employeesCount: 250, // Value > 0
              rating: 5, // Value > 0
              type: 'COMMERCIAL'
            },
            price: 150, // Value > 0
            manufactureCost: 75,
            rating: 4, // Value > 0
            owner: {
              name: 'Alice Johnson',
              eyeColor: 'BLUE',
              hairColor: 'YELLOW', // Valid enum value
              location: {
                x: 5,
                y: 10,
                z: 15
              },
              birthday: new Date('1990-01-01'),
              nationality: 'USA'
            }
          }}
        >
          <svelte:fragment slot="title">Create new Product</svelte:fragment>
          <svelte:fragment slot="button">Create</svelte:fragment>
        </SpaceMarineEdit>
      </Dialog.Content>
    </Dialog.Root>

    <DropdownMenu.Root>
      <DropdownMenu.Trigger asChild let:builder>
        <Button variant="outline" builders={[builder]}>
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
                    <Render of={cell.render()} />
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
                    <Render of={cell.render()} />
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
      {Object.keys($selectedDataIds).length} of {$rows.length} row(s) selected.
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
