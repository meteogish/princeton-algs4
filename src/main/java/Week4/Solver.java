import java.util.LinkedList;

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

public class Solver {
    private LinkedList<Board> solutionBoards;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }

        processSolution(initial);
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solutionBoards != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (solutionBoards == null) return -1;

        return solutionBoards.size() - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (solutionBoards != null) {
            return new LinkedList<>(solutionBoards);
        }
        return null;
    }

    private void processSolution(Board initial) {
        MinPQ<SearchNode> queue = new MinPQ<>();
        MinPQ<SearchNode> twinQueue = new MinPQ<>();
        
        SearchNode searchNode = new SearchNode(initial, 0, null);
        queue.insert(searchNode);
        
        SearchNode twinSearchNode = new SearchNode(initial.twin(), 0, null);
        twinQueue.insert(twinSearchNode);

        while (true) {
            searchNode = queue.delMin();
            twinSearchNode = twinQueue.delMin();
            
            if (searchNode.board.isGoal()) {
                solutionBoards = getSolutionBoards(searchNode);
                break;
            }

            if (twinSearchNode.board.isGoal()) {
                break;
            }
            
            addNeighBours(searchNode, queue, searchNode.moves + 1);
            addNeighBours(twinSearchNode, twinQueue, twinSearchNode.moves + 1);
        }
    }

    private void addNeighBours(SearchNode searchNode, MinPQ<SearchNode> queue, int moves) {
        for (Board neighbour : searchNode.board.neighbors()) {
            if (searchNode.previous != null) {
                if (!neighbour.equals(searchNode.previous.board)) {
                    queue.insert(new SearchNode(neighbour, moves, searchNode));
                }
            } else {
                queue.insert(new SearchNode(neighbour, moves, searchNode));
            }
        }
    }

    private LinkedList<Board> getSolutionBoards(SearchNode solutionSearchNode) {
        SearchNode tempNode = solutionSearchNode;
        Stack<Board> stack = new Stack<>();
        
        while (tempNode != null) {
            stack.push(tempNode.board);
            tempNode = tempNode.previous;
        }
        
        LinkedList<Board> boards = new LinkedList<>();
        for (Board b : stack) {
            boards.add(b);
        }
        return boards;
    }

    private class SearchNode implements Comparable<SearchNode> {
        private final Board board;
        private final int priority;
        private final SearchNode previous;
        private final int moves;

        public SearchNode(Board b, int movesCount, SearchNode previous) {
            super();
            this.moves = movesCount;
            this.board = b;
            this.priority = this.board.manhattan() + movesCount;
            this.previous = previous;
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
