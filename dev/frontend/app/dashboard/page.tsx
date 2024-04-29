"use client"

import DailyFeed from "@/components/dailyFeed";
import Navigation from "@/components/navigation/navigation";
import { ScrollArea } from "@/components/ui/scroll-area";

import { useAuth } from "../auth";
import { useState } from "react";

import BeforeRehab from "@/components/beforeRehab";
import DuringRehab from "@/components/duringRehab";
import AfterRehab from "@/components/afterRehab";
import FamilyHelp from "@/components/familyHelp";
import Leaderboard from "@/components/leaderboard";


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
                <div className=" p-10">
                    {pageView == "dailyFeed" && <h1 className="text-2xl font-bold">Daily Feed</h1>}
                    {pageView == "beforeRehab" && <h1 className="text-2xl font-bold">Before Rehab</h1>}
                    {pageView == "duringRehab" && <h1 className="text-2xl font-bold">During Rehab</h1>}
                    {pageView == "afterRehab" && <h1 className="text-2xl font-bold">After Rehab</h1>}
                    {pageView == "familyHelp" && <h1 className="text-2xl font-bold">Family Help</h1>}
                    {pageView == "leaderboard" && <h1 className="text-2xl font-bold">Leaderboard</h1>}
                    <div className="flex flex-grow flex-col items-center">
                        {pageView == "dailyFeed" && <DailyFeed />}
                        {pageView == "beforeRehab" && <BeforeRehab />}
                        {pageView == "duringRehab" && <DuringRehab />}
                        {pageView == "afterRehab" && <AfterRehab />}
                        {pageView == "familyHelp" && <FamilyHelp />}
                        {pageView == "leaderboard" && <Leaderboard />}
                    </div>
                </div>
            </div>
        </main>
    );
}