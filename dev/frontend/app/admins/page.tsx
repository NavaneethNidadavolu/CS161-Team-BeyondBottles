"use client";

import Navigation from "@/components/navigation/navigation"

import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"

import {
    AlertDialog,
    AlertDialogAction,
    AlertDialogCancel,
    AlertDialogContent,
    AlertDialogDescription,
    AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogTitle,
    AlertDialogTrigger,
} from "@/components/ui/alert-dialog"

import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger,
    DialogClose,
} from "@/components/ui/dialog"

import { zodResolver } from "@hookform/resolvers/zod";
import { set, useForm } from "react-hook-form";
import { z } from "zod";
import { toast } from "@/components/ui/use-toast";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";

import {
    Form,
    FormControl,
    FormField,
    FormItem,
    FormLabel,
    FormMessage,
} from "@/components/ui/form";

import { useAuth } from "../auth";
import { useEffect, useState } from "react";

import { useRouter } from "next/navigation"


export default function AdminsComponent() {

    const [admins, setAdmins] = useState([]);
    const [showDialog, setShowDialog] = useState(false);
    const [userId, setUserId] = useState(-1);
    const router = useRouter();

    
    useAuth();

    var login = null;

    const getAdmins = async () => {
        const url = `${process.env.NEXT_PUBLIC_BASE_API_URL}/getadmins`;

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
          setAdmins(data);
        } else {
          const data = await response.json();
          console.log(data);
          console.log('Get Admins failed.');
        }
    };

    useEffect(() => {

        console.log(localStorage.getItem('login'));

        login = localStorage.getItem('login');
        if (login) {
            setUserId(JSON.parse(login).id);
        }
        

        getAdmins();

    }, []);

    const FormSchema = z.object({
        username: z.string(),
        email: z.string().email(),
        password: z.string().min(4, {
            message: "Password must be at least 4 characters.",
        })
    })

    const form = useForm<z.infer<typeof FormSchema>>({
        resolver: zodResolver(FormSchema),
        defaultValues: {
            username: "",
            email: "",
            password: ""
        },
    });

    async function addAdmin(data: z.infer<typeof FormSchema>) {
        const url = `http://127.0.0.1:5001/addadmin`;
    
        console.log(url);

        const body = {
            username: form.getValues('username'),
            email: form.getValues('email'),
            password: form.getValues('password')
        }
        
        const response = await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        });
        if (response.ok) {
          const data = await response.json();
          console.log(data);
          toast({
            title: "Add Admin",
            description: "Admin Added Successfully"
          })
          getAdmins();
          setShowDialog(false);
        } else {
          const data = await response.json();
          console.log(data);
          console.log('Get Admins failed.');
          toast({
            title: "Add Admin",
            description: "Admin Added Failed"
          })
        }
    }

    const deleteAdmin = async (admin: { username: string, email: string, id: number }) => {
        const url = `http://127.0.0.1:5001/deleteadmin/${admin.id}`;
        
        const response = await fetch(url, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const data = await response.json();
            console.log(data);
            toast({
                title: "Admin Deleted",
                description: "Admin Deleted"
            })
            getAdmins();
            
        } else {
            const data = await response.json();
            console.log(data);
            console.log('Delete Admin failed.');
        }


    }

    return (
        <main>
            <Navigation />
            <div className="container">
                <div className="text-3xl font-extrabold my-8">
                    Admins
                </div>
                <div>
                <Table>
                    <TableCaption>List of Admins</TableCaption>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[100px]">Username</TableHead>
                            <TableHead className="w-[100px]">Role</TableHead>
                            <TableHead>Email</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {admins.map((admin: { username: string, email: string, id: number }, index: number) => (
                            <TableRow key={index}>
                                <TableCell className="font-medium">{admin.username}</TableCell>
                                <TableCell>Admin</TableCell>
                                <TableCell>{admin.email}</TableCell>
                                <TableCell>
                                    {userId !== admin.id && (
                                        <AlertDialog>
                                            <AlertDialogTrigger>Remove Admin</AlertDialogTrigger>
                                            <AlertDialogContent>
                                                <AlertDialogHeader>
                                                    <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                                                    <AlertDialogDescription>
                                                        This action cannot be undone. This will permanently remove this admin.
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction onClick={() => deleteAdmin(admin)}>Continue</AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>
                                    )}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
                </div>
                <div>
                    <div className="flex flex-row justify-between w-full">
                        <div>
                            <Dialog>
                                <DialogTrigger onClick={() => setShowDialog(true)}>Add Admin</DialogTrigger>
                                <DialogContent>
                                    <DialogHeader>
                                        <DialogTitle>Add Admin</DialogTitle>
                                        <DialogDescription>
                                            <Form {...form}>
                                                <form onSubmit={form.handleSubmit(addAdmin)} className="space-y-6">
                                                    <FormField
                                                        control={form.control}
                                                        name="username"
                                                        render={({ field }) => (
                                                            <FormItem>
                                                                <FormLabel>Username</FormLabel>
                                                                <FormControl>
                                                                    <Input placeholder="username" {...field} />
                                                                </FormControl>
                                                                <FormMessage />
                                                            </FormItem>
                                                        )}
                                                    />
                                                    <FormField
                                                        control={form.control}
                                                        name="email"
                                                        render={({ field }) => (
                                                            <FormItem>
                                                                <FormLabel>Email</FormLabel>
                                                                <FormControl>
                                                                    <Input placeholder="sample@gmail.com" {...field} />
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
                                                                    <Input placeholder="password" {...field} />
                                                                </FormControl>
                                                                <FormMessage />
                                                            </FormItem>
                                                        )}
                                                    />
                                                    <DialogFooter>
                                                        <DialogClose asChild>
                                                            <Button>Cancel</Button>
                                                        </DialogClose>
                                                        <Button type="submit">Save changes</Button>
                                                    </DialogFooter>
                                                </form>
                                            </Form>
                                        </DialogDescription>
                                    </DialogHeader>
                                </DialogContent>
                            </Dialog>
                        </div>
                    </div>
                </div>
            </div>
        </main>
    )

}