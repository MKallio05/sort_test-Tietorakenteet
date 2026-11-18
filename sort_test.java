import java.util.*;
import java.util.function.Consumer;

public class sort_test {

    public static void insertionSort(int[] data) {
        for (int i = 1; i < data.length; i++) {
            int key = data[i];
            int j = i - 1;

            while (j >= 0 && data[j] > key) {
                data[j + 1] = data[j];
                j--;
            }

            data[j + 1] = key;
        }
    }

    public static void bubbleSort(int[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            boolean swapped = false;

            for (int j = 0; j < data.length - i - 1; j++) {
                if (data[j] > data[j + 1]) {
                    int temp = data[j];
                    data[j] = data[j + 1];
                    data[j + 1] = temp;
                    swapped = true;
                }
            }

            if (!swapped) {
                break;
            }
        }
    }

    public static void merge(int[] data, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; i++) {
            L[i] = data[l + i];
        }

        for (int j = 0; j < n2; j++) {
            R[j] = data[m + 1 + j];
        }

        int i = 0;
        int j = 0;
        int k = l;

        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                data[k] = L[i];
                i++;
            } else {
                data[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            data[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            data[k] = R[j];
            j++;
            k++;
        }
    }

    public static void mergeSort(int[] data, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;

            mergeSort(data, l, m);
            mergeSort(data, m + 1, r);

            merge(data, l, m, r);
        }
    }

    public static int partition(int[] data, int low, int high) {
        int pivot = data[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (data[j] < pivot) {
                i++;

                int temp = data[i];
                data[i] = data[j];
                data[j] = temp;
            }
        }

        int temp = data[i + 1];
        data[i + 1] = data[high];
        data[high] = temp;

        return i + 1;
    }

    public static void quickSort(int[] data, int low, int high) {
        if (low < high) {
            int pi = partition(data, low, high);

            quickSort(data, low, pi - 1);
            quickSort(data, pi + 1, high);
        }
    }

    // ===== Optimoitu Quicksort =====

    // Insertion sort, joka toimii vain välillä [low, high]
    public static void insertionSortRange(int[] data, int low, int high) {
        for (int i = low + 1; i <= high; i++) {
            int key = data[i];
            int j = i - 1;

            while (j >= low && data[j] > key) {
                data[j + 1] = data[j];
                j--;
            }

            data[j + 1] = key;
        }
    }

    public static void swap(int[] data, int a, int b) {
        int temp = data[a];
        data[a] = data[b];
        data[b] = temp;
    }

    // Median-of-three: mediaani siirretään high-paikalle pivotiksi
    public static int partitionMedian(int[] data, int low, int high) {
        int mid = low + (high - low) / 2;

        if (data[mid] < data[low])  swap(data, mid, low);
        if (data[high] < data[low]) swap(data, high, low);
        if (data[mid] < data[high]) swap(data, mid, high);

        int pivot = data[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            if (data[j] < pivot) {
                i++;
                swap(data, i, j);
            }
        }

        swap(data, i + 1, high);
        return i + 1;
    }

    public static void quickSortOpt(int[] data, int low, int high) {
        while (low < high) {
            // Pieni osataulukko: insertion sort on nopeampi kuin rekursio
            if (high - low < 16) {
                insertionSortRange(data, low, high);
                return;
            }

            int pi = partitionMedian(data, low, high);

            // Rekursio pienempään osaan, silmukka suurempaan -> pinon syvyys O(log n)
            if (pi - low < high - pi) {
                quickSortOpt(data, low, pi - 1);
                low = pi + 1;
            } else {
                quickSortOpt(data, pi + 1, high);
                high = pi - 1;
            }
        }
    }

    public static void shellSort(int[] data) {
        int n = data.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                int temp = data[i];
                int j;

                for (j = i; j >= gap && data[j - gap] > temp; j -= gap) {
                    data[j] = data[j - gap];
                }

                data[j] = temp;
            }
        }
    }

    public static int[] CreateAscendingTable(int n) {
        int[] data = new int[n];

        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }

        return data;
    }

    public static int[] CreateDescendingTable(int n) {
        int[] data = new int[n];

        for (int i = 0; i < data.length; i++) {
            data[i] = n - i;
        }

        return data;
    }

    public static int[] createRandomData(int n) {
        int[] data = new int[n];
        Random random = new Random();

        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(n);
        }

        return data;
    }

    // Ajanotto ja virheenkäsittely yhdessä paikassa, algoritmi annetaan funktioreferenssinä
    public static void testSort(String name, int[] originalData, Consumer<int[]> sorter) {
        int[] copy = Arrays.copyOf(originalData, originalData.length);

        long start = System.nanoTime();
        long stop;

        try {
            sorter.accept(copy);
            stop = System.nanoTime();
            System.out.printf("%s: %.6f seconds%n",
                    name, (stop - start) / 1_000_000_000.0);
        } catch (StackOverflowError e) {
            stop = System.nanoTime();
            System.out.printf("%s: KAATUI (StackOverflowError) %.6f sekunnin jälkeen%n",
                    name, (stop - start) / 1_000_000_000.0);
        }
    }

    public static void testSorts(int[] originalData, String dataType) {
        System.out.println("\n========== " + dataType + " DATA ==========");

        testSort("Arrays.sort", originalData, Arrays::sort);
        testSort("Insertion Sort", originalData, sort_test::insertionSort);
        testSort("Bubble Sort", originalData, sort_test::bubbleSort);
        testSort("Merge Sort", originalData, d -> mergeSort(d, 0, d.length - 1));
        testSort("Quick Sort", originalData, d -> quickSort(d, 0, d.length - 1));
        testSort("Quick Sort (optimoitu)", originalData, d -> quickSortOpt(d, 0, d.length - 1));
        testSort("Shell Sort", originalData, sort_test::shellSort);
    }

    public static void main(String[] args) {
        int[] sizes = {10000, 500000};

        for (int size : sizes) {
            System.out.println("\n############ N = " + size + " ############");
            testSorts(createRandomData(size), "Random");
            testSorts(CreateAscendingTable(size), "Ascending");
            testSorts(CreateDescendingTable(size), "Descending");
        }
    }

}