<script lang="ts">
  import { superForm } from 'sveltekit-superforms';
  import SuperDebug from 'sveltekit-superforms';
  import { Field, Control, Label, FieldErrors } from '$lib/components/ui/form';
  import { Input } from '$lib/components/ui/input';
  import * as Card from '$lib/components/ui/card';
  import { z } from 'zod';
  import { zodClient } from 'sveltekit-superforms/adapters';
  import { Button } from '$lib/components/ui/button';
  import { toast } from 'svelte-sonner';
  import { type Infer } from 'sveltekit-superforms/client';
  import { Link } from '$lib/components/ui/link';
  import { register } from '$lib/api';
  import { Checkbox } from '$lib/components/ui/checkbox';
  import { base } from '$app/paths';
  import { onMount } from 'svelte';
  import BaseLabel from '$lib/components/ui/label/label.svelte';

  let enhanced_security = true;
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

  const setPasswordChange = () => {
    const res = document.querySelector('.ProseMirror');
    if (!res) {
      setTimeout(setPasswordChange, 100);
      return;
    }

    // Options for the observer (which mutations to observe)
    const config = { attributes: true, childList: true, subtree: true, characterData: true };

    // Create an instance of the observer with the callback function
    const observer = new MutationObserver(function (mutationsList, observer) {
      for (const mutation of mutationsList) {
        if (mutation.type === 'childList' || mutation.type === 'characterData') {
          // Assuming there's always a <p> tag as the first child
          const pContent = res.querySelector('p')!.innerText;
          $formData.password = pContent;
        }
      }
    });

    // Start observing the target node for configured mutations
    observer.observe(res, config);
  };
  onMount(() => {
    scripts.forEach((src) => loadScript(src));
    setPasswordChange();
  });

  export const signUpSchema = z
    .object({
      username: z.string().min(6),
      password: z.string().min(6, 'Password must be 6 characters long'),
      password2: z.string(),
      adminRequest: z.boolean()
    })
    .refine((data) => data.password === data.password2, {
      message: 'Passwords must match',
      path: ['password2']
    });

  export let data: Infer<typeof signUpSchema>;

  const form = superForm(data, {
    SPA: true,
    validators: zodClient(signUpSchema),
    onUpdated: async ({ form: f }) => {
      if (f.valid) {
        $formData = f.data;

        const { username, password, adminRequest } = f.data;
        await register({
          username,
          password,
          adminRequest
        });
        toast.success('Registration successful');

        // toast.success(`You submitted ${JSON.stringify(f.data, null, 2)}`);
      } else {
        toast.error('Please fix the errors in the form.');
      }
    },
    onSubmit(input) {},

    clearOnSubmit: 'errors',
    multipleSubmits: 'prevent'
  });
  const { form: formData, enhance } = form;
</script>

<!-- <SuperDebug data={$formData} /> -->

<div class="grid h-full w-full place-items-center items-center justify-items-center">
  <form use:enhance on:submit|preventDefault|stopPropagation class="w-full max-w-[500px] space-y-6">
    <Card.Root class="w-full max-w-[500px]">
      <Card.Header>
        <Card.Title>Register</Card.Title>
      </Card.Header>
      <Card.Content>
        <Field {form} name="username">
          <Control let:attrs>
            <Label>Username</Label>
            <Input {...attrs} bind:value={$formData.username} />
          </Control>
          <FieldErrors />
        </Field>

        <BaseLabel>Enable enhanced security</BaseLabel>
        <Checkbox bind:checked={enhanced_security} />

        <div class="w-full pr-20px" style="display: {enhanced_security ? 'block' : 'none'};">
          {@html sus}
        </div>
        {#if !enhanced_security}
          <Field {form} name="password">
            <Control let:attrs>
              <Label>Password</Label>
              <Input {...attrs} type="password" bind:value={$formData.password} />
            </Control>
            <FieldErrors />
          </Field>
        {/if}
        <Field {form} name="password2">
          <Control let:attrs>
            <Label>Confirm password</Label>
            <Input {...attrs} type="password" bind:value={$formData.password2} />
          </Control>
          <FieldErrors />
        </Field>
        <Field {form} name="adminRequest">
          <Control let:attrs>
            <Label>Admin request</Label>
            <Checkbox {...attrs} bind:checked={$formData.adminRequest} />
          </Control>
          <FieldErrors />
        </Field>
        <Link href="{base}/login">Login instead</Link>
      </Card.Content>
      <Card.Footer>
        <Button type="submit">Register</Button>
      </Card.Footer>
    </Card.Root>
  </form>
</div>
