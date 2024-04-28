import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"

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
    DialogTrigger,
} from "@/components/ui/dialog"

import { toast } from "@/components/ui/use-toast";
import OpenPost from "./openPost";

async function getDailyFeed() {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/questions`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response
}

export default async function DailyFeed() {

    const data = await (await getDailyFeed()).json()

    console.log(data)

    return (
        <div>
            <div className="my-8 w-96">
                {
                    data.length > 0 ? data.map((post: any) => {
                        return (
                            <Card id={post.id} className="mb-6">
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
                                            <DialogTrigger>Open</DialogTrigger>
                                            <OpenPost post={post} />
                                        </Dialog>
                                    </div>
                                </CardFooter>
                            </Card>
                        )
                    }) : <p>No Posts available</p>
                }
            </div>
        </div >
    );
}