"use client";

import {
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle
} from "@/components/ui/dialog"

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
import { useEffect, useState } from "react";
import { toast } from "./ui/use-toast";

// [
//     {
//         "comment": "Comment from me !!",
//         "time": "7 days ago",
//         "username": "vathsav"
//     },
//     {
//         "comment": "Sample comment from leelu",
//         "time": "8 days ago",
//         "username": "leelu"
//     },
//     {
//         "comment": "This is my first comment",
//         "time": "13 days ago",
//         "username": "likhithnemani"
//     }
// ]

interface Comment {
    "id": number,
    "comment": string,
    "time": string,
    "username": string
}

async function getComments(post_id: number) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/getcomments?questionid=${post_id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response
}


async function deletePost(post_id: number) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/deletequestion/${post_id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response
}

async function deleteComment(comment_id: number) {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/deletecomment/${comment_id}`, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response
}

export default function OpenPost({ post }: { post: any }) {

    const [comments, setComments] = useState<[Comment] | []>([])
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        getComments(post.id).then(async (data) => {
            let response: [Comment] = await data.json();
            setComments(response);
            setLoading(false);
        });
    }, [])

    return (
        <div>
            {loading ? <></> :
                <div>
                    <DialogContent>
                        <DialogHeader>
                            <DialogTitle className="font-extrabold">{post.username}</DialogTitle>
                            <DialogDescription>
                                <div className="text-xl font-semibold my-6">
                                    {post.question}
                                </div>
                                <div className="font-semibold text-lg my-4">
                                    Comments
                                </div>
                                {comments.length == 0 ? <div className="text-md my-4">No comments</div> :
                                    comments.map((comment: any) => {
                                        return (
                                            <div className="my-4">
                                                <p><span className="text-md font-bold">{comment.username}</span> - {comment.time}</p>
                                                <p>{comment.comment}</p>
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
                                                                deleteComment(comment.id).then(async (data) => {
                                                                    let response = await data.json();
                                                                    console.log(response);
                                                                    toast({ title: "Comment deleted successfully" })
                                                                    getComments(post.id).then(async (data) => {
                                                                        let response: [Comment] = await data.json();
                                                                        setComments(response);
                                                                        setLoading(false);
                                                                    });
                                                                }).catch((error) => {
                                                                    console.log("Error deleting Comment");
                                                                    toast({ title: "Error occured while deleting comment." })
                                                                });
                                                            }} >Continue</AlertDialogAction>
                                                        </AlertDialogFooter>
                                                    </AlertDialogContent>
                                                </AlertDialog>
                                            </div>
                                        )
                                    })}
                                <AlertDialog>
                                    <AlertDialogTrigger>Delete Post</AlertDialogTrigger>
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
                                                deletePost(post.id).then(async (data) => {
                                                    let response = await data.json();
                                                    console.log(response);
                                                    toast({ title: "Post deleted successfully" })
                                                }).catch((error) => {
                                                    console.log("Error deleting post");
                                                    toast({ title: "Error occured while deleting post." })
                                                });
                                            }} >Continue</AlertDialogAction>
                                        </AlertDialogFooter>
                                    </AlertDialogContent>
                                </AlertDialog>
                            </DialogDescription>
                        </DialogHeader>
                    </DialogContent>
                </div>
            }
        </div>
    )

}



// <AlertDialog>
// <AlertDialogTrigger>Delete</AlertDialogTrigger>
// <AlertDialogContent>
//     <AlertDialogHeader>
//         <AlertDialogTitle>Are you absolutely sure?</AlertDialogTitle>
//         <AlertDialogDescription>
//             This action cannot be undone. This will permanently delete your account
//             and remove your data from our servers.
//         </AlertDialogDescription>
//     </AlertDialogHeader>
//     <AlertDialogFooter>
//         <AlertDialogCancel>Cancel</AlertDialogCancel>
//         <AlertDialogAction>Continue</AlertDialogAction>
//     </AlertDialogFooter>
// </AlertDialogContent>
// </AlertDialog>