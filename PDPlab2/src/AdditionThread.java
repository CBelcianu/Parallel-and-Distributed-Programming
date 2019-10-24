public class AdditionThread implements Runnable {
    private int[][] A;
    private int[][] B;
    private int[][] C;

    public AdditionThread(int[][] A, int[][] B, int[][] C){
        this.A = A;
        this.B = B;
        this.C = C;
    }

    @Override
    public void run() {
        for(int i=0; i<this.A[0].length; i++){
            for(int j=0; j<this.A[0].length; j++){
                C[i][j] = A[i][j] + B[i][j];
            }
        }
    }
}
