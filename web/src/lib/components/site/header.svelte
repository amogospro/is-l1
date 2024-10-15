<script lang="ts">
  // import {
  // 	CommandMenu,
  // 	MainNav,
  // 	MobileNav,
  // 	ModeToggle,
  // } from "$lib/components/docs/index.js";
  // import { buttonVariants } from "$lib/registry/new-york/ui/button/index.js";
  import Button from '$lib/components/ui/button/button.svelte';
  import Sun from 'svelte-radix/Sun.svelte';
  import Moon from 'svelte-radix/Moon.svelte';
  // import { siteConfig } from "$lib/config/site.js";
  import { cn } from '$lib/utils.js';
  import { toggleMode } from 'mode-watcher';
  import Link from '../ui/link/link.svelte';
</script>

<header
  class="border-border/40 bg-background/95 supports-[backdrop-filter]:bg-background/60 sticky top-0 z-50 w-full border-b backdrop-blur"
>
  <div class="container flex h-14 max-w-screen-2xl items-center gap-2">
    <h1 class="text-xl font-bold">Лабораторная работа #1</h1>
    <h1 class="text-xl">Space Marines</h1>
    <Link href="/">Main menu</Link>
    <!-- <MainNav />
		<MobileNav /> -->
    <div class="flex flex-1 items-center justify-between space-x-2 md:justify-end">
      <div class="w-full flex-1 md:w-auto md:flex-none">
        <!-- <CommandMenu /> -->
      </div>
      <nav class="flex items-center">
        <!-- <a href={siteConfig.links.github} target="_blank" rel="noopener noreferrer">
					<div
						class={cn(
							buttonVariants({
								size: "sm",
								variant: "ghost",
							}),
							"h-8 w-8 px-0"
						)}
					>
						<Icons.gitHub class="h-4 w-4" />
						<span class="sr-only">GitHub</span>
					</div>
				</a>
				<a href={siteConfig.links.twitter} target="_blank" rel="noreferrer">
					<div
						class={cn(
							buttonVariants({
								size: "sm",
								variant: "ghost",
							}),
							"h-8 w-8 px-0"
						)}
					>
						<Icons.twitter class="h-3 w-3 fill-current" />
						<span class="sr-only">X (formerly known as Twitter)</span>
					</div>
				</a> -->
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
