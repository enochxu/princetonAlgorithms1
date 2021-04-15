import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import java.util.Arrays;

public class BaseballElimination {
    private final int numTeams;
    private final String[] teams;
    private final int[] wins;
    private final int[] losses;
    private final int[] remaining;
    private final int[][] gamesLeft;

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In scores = new In(filename);
        
        numTeams = scores.readInt();
        teams = new String[numTeams];
        wins = new int[numTeams];
        losses = new int[numTeams];
        remaining = new int[numTeams];
        gamesLeft = new int[numTeams][numTeams];

        for (int i = 0; i < numTeams; i++) {
            teams[i] = scores.readString();
            wins[i] = scores.readInt();
            losses[i] = scores.readInt();
            remaining[i] = scores.readInt();
            for (int j = 0; j < numTeams; j++)
                gamesLeft[i][j] = scores.readInt();
            // System.out.println(teams[i] + " " + wins[i] + " " + losses[i] + " " + remaining[i] + " ");
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return Arrays.asList(teams);
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[getTeam(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return losses[getTeam(team)];
    }              

    // number of remaining games for given team
    public int remaining(String team) {
        return remaining[getTeam(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        int ind1 = getTeam(team1);
        int ind2 = getTeam(team2);
        return gamesLeft[ind1][ind2];
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team) {
        int index = getTeam(team);

        FordFulkerson maxFlow = getMaxFlow(index);   
        return calcElimination(maxFlow, index);
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        int index = getTeam(team);
        // System.out.print(team + " corresponds to " + index);
        
        FordFulkerson maxFlow = getMaxFlow(index);

        if (maxFlow != null && !calcElimination(maxFlow, index))
            return null;
        
        int games = (numTeams - 2) * (numTeams - 1) / 2;
        int[] teamVertices = new int[numTeams];
        int teamVertex = 1;        
        for (int i = 0; i < numTeams; i++) {
            if (i != index) {
                teamVertices[i] = games + teamVertex;
                teamVertex++;
            }                
        }
        // int[] newTeam = new int[numTeams - 1];
        // int vertex = 0;
        // for (int i = 0; i < numTeams; i++) {
        //     if (i != index) {
        //         teamVertices[vertex] = team[i];
        //         ertex++;
        //     }                
        // }

        Queue<String> certificate = new Queue<>();
        for (int i = 0; i < numTeams; i++) {
            if (i != index) {
                if (maxFlow == null
                    && wins[i] > wins[index] + remaining[index])
                    certificate.enqueue(teams[i]);
                if (maxFlow != null && maxFlow.inCut(teamVertices[i])) {
                    certificate.enqueue(teams[i]);
                    // System.out.println("index " + index + "i " + i);
                }
                    
            }
        }
        
        return certificate;
    }

    private FordFulkerson getMaxFlow(int index) {
        // trivial eliminaion;
        for (int i = 0; i < numTeams; i++) {
            if (wins[index] + remaining[index] < wins[i]) {
                // System.out.println("wins " + wins[index] + " remaining " + remaining[index] + "comp: " + wins[i]);
                // System.out.println("helo");
                return null;
            }
                
        }   

        int games = (numTeams - 2) * (numTeams - 1) / 2;
        // System.out.println("game vertices " + gameVertices);
        int[] teamVertices = new int[numTeams];
        // int[] gameVertices = new int[games];
        // for (int i = 0; i < games; i++)


        int teamVertex = 1;
        for (int i = 0; i < numTeams; i++) {
            if (i != index) {
                teamVertices[i] = games + teamVertex;
                teamVertex++;
            }                
        }

        int total = games + numTeams + 1;
        // System.out.println("toatal " + total);
        FlowNetwork gameNetwork = new FlowNetwork(total);
        
        int gameVertex = 1;
        for (int i = 0; i < numTeams; i++) {
            if (i != index) {
                for (int j = i + 1; j < numTeams; j++) {
                    if (j != index) {
                        // System.out.println("v: " + gameVertex + " gamesleft" + gamesLeft[i][j]);
                        gameNetwork.addEdge(new FlowEdge(0, gameVertex, gamesLeft[i][j]));
                        // System.out.println("v2: " + games + i);
                        gameNetwork.addEdge(new FlowEdge(gameVertex, teamVertices[i], 
                                                        Double.POSITIVE_INFINITY));
                        // System.out.println("v3: " + gameVertices + j);
                        gameNetwork.addEdge(new FlowEdge(gameVertex, teamVertices[j], 
                                                        Double.POSITIVE_INFINITY));
                        gameVertex++;
                    }
                }
                // 3, 3??
                //numTeams = 4
                // if (i + 1 < numTeams)
                // System.out.println("vertices " + gameVertices);

                // System.out.println("index " + i);
                // System.out.println("max allowed " + (wins[index] + remaining[index] - wins[i]));
            }
        }
        int vertex = 1;
        for (int i = 0; i < numTeams; i++) {
            if (i != index) {
                gameNetwork.addEdge(new FlowEdge(games + vertex, total - 1, 
                                    wins[index] + remaining[index] - wins[i]));
                vertex++;
            }
        }
        // System.out.println(gameNetwork.toString());
        // for (int i = 0; i < numTeams; i++) {
        //     if (i != index)
        //     gameNetwork.addEdge(new FlowEdge(gameVertices + i, total - 1, 
        //                                     wins[index] + remaining[index] - wins[i]));
        // }
        FordFulkerson maxFlow = new FordFulkerson(gameNetwork, 0, total - 1);
        return maxFlow;
    }

    private boolean calcElimination(FordFulkerson maxFlow, int index) {  
        if (maxFlow == null)
            return true;        
        int totalCap = 0;
        for (int i = 0; i < numTeams; i++) {
            if (i != index) {
                for (int j = i + 1; j < numTeams; j++) {
                    if (j != index) {
                        totalCap += gamesLeft[i][j];
                    }
                }
            }
            
        }
        if (totalCap == maxFlow.value())
            return false;           
        else
            return true;            
    }

    private int getTeam(String team) {
        if (team == null)
            throw new IllegalArgumentException("Argument is null.");

        for (int i = 0; i < teams.length; i++) {
            if (team.equals(teams[i])) {
                return i;
            }
        }
        throw new IllegalArgumentException("Team not in set.");
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}