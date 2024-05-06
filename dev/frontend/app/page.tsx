"use client"

import { useRouter } from "next/navigation"

import { Input } from "@/components/ui/input"
import { Button } from "@/components/ui/button"
import { toast } from "@/components/ui/use-toast"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"

import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { useEffect } from "react"

export default function Login() {

  const router = useRouter();

  const FormSchema = z.object({
    username: z.string().email(),
    password: z.string().min(4, {
      message: "Password must be at least 4 characters.",
    })
  })

  const form = useForm<z.infer<typeof FormSchema>>({
    resolver: zodResolver(FormSchema),
    defaultValues: {
      username: "",
      password: ""
    },
  });

  useEffect(() => {  
    console.log(localStorage.getItem('login'));
    if (localStorage.getItem('login')) {
      router.push("/dashboard");
    }
  }
  ,[]);

  // function onSubmit(data: z.infer<typeof FormSchema>) {
  //   toast({
  //     title: "You submitted the following values:",
  //     description: (
  //       <pre className="mt-2 w-[340px] rounded-md bg-slate-950 p-4">
  //         <code className="text-white">{JSON.stringify(data, null, 2)}</code>
  //       </pre>
  //     ),
  //   })
  //   router.push("/dashboard");
  // }

  const onSubmit = async () => {
    const email = form.getValues('username');
    const password = form.getValues('password');
    
    const url = `${process.env.NEXT_PUBLIC_BASE_API_URL}/getadmin?email=${email}&password=${password}`;

    console.log(url);
    
    const response = await fetch(url, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      },
    });
    if (response.ok) {
      const data = await response.json();
      console.log(data);
      localStorage.setItem('login', JSON.stringify(data));
      toast({
        title: "Login Success !",
        description: (
          <pre className="mt-2 w-[340px] rounded-md bg-slate-950 p-4">
            <code className="text-white">{JSON.stringify(data, null, 2)}</code>
          </pre>
        ),
      })
      router.push("/dashboard");
    } else {
      const data = await response.json();
      console.log(data);
      console.log('Login failed.');
      toast({
        title: "Login Failed !",
        description: (
          <pre className="mt-2 w-[340px] rounded-md bg-slate-950 p-4">
            <code className="text-white">{JSON.stringify(data, null, 2)}</code>
          </pre>
        ),
      })
    }
  }

  return (
    <main className="flex min-h-screen flex-col items-center justify-around p-24">
      <div className="w-full sm:w-2/6 md:w-2/6 lg:w-1/6">
        <p className="text-2xl font-extrabold mb-6">Beyond Bottles</p>
        <Form {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            <FormField
              control={form.control}
              name="username"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Email</FormLabel>
                  <FormControl>
                    <Input placeholder="email@gmail.com" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <FormField
              control={form.control}
              name="password"
              render={({ field }) => (
                <FormItem>
                  <FormLabel>Password</FormLabel>
                  <FormControl>
                    <Input type="password" placeholder="******" {...field} />
                  </FormControl>
                  <FormMessage />
                </FormItem>
              )}
            />
            <Button type="submit">Login</Button>
          </form>
        </Form>
      </div>
    </main>
  );
}