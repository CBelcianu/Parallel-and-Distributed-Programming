import java.util.ArrayList;
import java.util.List;

class Permutation {
    private List<Runnable> threads = new ArrayList<>();
    private int[][] adjacency;

    Permutation(int[][] adjacency){
        this.adjacency=adjacency;
    }

    List<Runnable> getThreads(){
        return this.threads;
    }

    void heapPermutation(int[] a, int size, int n) {
        if (size == 1){
            PathChecker pc = new PathChecker(a.clone(), adjacency);
            threads.add(pc);
        }

        for (int i=0; i<size; i++) {
            heapPermutation(a, size-1, n);

            if (size % 2 == 1) {
                int temp = a[0];
                a[0] = a[size-1];
                a[size-1] = temp;
            }
            else {
                int temp = a[i];
                a[i] = a[size-1];
                a[size-1] = temp;
            }
        }
    }
}
