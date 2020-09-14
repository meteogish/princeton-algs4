import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Insertion
 */
public class SortingAlgs {

    public static <T> void shellSort(Comparable<T>[] array) {
        int countOfExch = 0;
        int[] hRanges = { 15, 7, 3, 1 };

        for (int h : hRanges) {
            for (int i = h; i < array.length; i++) {
                for (int j = i; j >= h; j -= h) {
                    if (less(array[j], array[j - h])) {
                        exch(array, j, j - h);
                        ++countOfExch;
                    } else {
                        break;
                    }
                }
            }
        }

        StdOut.println("Count of exch: " + countOfExch);
    }

    public static <T> void insertionSort(Comparable<T>[] array) {
        int countOfExch = 0;
        for (int i = 1; i < array.length; i++) {
            for (int j = i; j > 0; j--) {
                if (less(array[j], array[j - 1])) {
                    exch(array, j, j - 1);
                    ++countOfExch;
                } else {
                    break;
                }
            }
        }

        StdOut.println("Count of exch: " + countOfExch);
    }

    public static <T> void selectionSort(Comparable<T>[] array) {
        for (int i = 0; i < array.length; i++) {
            int minIdx = i;

            for (int j = i + 1; j < array.length; j++) {
                if (less(array[j], array[minIdx])) {
                    minIdx = j;
                }
            }

            exch(array, i, minIdx);
        }
    }

    private static <T> void exch(Comparable<T>[] array, int fromIdx, int toIdx) {
        Comparable<T> t = array[fromIdx];
        array[fromIdx] = array[toIdx];
        array[toIdx] = t;
    }

    private static <T extends Comparable> Boolean less(T left, T right) {
        return left.compareTo(right) < 0;
    }

    @SuppressWarnings("PMD.UnusedPrivateMethod")
    private static <T> void printArray(T[] array) {
        for (int i = 0; i < array.length; i++) {
            StdOut.println(array[i]);
        }
    }

    public static <T> Boolean isSorted(Comparable<T>[] array) {
        for (int i = 1; i < array.length; i++) {
            if (!less(array[i - 1], array[i])) {
                return false;
            }
        }
        return true;
    }

    // public static void main(String[] args) {
    //     final int count = 20;
    //     Double[] array = new Double[count];
        
    //     for (int i = 0; i < count; i++) {
    //         array[i] = StdRandom.uniform();
    //     }
    //     assert isSorted(array) == false;

    //     StdOut.println("Before:");
    //     printArray(array);
    //     SortingAlgs.shellSort(array);

    //     StdOut.println("After:");
    //     printArray(array);

    //     assert isSorted(array) : "Array is not sorted";
    //     // StdOut.println(isSorted(array));
    // }
}