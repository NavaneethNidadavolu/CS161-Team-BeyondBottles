"use client"

import DailyFeed from "@/components/dailyFeed";
import Navigation from "@/components/navigation/navigation";
import { ScrollArea } from "@/components/ui/scroll-area";

import Link from "next/link";


export default function Dashboard() {

    return (
        <main>
            <Navigation />
            <div className="flex flex-row min-h-screen">
                <aside className="mx-2 p-16">
                    <ScrollArea className="rounded-md border p-4 w-56">
                        <p className="text-2xl font-semibold">menu</p>
                        <div className="my-6">
                            <Link href="/"><button>Daily Feed</button></Link>
                        </div>
                        <div className="my-6">
                            <Link href="/"><button>Before Rehab</button></Link>
                        </div>
                        <div className="my-6">
                            <Link href="/"><button>During Rehab</button></Link>
                        </div>
                        <div className="my-6">
                            <Link href="/"><button>After Rehab</button></Link>
                        </div>
                        <div className="my-6">
                            <Link href="/"><button>Family Help</button></Link>
                        </div>
                        <div className="my-6">
                            <Link href="/"><button>Leaderboard</button></Link>
                        </div>
                    </ScrollArea>
                </aside>
                <div className="flex flex-grow flex-col items-center p-10">
                    <div>
                        <DailyFeed />
                    </div>
                </div>
            </div>
        </main>
    );
}