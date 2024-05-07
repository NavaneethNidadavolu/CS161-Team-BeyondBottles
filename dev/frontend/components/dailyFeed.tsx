"use client";
import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle, } from "@/components/ui/card";
import { Dialog, DialogTrigger } from "@/components/ui/dialog";
import OpenPost from "./openPost";
import './css/dailyFeed.css';

async function getDailyFeed() {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/questions`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });
    return response.json();
}

export default function DailyFeed() {
    const [data, setData] = useState([]);
    const [isLoading, setIsLoading] = useState(false); // Add the loading state

    useEffect(() => {
        async function fetchData() {
            setIsLoading(true); // Set loading state to true before fetching data
            const fetchedData = await getDailyFeed();
            setData(fetchedData);
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
                                <CardTitle>{post.username}</CardTitle>
                                <CardDescription>{post.time}</CardDescription>
                            </CardHeader>
                            <CardContent>
                                <div>
                                    <p>{post.question}</p>
                                    <div className="text-sm mt-4">
                                        <p className="mb-1">{post.upvotes} Upvotes</p>
                                        <p>{post.comments} Comments</p>
                                    </div>
                                </div>
                            </CardContent>
                            <CardFooter>
                                <div className="flex flex-row w-full">
                                    <Dialog>
                                        <DialogTrigger>
                                            <span className="open-button">Open</span>
                                        </DialogTrigger>
                                        <OpenPost post={post} />
                                    </Dialog>
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