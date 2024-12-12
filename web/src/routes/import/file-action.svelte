<script lang="ts">
  import api from '$lib/api';
  import { Button } from '$lib/components/ui/button';
  import Link from '$lib/components/ui/link/link.svelte';
  import { toast } from 'svelte-sonner';

  export let file: string;

  const good = (input: string) => {
    // Regular expression to match the date pattern
    const regex = /\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}.\d+/;

    // Replace the date part with an empty string
    const output = input.replace(regex, '');

    return output;
  };

  function amogusFile(content: string, filename: string): void {
    // Create a Blob from the content
    const blob = new Blob([content], { type: 'text/plain' });

    // Create a URL for the Blob
    const url = window.URL.createObjectURL(blob);

    // Create a temporary anchor element and trigger the download
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = filename;
    document.body.appendChild(anchor);
    anchor.click();

    // Clean up by removing the anchor element and revoking the Blob URL
    document.body.removeChild(anchor);
    window.URL.revokeObjectURL(url);
  }

  const downloadFile = async () => {
    const { data } = await api.get(`/import/download/imported-files/${file}`);
    console.log(data);

    amogusFile(JSON.stringify(data, null, 2), good(file));
    toast.info('Downloading file');
  };
</script>

<Button variant="link" on:click={downloadFile}>
  {good(file)}
</Button>
