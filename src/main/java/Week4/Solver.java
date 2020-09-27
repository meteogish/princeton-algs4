import java.util.Comparator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private SearchNode _searchNode;

    private SearchNode _previousSearchNode;

    private int _movesCount;

    private LinkedList<Board> _solutionBoards; 

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        _movesCount = 0;
        _searchNode = new SearchNode(initial, initial.manhattan() + _movesCount);
        _previousSearchNode = null;
        _solutionBoards = new LinkedList<>();
        ProcessSolution();
        
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return _movesCount - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        LinkedList<Board> l = new LinkedList<>(_solutionBoards);
        return l;
    }

    private void ProcessSolution() {
        MinPQ<SearchNode> _queue = new MinPQ<>(6);
        _queue.insert(_searchNode);

        while (true) {
            _previousSearchNode = _searchNode;
            _searchNode = _queue.delMin();
            
            _solutionBoards.add(_searchNode.board);

            //StdOut.print("Next node: \n");
            //StdOut.print(_searchNode.toString());
            //StdOut.print("\n");

            ++_movesCount;
            
            if (_searchNode.board.isGoal()) {
                break;
            }

            for (Board neighbour : _searchNode.board.neighbors()) {
                if (!neighbour.equals(_previousSearchNode.board)) {
                    _queue.insert(new SearchNode(neighbour, neighbour.manhattan() + _movesCount));
                }
            }
        }
    }

    private class SearchNode implements Comparable<SearchNode> {
        private Board board;
        private int priority;

        public SearchNode(Board board, int priority) {
            super();
            this.board = board;
            this.priority = priority;
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
