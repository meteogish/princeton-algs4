import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class BoardTest {
    public static void main(String[] args) {
        // create initial board from file
        
        //In in = new In("/Users/yevhenii/Documents/Repositories/princeton-algs4/tile3_incorrect2.txt");
        In in = new In("https://coursera.cs.princeton.edu/algs4/assignments/8puzzle/files/puzzle04.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);
        
        StdOut.print(initial.toString());
        StdOut.print("\n");


        StdOut.print("manhattan = " + initial.manhattan());
        StdOut.print("\n");
        StdOut.print("haming = " + initial.hamming());
        StdOut.print("\nisGoal = " + initial.isGoal());
        StdOut.print("\nneighbors\n");

        for(Board nei : initial.neighbors()) {
            StdOut.print(nei.toString());
            StdOut.print("\n");
        }

        StdOut.print("Its Working\n");
	
	     // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }   
}
