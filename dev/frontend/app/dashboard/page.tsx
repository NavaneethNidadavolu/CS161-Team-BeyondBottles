"use client"

import DailyFeed from "@/components/dailyFeed";
import Navigation from "@/components/navigation/navigation";
import { ScrollArea } from "@/components/ui/scroll-area";

import Link from "next/link";

import { useAuth } from "../auth";
import { useState } from "react";


export default function Dashboard() {

    useAuth();

    const [pageView, setPageView] = useState("dailyFeed");

    return (
        <main>
            <Navigation />
            <div className="flex flex-row min-h-screen">
                <aside className="mx-2 p-16">
                    <ScrollArea className="rounded-md border p-4 w-56">
                        <p className="text-2xl font-semibold">menu</p>
                        <div className="my-6">
                            <button onClick={() => {
                                setPageView("dailyFeed");
                            }}>Daily Feed</button>
                        </div>
                        <div className="my-6">
                            <button onClick={() => {
                                setPageView("beforeRehab");
                            }}>Before Rehab</button>
                        </div>
                        <div className="my-6">
                            <button onClick={() => {
                                setPageView("duringRehab");
                            }}>During Rehab</button>
                        </div>
                        <div className="my-6">
                            <button onClick={() => {
                                setPageView("afterRehab");
                            }}>After Rehab</button>
                        </div>
                        <div className="my-6">
                            <button onClick={() => {
                                setPageView("familyHelp");
                            }}>Family Help</button>
                        </div>
                        <div className="my-6">
                            <button onClick={() => {
                                setPageView("leaderboard");
                            }}>Leaderboard</button>
                        </div>
                    </ScrollArea>
                </aside>
                <div className="flex flex-grow flex-col items-center p-10">
                    {pageView === "dailyFeed" && <DailyFeed />}
                    {pageView === "beforeRehab" && <DailyFeed />}
                    {pageView === "duringRehab" && <DailyFeed />}
                    {pageView === "afterRehab" && <DailyFeed />}
                    {pageView === "familyHelp" && <DailyFeed />}
                    {pageView === "leaderboard" && <DailyFeed />}
                </div>
            </div>
        </main>
    );
}