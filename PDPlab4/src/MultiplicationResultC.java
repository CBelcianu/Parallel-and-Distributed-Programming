public class MultiplicationResultC extends Thread {
    private int[][] D;
    private int[][] C;
    private int[][] E;
    private int row;

    MultiplicationResultC(int[][] D, int[][] C, int[][] E, int row) {
        this.D = D;
        this.C = C;
        this.E = E;
        this.row = row;
    }

    public void run() {
        AppStart.lock.lock();
        try {
            while (!AppStart.isFilledRow(D, row)) {
                AppStart.rowDone.await();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int j = 0; j < D[row].length; j++) {
            for (int k = 0; k < C[row].length; k++) {
                E[row][j] += D[row][k] * C[k][j];
            }
        }
        AppStart.lock.unlock();
    }
}