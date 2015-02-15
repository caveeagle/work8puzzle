
/*    java Board puzzle04.txt   */


class Board
{
    /**   short type because N < 128  */
    private short[] blocks; 
    
    /**  board dimension  */
    private int N; 
    
    private int zeroI;
    private int zeroJ;
    
    /**
    *    construct a board from an N-by-N array of blocks;
    *    (where blocks[i][j] = block in row i, column j)
    *   @param blocks
    */
    public Board(int[][] blocks)
    {
        N = blocks.length;
        
        this.blocks = new short[N * N];
        
        for (int i = 0; i < blocks.length; i++) 
        {
            int[] row = blocks[i];
            for (int j = 0; j < row.length; j++) 
            {
                this.blocks[ N * i + j ] = (short) blocks[i][j];
                
                // Find zero element position
                if (blocks[i][j] == 0) 
                {
                    zeroI = i;
                    zeroJ = j;
                }
            }
        }

        
    }
    
    /**
     * @return board dimension N.
     */
    public int dimension()
    {
        return N;
    }
     
    /**
     * @return number of blocks out of place.
     */
    public int hamming() 
    {
        int hamming = 0;
        for (int i = 0; i < blocks.length; i++) 
        {
            if (blocks[i] == 0) 
            {
                continue;
            }
            if (blocks[i] != (i + 1)) 
            {
                hamming++;
            }
        }
        return hamming;
    }     
     
    /**
     * @return is this board the goal board?
     */
    public boolean isGoal() 
    {
        int r = hamming();
        return (r == 0);
    }
     
    /**
     * @return string representation of the board 
     * (in the output format specified below)
     */


    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
                s.append(String.format("%2d ", blocks[ N * i + j ]));
            }
            s.append("\n");
        }
        
        /* HINT: if puzzle is solvable, add additional invisible symbol */
        
        /*     It need for parameter passing from private method        */
        
        if( this.isSolvable() )
        {
            s.append("\n");
        }
        return s.toString();
    }

    /**
     * <b>(ONLY PRIVATE METHOD)</b>
     * @return is the board solvable? 
     */
    private boolean isSolvable()
    {
        int sum = 0;
        for (int i = 0; i < blocks.length-1; i++)    
        {
          short current = blocks[i];
          
          if (current == 0) continue; 
          
          for (int j = i+1; j < blocks.length; j++)
          {
              if (blocks[j] != 0 && current > blocks[j]) 
              {
                    sum++;
              }
          }    
        }
        
        if (N % 2 == 0)
        {
            sum += (zeroI+1);
        }
        
        if (sum % 2 == 0) 
        {
            return true;
        } 
        else 
        {
            return false;
        }
    }            
                     
  /**
    * @return sum of Manhattan distances between blocks and goal.
    */
    public int manhattan() 
    {
        int sum = 0;
        for (int i = 0; i < blocks.length; i++) 
        {
            if (blocks[i] == 0) 
            {
                continue;
            }
            
            int dRow = Math.abs(  (i % N ) - (blocks[i] - 1) % N );
            int dCol = Math.abs(  (i / N ) - (blocks[i] - 1) / N );
            
            sum += row + col;
        }
        return man;
    }
 
  
  
  
    //public Board twin()                    // a boadr that is obtained by exchanging two adjacent blocks in the same row
    //public boolean equals(Object y)        // does this board equal y?
    //public Iterable<Board> neighbors()     // all neighboring boards




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
            
            StdOut.println( "Is solvable: "+testBoard.isSolvable() );

     }
     
} 
