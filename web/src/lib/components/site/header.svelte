<script lang="ts">
  import Button from '$lib/components/ui/button/button.svelte';
  import Sun from 'svelte-radix/Sun.svelte';
  import Moon from 'svelte-radix/Moon.svelte';
  import Exit from 'svelte-radix/Exit.svelte';
  import { toggleMode } from 'mode-watcher';
  import Link from '../ui/link/link.svelte';
  import _ from 'lodash';
  import { clearToken, role, username } from '$lib/api';
  import { goto } from '$app/navigation';
  import { base } from '$app/paths';

  function logOut() {
    clearToken();
    goto(`${base}/login`);
  }
</script>

<header
  class="border-border/40 bg-background/95 supports-[backdrop-filter]:bg-background/60 sticky top-0 z-50 w-full border-b backdrop-blur"
>
  <div class="container flex h-14 max-w-screen-2xl items-center gap-2">
    <h1 class="text-xl font-bold">Лабораторная работа #1</h1>
    <!-- <h1 class="text-xl">Space Marines</h1> -->
    <Link href="{base}/">Table</Link>
    <Link href="{base}/extra">Extra functions</Link>
    <Link href="{base}/visualization">Visualization</Link>
    <Link href="{base}/import">File import</Link>

    <div class="flex flex-1 items-center justify-between space-x-2 md:justify-end">
      <nav class="gap-10px flex items-center">
        <div class="gap-10px flex">
          <p>
            Hi
            {#if $username != null}
              {$username}, you are
            {/if}
            {#if $role != null}
              {_.startCase(_.toLower($role))}
            {/if}
          </p>

          {#if _.lowerCase($role) === 'admin'}
            <Link class="ml-10px" href="{base}/admin">Pending requests</Link>
          {/if}
        </div>
        <Button on:click={logOut} variant="outline" size="icon">
          <Exit
            class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0"
          />
          <Exit
            class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100"
          />
          <span class="sr-only">Logout</span>
        </Button>

        <Button on:click={toggleMode} variant="outline" size="icon">
          <Sun
            class="h-[1.2rem] w-[1.2rem] rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0"
          />
          <Moon
            class="absolute h-[1.2rem] w-[1.2rem] rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100"
          />
          <span class="sr-only">Toggle theme</span>
        </Button>
      </nav>
    </div>
  </div>
</header>
