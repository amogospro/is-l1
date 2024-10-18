<script lang="ts">
  import { onMount } from 'svelte';
  import * as d3 from 'd3';
  import { toast } from 'svelte-sonner';
  import * as Dialog from '$lib/components/ui/dialog/index.js';
  import SpaceMarineEdit from '$lib/components/custom/space-marine-edit.svelte';
  import type { Product } from '$lib/types';
  import { products } from '$lib/data';

  let svg;

  let data: Product | null = null;

  const spaceMarines = products;
  const hash = (str: string) => {
    let hash = 0;
    for (let i = 0; i < str.length; i++) {
      const charCode = str.charCodeAt(i);
      hash = (hash << 5) - hash + charCode;
      hash = hash & hash; // Convert to 32bit integer
    }
    const seed = Math.abs(hash);
    const random = Math.sin(seed) * 10000;
    const res = Math.floor((random - Math.floor(random)) * 360);

    console.log(res);
    return res;
  };

  onMount(() => {
    const width = 800,
      height = 800,
      margin = 50;

    // const x = d3
    //   .scaleLinear()
    //   .domain([
    //     d3.min(spaceMarines, (d) => d.coordinates.x) ?? 0 - 50,
    //     d3.max(spaceMarines, (d) => d.coordinates.x) ?? 0 + 50
    //   ])
    //   .range([0, width]);

    // const y = d3
    //   .scaleLinear()
    //   .domain([
    //     d3.min(spaceMarines, (d) => d.coordinates.y) ?? 0 - 50,
    //     d3.max(spaceMarines, (d) => d.coordinates.y) ?? 0 + 50
    //   ])
    //   .range([height, 0]);

    svg = d3.select('#visualization').append('svg').attr('width', width).attr('height', height);

    const x = d3
      .scaleLinear()
      .domain([
        d3.min(spaceMarines, (d) => d.coordinates.x - 50) ?? 0,
        d3.max(spaceMarines, (d) => d.coordinates.x + 50) ?? 0
      ])
      .range([margin, width - margin]);

    const y = d3
      .scaleLinear()
      .domain([
        d3.min(spaceMarines, (d) => d.coordinates.y - 50) ?? 0,
        d3.max(spaceMarines, (d) => d.coordinates.y + 50) ?? 0
      ])
      .range([height - margin, margin]);

    // Draw Grid
    const xAxis = d3
      .axisBottom(x)
      // .ticks(5)
      .tickSize(-height + 2 * margin)
      .tickPadding(10);
    const yAxis = d3
      .axisLeft(y)
      // .ticks(5)
      .tickSize(-width + 2 * margin)
      .tickPadding(10);

    svg
      .append('g')
      .attr('transform', `translate(0, ${height - margin})`)
      .call(xAxis);

    svg.append('g').attr('transform', `translate(${margin}, 0)`).call(yAxis);

    // Adding the grid lines
    svg.selectAll('.tick line').attr('stroke', 'lightgrey');

    // Draw SpaceMarines
    svg
      .selectAll('.marine')
      .data(spaceMarines)
      .enter()
      .append('image')
      .attr('xlink:href', '/space-marine.png')
      .attr('x', (d) => x(d.coordinates.x) - 25) // Adjust for image centering
      .attr('y', (d) => y(d.coordinates.y) - 25) // Adjust for image centering
      .attr('width', 50)
      .attr('height', 50)
      .attr('fill', (d) => d3.scaleOrdinal(d3.schemeCategory10)(d.name))
      .on('click', (e, item) => onClick(item))
      .attr(
        'filter',
        (d) => `invert(20%) sepia(40%) saturate(1352%) hue-rotate(${hash(d.name)}deg)`
      );
  });

  const onClick = (item: any) => {
    data = item;
    console.log(item);
    // toast.info(JSON.stringify(item));
  };
</script>

<div id="visualization"></div>

<Dialog.Root open={data != null}>
  <Dialog.Trigger></Dialog.Trigger>
  <Dialog.Content class="w-full max-w-[1500px]">
    <!-- <Dialog.Header>
      <Dialog.Title>Are you sure absolutely sure?</Dialog.Title>
      <Dialog.Description>
        This action cannot be undone. This will permanently delete your account and remove your data
        from our servers.
      </Dialog.Description>
    </Dialog.Header> -->

    {#if data != null}
      <SpaceMarineEdit {data} />
    {/if}
  </Dialog.Content>
</Dialog.Root>
