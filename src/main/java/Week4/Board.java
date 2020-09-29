import java.util.LinkedList;

import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private transient int _dimension;
    private transient int[] _tiles1D;
    private int zeroAtY;
    private int zeroAtX;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles.length < 2 || tiles.length >= 128) {
            throw new IllegalArgumentException("The tiles size is not supported");
        }

        if (tiles.length != tiles[0].length) {
            throw new IllegalArgumentException("The tiles size is not supported");
        }

        _dimension = tiles.length;
        _tiles1D = new int[_dimension * _dimension];

        for (int i = 0; i < _dimension; i++) {
            for (int j = 0; j < _dimension; j++) {
                if (tiles[i][j] == 0) {
                    zeroAtY = i;
                    zeroAtX = j;
                }

                _tiles1D[to1D(i, j)] = tiles[i][j];
            }
        }
    }
                                           
    // string representation of this board
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(_dimension);

        for (int i = 0; i < _dimension; i++) {
            sb.append("\n");
            for (int j = 0; j < _dimension; j++) {
                sb.append(" ");
                sb.append(_tiles1D[to1D(i, j)]);
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
                int currentState = _tiles1D[to1D(i, j)];
                if (currentState != 0 && currentState != shouldBe) {
                    // System.out.println("Increase hamming at point " + i + " " + j);
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
                int currentState = _tiles1D[to1D(i, j)];

                int x = (currentState - 1) % _dimension;
                int y = (currentState - 1) / _dimension;
                int dist = 
                    currentState == 0
                    ? 0
                    : Math.abs(y - i) + Math.abs(x - j);
                // System.out.println("(" + i + " , " + j + ") = " + dist);
                result += dist;
            }
        }
        return result;
    }

    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < _dimension * _dimension - 1; i++) {
           if (_tiles1D[i] == 0 || _tiles1D[i] != i + 1) {
               return false;
           } 
        }
        
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        Board that = (Board) y;

        if (_dimension != that._dimension) {
            return false;
        }

        for (int i = 0; i < _dimension; i++) {
            for (int j = 0; j < _dimension; j++) {
                if (_tiles1D[to1D(i,j)] != that._tiles1D[to1D(i,j)]) {
                    return false;
                }
            }
        }

        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        LinkedList<Board> result = new LinkedList<>();

        if (zeroAtX - 1 >= 0) {
            result.add(createNeighbor(zeroAtY, zeroAtX - 1));
        }

        if (zeroAtX + 1 < _dimension) {
            result.add(createNeighbor(zeroAtY, zeroAtX + 1));
        }

        if (zeroAtY - 1 >= 0) {
            result.add(createNeighbor(zeroAtY - 1, zeroAtX));
        }

        if (zeroAtY + 1 < _dimension) {
            result.add(createNeighbor(zeroAtY + 1, zeroAtX));
        }

        return result;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int firstRandom = getRandomNotZeroTileNotLike(-1);
        int secondRandom = getRandomNotZeroTileNotLike(firstRandom);

        int firstTileValue = _tiles1D[firstRandom];
        int secondTileValue = _tiles1D[secondRandom];

        int[][] newTiles = new int[_dimension][_dimension];

        for (int i = 0; i < _dimension; i++) {
            for (int j = 0; j < _dimension; j++) {
                int oneD = to1D(i, j);
                if (oneD == firstRandom) {
                    newTiles[i][j] = secondTileValue;
                }
                else if (oneD == secondRandom) {
                    newTiles[i][j] = firstTileValue;
                }
                else 
                {
                    newTiles[i][j] = _tiles1D[oneD];
                }
            }
        }

        return new Board(newTiles);
    }

    private int getRandomNotZeroTileNotLike(int notLike)
    {
        int zeroAt = to1D(zeroAtY, zeroAtX);
        int tile = -1;

        do
        {
            tile = StdRandom.uniform(_tiles1D.length);
        } while (tile == zeroAt || tile == notLike);

        return tile;
    }

    private Board createNeighbor(int moveZeroToY, int moveZeroToX) {
        int[][] copy = new int[_dimension][_dimension];
        for (int i = 0; i < _dimension; i++) {
            for (int j = 0; j < _dimension; j++) {
                copy[i][j] = _tiles1D[to1D(i,j)];
            }
        }

        int t = copy[moveZeroToY][moveZeroToX];
        copy[zeroAtY][zeroAtX] = t;
        copy[moveZeroToY][moveZeroToX] = 0;

        return new Board(copy);
    }

    private int to1D(int y, int x) {
        return y * _dimension + x;
    }

    // unit testing (not graded)
    //public static void main(String[] args)

}