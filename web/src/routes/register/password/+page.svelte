<script lang="ts">
  import { base } from '$app/paths';
  import { onMount } from 'svelte';

  const sus = `<div data-server-rendered="true" id="__nuxt">
		<div id="__layout">
			<div>
				
			</div>
		</div>
	</div>
`;
  // List of scripts to load
  const scripts = [
    `${base}/_nuxt/static/1730312300/password-game/state.js`,
    // `${base}/_nuxt/static/1730312300/password-game/payload.js`,
    `${base}/_nuxt/26c24c8.js`,
    `${base}/_nuxt/6b074e1.js`,
    `${base}/_nuxt/0d0a72c.js`,
    `${base}/_nuxt/29fc085.js`,
    `${base}/_nuxt/a560b8a.js`,
    `${base}/_nuxt/39017cb.js`,
    `${base}/_nuxt/6bd6cb3.js`,
    `${base}/_nuxt/7ca8a56.js`,
    `${base}/_nuxt/57a5676.js`
  ];

  // Function to load a script
  function loadScript(src: string) {
    const script = document.createElement('script');
    script.src = src;
    script.defer = true;
    document.head.appendChild(script);
  }

  // Load all scripts on component mount
  onMount(() => {
    scripts.forEach((src) => loadScript(src));
  });
</script>

<p>amogus</p>

{@html sus}
