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
        <Field {form} name="password">
          <Control let:attrs>
            <Label>Password</Label>
            <Input {...attrs} type="password" bind:value={$formData.password} />
          </Control>
          <FieldErrors />
        </Field>
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
