import edu.princeton.cs.algs4.StdOut;

public class Board {
    private transient int _dimension;
    private transient int[][] _tiles;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles.length < 2 || tiles.length >= 128) {
            throw new IllegalArgumentException("The tiles size is not supported");
        }

        if (tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("The tiles size is not supported");
        }

        _tiles = tiles;
        _dimension = tiles.length;
    }
                                           
    // string representation of this board
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(_tiles.length);

        for (int j = 0; j < _tiles.length; j++) {
            sb.append("\n");
            for (int i = 0; i < _tiles.length; i++) {
                sb.append(" ");
                sb.append(_tiles[i][j]);
            }
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return _dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int result = 0;

        for (int i = 0; i < _dimension; i++) {
            for (int j = 0; j < _dimension; j++) {
                int shouldBe = i * _dimension + j + 1;
                int currentState = _tiles[i][j];
                if (currentState != shouldBe) {
                    ++result;
                }
            }
        }
        return result;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int result = 0;

        for (int i = 0; i < _dimension; i++) {
            for (int j = 0; j < _dimension; j++) {
                int currentState = _tiles[i][j];

                int x = (currentState - 1) % _dimension;
                int y = (currentState - 1) / _dimension;
                int dist = (y - i) + (x - j);

                result += dist;
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;

        if (_dimension != that._dimension) {
            return false;
        }

        for (int i = 0; i < _dimension; i++) {
            for (int j = 0; j < _dimension; j++) {
                if (_tiles[i][j] != that._tiles[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        return null;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        return null;
    }

    // unit testing (not graded)
    //public static void main(String[] args)

}