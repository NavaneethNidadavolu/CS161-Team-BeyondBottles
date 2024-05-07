"use client";

import React, { useEffect, useState } from 'react';
import {
    Card,
    CardContent,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card";

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
} from "@/components/ui/alert-dialog";

import { toast } from "@/components/ui/use-toast";

async function getFeed() {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/getblogs?type=before_rehab`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response;
}

async function deleteBlog(blog_id: any) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/deleteblog/${blog_id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response;
}

export default function BeforeRehab() {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(false); // Add the loading state

    useEffect(() => {
        async function fetchData() {
            setIsLoading(true); // Set loading state to true before fetching data
            const response = await getFeed();
            const jsonData = await response.json();
            setData(jsonData);
            setIsLoading(false); // Set loading state to false after data is fetched
        }

        fetchData();
    }, []);

    return (
        <div>
            <div className="my-8 w-96">
                {isLoading ? ( // Render a loading indicator or message if isLoading is true
                    <p>Loading...</p>
                ) : data.length > 0 ? (
                    data.map((post: any) => (
                        <Card key={post.id} className="mb-6">
                            <CardHeader>
                                <CardTitle>{post.content_header}</CardTitle>
                            </CardHeader>
                            <CardContent>
                                <div>{post.content}</div>
                                <div>
                                    <a href={post.link} target="_blank">
                                        Open
                                    </a>
                                </div>
                            </CardContent>
                            <CardFooter>
                                <div className="flex flex-row w-full">
                                    <AlertDialog>
                                        <AlertDialogTrigger>Delete</AlertDialogTrigger>
                                        <AlertDialogContent>
                                            <AlertDialogHeader>
                                                <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
                                                <AlertDialogDescription>
                                                    This action cannot be undone. This will permanently delete your account and remove your data from our servers.
                                                </AlertDialogDescription>
                                            </AlertDialogHeader>
                                            <AlertDialogFooter>
                                                <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                <AlertDialogAction
                                                    onClick={() => {
                                                        deleteBlog(post.id)
                                                            .then(async (data) => {
                                                                let response = await data.json();
                                                                console.log(response);
                                                                toast({ title: "Blog deleted successfully" });
                                                            })
                                                            .catch((error) => {
                                                                console.log("Error deleting Blog");
                                                                toast({ title: "Error occured while deleting comment." });
                                                            });
                                                    }}
                                                >
                                                    Continue
                                                </AlertDialogAction>
                                            </AlertDialogFooter>
                                        </AlertDialogContent>
                                    </AlertDialog>
                                </div>
                            </CardFooter>
                        </Card>
                    ))
                ) : (
                    <p>No Posts available</p>
                )}
            </div>
        </div>
    );
}