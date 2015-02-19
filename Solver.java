import java.util.Comparator;

public class Solver {
    
    private MinPQ<SearchNode> pq;
    
    private SearchNode goal = null;
    
    private boolean solvable = true;
    
    private final Comparator<SearchNode> byHamming = new ByHamming();
    private final Comparator<SearchNode> byManhattan = new ByManhattan();
    
    /**
     * find a solution to the initial board (using the A* algorithm)
     * @param initial
     */
    public Solver(Board initial) 
    {
        
        /* Determine, is the initial board solvable? (with hint) */
        String st = initial.toString();
        int dif =  st.length() - st.trim().length(); // 2 if false, and 3 if solvable
        if ( dif > 2 ) {
                solvable = true;
        }else{
                solvable = false;
        }
        
        
        pq = new MinPQ<SearchNode>(byManhattan);
        
        SearchNode initNode = new SearchNode(initial, 0, null);
        pq.insert(initNode);
        
        boolean solved = false;
        while (!solved) 
        {
            solved = solve();
        }
    }

    private class ByHamming implements Comparator<SearchNode> 
    {
        public int compare(SearchNode n1, SearchNode n2) 
        {
            return n1.board.hamming() - n2.board.hamming();
        }
    }
    
    private class ByManhattan implements Comparator<SearchNode> 
    {
        public int compare(SearchNode n1, SearchNode n2) 
        {
            int D = n1.board.manhattan() + n1.moves  - n2.board.manhattan() - n2.moves;
            return D;
        }
    }
    
    private class SearchNode 
    {
        private Board board;
        private int moves;
        private SearchNode prev;
        
        private int hash = 0;
        
        public SearchNode(Board b, int m, SearchNode p) 
        {
            board = b;
            moves = m;
            prev = p;
        }
        
        public boolean equals(Object y) {
            if (y == this) return true;
            if (y == null) return false;
            if (y.getClass() != this.getClass()) return false;
            
            SearchNode that = (SearchNode) y;
            if (that.board.equals(board)) {
                return true;
            }
            return false;
        }
        
        public int hashCode() {
            if (hash != 0) {
                return hash;
            }
            hash = board.toString().hashCode();
            return hash;
        }
    }

    private boolean solve() 
    {
        if(!solvable) { return true; }
        
        
        // solve pq
        SearchNode node = pq.delMin();
        if (node.board.isGoal()) {
            goal = node;
            return true;
        }

        for (Board board : node.board.neighbors()) {
            SearchNode n = new SearchNode(board, node.moves + 1, node);
            if (n.equals(node.prev)) {
                continue;
            }

            pq.insert(n);
        }
        
        return false;
    }
    
    /**
     * @return is the initial board solvable?
     */
    public boolean isSolvable() 
    {
        return solvable;
    }
    
    /**
     * @return min number of moves to solve initial board; -1 if no solution
     */
    public int moves() 
    {
        if (!solvable) 
        {
            return -1;
        }
        return goal.moves;
    }
    
    /**
     * @return sequence of boards in a shortest solution; null if no solution
     */
    public Iterable<Board> solution() {
        
        if (goal == null) 
        {
            return null;
        }
        
        Stack<Board> stack = new Stack<Board>();
        SearchNode node = goal;
        stack.push(node.board);
        while (node.prev != null) {
            stack.push(node.prev.board);
            node = node.prev;
        }
        
        return stack;
    }
    
    /**
     * solve a slider puzzle 
     * @param args
     */
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
        {
            StdOut.println("No solution possible");
        }
        else 
        {
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }
}
