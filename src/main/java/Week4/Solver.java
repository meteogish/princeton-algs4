import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private SearchNode _solutionSearchNode;
    private int _movesCount;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        _movesCount = 0;
        boolean isSolvable = initial.isSolvable();
        if (isSolvable) {
            ProcessSolution(initial);
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return _solutionSearchNode != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return _movesCount - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (_solutionSearchNode != null) {
            SearchNode tempNode = _solutionSearchNode;
            Stack<Board> boards = new Stack<>();
            
            while (tempNode != null) {
                boards.push(tempNode.board);
                tempNode = tempNode._previous;
            } 
            return boards;
        }
        return null;
    }

    private void ProcessSolution(Board initial) {
        MinPQ<SearchNode> _queue = new MinPQ<>();
        SearchNode searchNode = new SearchNode(initial, initial.manhattan() + _movesCount, null);
        _queue.insert(searchNode);

        while (true) {
            searchNode = _queue.delMin();
            
            StdOut.print("Next node: \n");
            StdOut.print(searchNode.board.toString());
            StdOut.print("\n");

            ++_movesCount;
            
            if (searchNode.board.isGoal()) {
                _solutionSearchNode = searchNode;
                break;
            }
            
            for (Board neighbour : searchNode.board.neighbors()) {
                if (searchNode._previous != null) {
                    if (!neighbour.equals(searchNode._previous.board)) {
                        _queue.insert(new SearchNode(neighbour, neighbour.manhattan() + _movesCount, searchNode));
                    }
                }
                else {
                    _queue.insert(new SearchNode(neighbour, neighbour.manhattan() + _movesCount, searchNode));
                }
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int priority;
        private SearchNode _previous;

        public SearchNode(Board board, int priority, SearchNode previous) {
            super();
            this.board = board;
            this.priority = priority;
            this._previous = previous;
        }

        @Override
        public int compareTo(Solver.SearchNode right) {
            if (priority > right.priority) {
                return 1;
            } 
            else if (priority < right.priority) {
                return -1;
            }
            else {
                return 0;
            }
        }

    }
}
