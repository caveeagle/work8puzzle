

public class Solver 
{
    private Board initBoard;
    
    
    
    /** @return is the initial board solvable? */
    public boolean isSolvable()
    {
       String t1 = initBoard.toString();
       String t2 = t1.trim();
       int a1 = t1.length();
       int a2 = t2.length();
       
       System.out.println(a1+" - "+a2);
       
       return false;
    }


    /** find a solution to the initial board (using the A* algorithm) */
    public Solver(Board initial)
    { 
      initBoard = initial;  
    }
    
                   
    //public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    //public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable

     public static void main (String[] args)
     {
            In in = new In(args[0]);
            int N = in.readInt();
            int[][] tiles = new int[N][N];
            for (int i = 0; i < N; i++) 
            {
                for (int j = 0; j < N; j++) 
                {
                    tiles[i][j] = in.readInt();
                }
            }
            Board testBoard = new Board(tiles);
            Solver solver = new Solver(testBoard);
            
            StdOut.println( "Is solvable: "+solver.isSolvable() );
     }
}

