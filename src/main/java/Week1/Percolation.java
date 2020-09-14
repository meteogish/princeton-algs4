import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static final byte OPENED_STATE = 1;
    private static final byte ISFULL_STATE = 2;
    private static final byte ISBOTTOM_STATE = 4;
    private static final int FIRST = 1;

    private final transient WeightedQuickUnionUF sites;
    private final transient int width;
    private final transient int topSite;

    private final transient byte[] sitesStates;
    private transient int openedSitesCount;

    public Percolation(int n) // create n-by-n grid, with all sites blocked
    {
        if (n <= 0) {
            throw new IllegalArgumentException("n param should be > 0");
        }

        sites = new WeightedQuickUnionUF(n * n + 1);
        width = n;
        sitesStates = new byte[n * n + 1];

        topSite = 0;
        openedSitesCount = 0;
        sitesStates[topSite] = (byte) (OPENED_STATE | ISFULL_STATE);
    }

    public void open(int row, int col) // open site (row, col) if it is not open already
    {
        if (isOpen(row, col)) {
            return;
        }

        int currentSite = getSiteIndex(row, col);
        byte currentSiteState = OPENED_STATE;

        if (row > FIRST)
        {
            // Can connect top neighbour
            currentSiteState = connectNeighbour(currentSite - width, currentSite, currentSiteState);
        }
        else 
        {
            // Can connect to top site
            currentSiteState = connectNeighbour(topSite, currentSite, currentSiteState);
        }

        if (row < width) {
            // Can connect bottom neighbour
            currentSiteState = connectNeighbour(currentSite + width, currentSite, currentSiteState);
        }
        else
        {
            // It is the bottom row site so just set state to BOTTOM_STATE
            currentSiteState = (byte) (currentSiteState | ISBOTTOM_STATE);
        }

        if (col > FIRST) 
        {
            // Can connect left neighbour
            currentSiteState = connectNeighbour(currentSite - 1, currentSite, currentSiteState);
        }

        if (col < width) 
        {
            // Can connect right neighbour
            currentSiteState = connectNeighbour(currentSite + 1, currentSite, currentSiteState);
        }

        // The roor of current site can change because of UF weighting process
        // so just set it's state to new cumulated state of all neighbours  
        int currentRoot = sites.find(currentSite);
        setSiteState(currentSite, currentSiteState);
        setSiteState(currentRoot, currentSiteState);
        ++openedSitesCount;
    }

    public boolean isOpen(int row, int col) // is site (row, col) open?
    {
        checkIndices(row, col);
        return isOpen(getSiteIndex(row, col));
    }

    public boolean isFull(int row, int col) // is site (row, col) full?
    {
        checkIndices(row, col);
        int root = sites.find(getSiteIndex(row, col));
        return isFull(root);
    }

    public int numberOfOpenSites() // number of open sites
    {
        return openedSitesCount;
    }

    public boolean percolates() // does the system percolate?
    {
        int topSiteRoot = sites.find(topSite);
        return isFull(topSiteRoot) && isBottomConnected(topSiteRoot);
    }

    private int getSiteIndex(int row, int col) {
        return --row * width + col;
    }

    private byte connectNeighbour(int neighbour, int currentSite, byte currentSiteState) {
        if (isOpen(neighbour)) 
        {
            int neighbourRoot = sites.find(neighbour);
            sites.union(currentSite, neighbourRoot);

            byte newState = (byte) (currentSiteState | sitesStates[neighbourRoot]);
            return newState;
        }
        return currentSiteState;
    }

    private void setSiteState(int siteIndex, byte state) {
        sitesStates[siteIndex] |= state;
    }

    private boolean isBottomConnected(int site) {
        return (sitesStates[site] & ISBOTTOM_STATE) != 0;
    }

    // public static void main(String[] args) // test client (optional)

    private boolean isFull(int site) {
        return (sitesStates[site] & ISFULL_STATE) != 0;
    }

    private boolean isOpen(int site) {
        return (sitesStates[site] & OPENED_STATE) != 0;
    }

    private void checkIndices(int row, int col) {
        if (row < 1 || row > width || col < 1 || col > width) {
            throw new IllegalArgumentException("Row = " + row + " Col = " + col);
        }
    }
}