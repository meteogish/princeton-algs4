import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private SearchNode _solutionSearchNode;
    private int _movesCount;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        _movesCount = 0;
        processSolution(initial);
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

    private void processSolution(Board initial) {
        MinPQ<SearchNode> queue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();
        
        SearchNode searchNode = new SearchNode(initial, _movesCount, null);
        queue.insert(searchNode);
        
        SearchNode twinSearchNode = new SearchNode(initial.twin(), _movesCount, null);
        twinQueue.insert(twinSearchNode);

        while (true) {
            searchNode = queue.delMin();
            twinSearchNode = twinQueue.delMin();

            ++_movesCount;
            
            if (searchNode.board.isGoal()) {
                _solutionSearchNode = searchNode;
                break;
            }

            if (twinSearchNode.board.isGoal()) {
                break;
            }
            
            addNeighBours(searchNode, queue, _movesCount);
            addNeighBours(twinSearchNode, twinQueue, _movesCount);
        }
    }

    private void addNeighBours(SearchNode searchNode, MinPQ<SearchNode> queue, int movesCount) {
        for (Board neighbour : searchNode.board.neighbors()) {
            if (searchNode._previous != null) {
                if (!neighbour.equals(searchNode._previous.board)) {
                    queue.insert(new SearchNode(neighbour, movesCount, searchNode));
                }
            } else {
                queue.insert(new SearchNode(neighbour, movesCount, searchNode));
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int priority;
        private SearchNode _previous;

        public SearchNode(Board board, int movesCount, SearchNode previous) {
            super();
            this.board = board;
            this.priority = board.manhattan() + movesCount;
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
