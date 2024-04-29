import {
    Table,
    TableBody,
    TableCaption,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"

async function getLeaderboard() {
    const response = await fetch(`${process.env.NEXT_PUBLIC_BASE_API_URL}/leaderboard`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
        cache: 'no-store'
    });

    return response
}

export default async function Leaderboard() {

    const data = await (await getLeaderboard()).json()

    console.log(data)

    return (
        <div>
            <Table>
                <TableHeader>
                    <TableRow>
                        <TableHead className="w-[100px]">Rank</TableHead>
                        <TableHead className="w-[100px]">Username</TableHead>
                        <TableHead className="w-[100px]">score</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody>
                    {data.map((user: any) => (
                        <TableRow key={user.id}>
                            <TableCell>{user.rank}</TableCell>
                            <TableCell className="font-medium">{user.username}</TableCell>
                            <TableCell>{user.score}</TableCell>
                        </TableRow>
                    ))}
                </TableBody>
            </Table>
        </div >
    );
}