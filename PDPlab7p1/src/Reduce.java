public class Reduce implements Runnable {
    private int[] a;
    private int pos;
    private int power;

    Reduce(int[] a, int pos, int power){
        this.a=a;
        this.pos=pos;
        this.power=power;
    }

    @Override
    public void run() {
        a[pos + power - 1] += a[pos + power/2 - 1];
    }
}
