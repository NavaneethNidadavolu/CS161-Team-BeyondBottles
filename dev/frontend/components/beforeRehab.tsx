
import {
    Card,
    CardContent,
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

import { toast } from "@/components/ui/use-toast";

async function getFeed() {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/getblogs?type=before_rehab`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response
}

async function deleteBlog(blog_id: number) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/deleteblog/${blog_id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response
}

export default async function BeforeRehab() {

    const data = await (await getFeed()).json()

    console.log(data)

    return (
        <div>
            <div className="my-8 w-96">
                {
                    data.length > 0 ? data.map((post: any) => {
                        return (
                            <Card key={post.id} className="mb-6">
                                <CardHeader>
                                    <CardTitle>{post.content_header}</CardTitle>
                                </CardHeader>
                                <CardContent>
                                    <div>
                                        {post.content}
                                    </div>
                                    <div>
                                        <a href={post.link} target="_blank" >Open</a>
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
                                                        This action cannot be undone. This will permanently delete your account
                                                        and remove your data from our servers.
                                                    </AlertDialogDescription>
                                                </AlertDialogHeader>
                                                <AlertDialogFooter>
                                                    <AlertDialogCancel>Cancel</AlertDialogCancel>
                                                    <AlertDialogAction onClick={() => {
                                                        deleteBlog(post.id).then(async (data) => {
                                                            let response = await data.json();
                                                            console.log(response);
                                                            toast({ title: "Blog deleted successfully" })
                                                        }).catch((error) => {
                                                            console.log("Error deleting Blog");
                                                            toast({ title: "Error occured while deleting comment." })
                                                        });
                                                    }} >Continue</AlertDialogAction>
                                                </AlertDialogFooter>
                                            </AlertDialogContent>
                                        </AlertDialog>
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