
/*    java Board puzzle04.txt   */

import java.util.Arrays;

public class Board
{
    /**   short type because N < 128  */
    private short[] blocks; 
    
    /**  board dimension  */
    private int N; 
    
    private int zeroI;
    private int zeroJ;

    private int manhDist = -1;
    
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


    private Board(short[] blocks, int N, int zeroI, int zeroJ) 
    {
        this.blocks = Arrays.copyOf(blocks, blocks.length);
        this.N = N;
        this.zeroI = zeroI;
        this.zeroJ = zeroJ;
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
        /*if( manhDist != -1 )
        { 
            return manhDist;
        }*/
        
        manhDist = 0;
        for (int i = 0; i < blocks.length; i++) 
        {
            if (blocks[i] == 0) 
            {
                continue;
            }
            int deltaJ = (i % N ) - ( (blocks[i] - 1) % N );
            int deltaI = (i / N ) - ( (blocks[i] - 1) / N );

            manhDist = manhDist + Math.abs(deltaI) + Math.abs(deltaJ);
            
        }
        return manhDist;
    }
 
  
    /**
     * does this board equal y?
     */
    public boolean equals(Object y) 
    {
        if (y == this) return true;

        if (y == null) return false;

        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        
        if (this.N != that.N) return false;
        
        for (int i = 0; i < blocks.length; i++)
        {
            if( this.blocks[i] != that.blocks[i] )  return false;  
        }
        return true;
    }
  
    /**
     * @return a board obtained by exchanging 
     * two adjacent blocks in the same row.
     */
    public Board twin() 
    {
        int[][] twinB = new int[N][N];
        
        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
                twinB[i][j] = blocks[i * N + j];
            }
        }
        
        boolean ok = false;

        int row  = 0;
        int col1 = 0;
        int col2 = 1;

        while (!ok) 
        {
            if (col2 >= N)
            { 
              row++;
              col1 = 0;  
              col2 = 1;  
              continue;  
            }                       

            if( twinB[row][col1] == 0 || twinB[row][col2] == 0 )
            {
              col1++;  
              col2++;
              continue;  
            }
            
            int tmp = twinB[row][col1];
            twinB[row][col1] = twinB[row][col2];
            twinB[row][col2] = tmp; 
            
            ok = true;
        }

        return new Board(twinB);
    }
  
    /**
     * @return all neighboring boards
     */
    public Iterable<Board> neighbors() 
    {
        Stack<Board> boardsStack = new Stack<Board>();
        
        short tmpBlock;
        int indexFirst,indexSecond;
        int deltaM,tmp;

        if(zeroI!=0) 
        {
            indexFirst  = N * (zeroI - 1) + zeroJ ;
            indexSecond = N * zeroI + zeroJ ;
            tmpBlock  = blocks[indexFirst];
            
            tmp = zeroI - (tmpBlock - 1) / N ;
            deltaM = Math.abs(tmp)-Math.abs(tmp-1);
            
            /*  swap  */
            blocks[indexFirst] = 0;
            blocks[indexSecond] = tmpBlock;
            /*  push  */
            boardsStack.push(new Board(this.blocks, this.N, zeroI - 1, zeroJ));
            /*  restore  */
            blocks[indexFirst] = tmpBlock;
            blocks[indexSecond] = 0;
        }

        if(zeroI!=N-1) 
        {
            indexFirst  = N * (zeroI + 1) + zeroJ ;
            indexSecond = N * zeroI + zeroJ ;
            tmpBlock  = blocks[indexFirst];

            tmp = zeroI - (tmpBlock - 1) / N ;
            deltaM = Math.abs(tmp)-Math.abs(tmp+1);
            
            /*  swap  */
            blocks[indexFirst] = 0;
            blocks[indexSecond] = tmpBlock;
            /*  push  */
            boardsStack.push(new Board(this.blocks, this.N, zeroI + 1, zeroJ));
            /*  restore  */
            blocks[indexFirst] = tmpBlock;
            blocks[indexSecond] = 0;
        }
        
        if(zeroJ!=0) 
        {
            indexFirst  = N * zeroI + ( zeroJ - 1 ) ;
            indexSecond = N * zeroI + zeroJ ;
            tmpBlock  = blocks[indexFirst];

            tmp = zeroJ - (tmpBlock - 1) % N ;
            deltaM = Math.abs(tmp)-Math.abs(tmp-1);
                        
            /*  swap  */
            blocks[indexFirst] = 0;
            blocks[indexSecond] = tmpBlock;
            /*  push  */
            boardsStack.push(new Board(this.blocks, this.N, zeroI, zeroJ - 1));
            /*  restore  */
            blocks[indexFirst] = tmpBlock;
            blocks[indexSecond] = 0;
        }
        
        if(zeroJ!=N-1) 
        {
            indexFirst  = N * zeroI + ( zeroJ + 1 ) ;
            indexSecond = N * zeroI + zeroJ ;
            tmpBlock  = blocks[indexFirst];

            tmp = zeroJ - (tmpBlock - 1) % N ;
            deltaM = Math.abs(tmp)-Math.abs(tmp+1);

            /*  swap  */
            blocks[indexFirst] = 0;
            blocks[indexSecond] = tmpBlock;
            /*  push  */
            boardsStack.push(new Board(this.blocks, this.N, zeroI, zeroJ + 1));
            /*  restore  */
            blocks[indexFirst] = tmpBlock;
            blocks[indexSecond] = 0;
        }
        
        return boardsStack;
    }




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

            Board twin = testBoard.twin();
            
            StdOut.println( "Orig: \n"+testBoard.toString() );
            StdOut.println( "Twin: \n"+twin.toString() );

     }
     
} 
