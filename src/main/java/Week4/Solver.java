import java.util.Comparator;
import java.util.LinkedList;

import edu.princeton.cs.algs4.MinPQ;

public class Solver {
    private MinPQ<Board> _queue;

    private Board _searchNode;

    private Board _previousSearchNode;

    private int _movesCount;

    private LinkedList<Board> _solutionBoards; 

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        _searchNode = initial;
        _previousSearchNode = null;
        _movesCount = 0;
        _queue = new MinPQ<>(6, new HammingComparer());
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
        _queue.insert(_searchNode);

        while (true) {
            _previousSearchNode = _searchNode;
            _searchNode = _queue.delMin();
            
            _solutionBoards.add(_searchNode);

            //StdOut.print("Next node: \n");
            //StdOut.print(_searchNode.toString());
            //StdOut.print("\n");

            ++_movesCount;
            
            if (_searchNode.isGoal()) {
                break;
            }

            for (Board neighbour : _searchNode.neighbors()) {
                if (!neighbour.equals(_previousSearchNode)) {
                    _queue.insert(neighbour);
                } 
            }
        }
    }

    private class HammingComparer implements Comparator<Board> {

        @Override
        public int compare(Board o1, Board o2) {
           int hammingFirst = o1.hamming() + Solver.this._movesCount;
           int hammingSecond = o2.hamming() + Solver.this._movesCount;

           if (hammingFirst > hammingSecond) 
                return 1;
            else if (hammingFirst < hammingSecond)
                return -1;
            else 
                return 0;
        }
    }
}
