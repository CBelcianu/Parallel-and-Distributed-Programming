public class MultiplicationAB extends Thread {
    private int[][] A;
    private int[][] B;
    private int[][] D;
    private int row;

    MultiplicationAB(int[][] A, int[][] B, int[][] D, int row) {
        this.A = A;
        this.B = B;
        this.D = D;
        this.row = row;
    }

    public void run() {
        AppStart.lock.lock();
        for (int j = 0; j < B[row].length; j++) {
            for (int k = 0; k < A[row].length; k++) {
                D[row][j] += A[row][k] * B[k][j];
            }
        }
        AppStart.rowDone.signal();
        AppStart.lock.unlock();
    }
}
