import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Node {
    int[] number;
    int[] initialNumber;
    ArrayList<Integer> result;
    boolean isFirst;
    boolean isLast;
    BlockingQueue<Integer> queue;
    Node parent;

    Node(final int size, final Node par){
        final Random r = new Random();
        number = new int[size];
        number[0] = r.nextInt(9) + 1;
        for(int i = 1; i < size; ++i){
            number[i] = r.nextInt(10);
        }
        queue = new ArrayBlockingQueue<>(100);
        parent = par;
        isFirst = false;
        isLast = false;
    }

    Node(final int size, final Node par, final boolean isFirst, final boolean isLast)
    {
        this(size, par);
        this.isLast = isLast;
        this.isFirst = isFirst;
        if(isFirst){
            final Random r = new Random();
            initialNumber = new int[size];
            initialNumber[0] = r.nextInt(9) + 1;
            for(int i = 1; i < size; ++i){
                initialNumber[i] = r.nextInt(10);
            }
        }
        if(isLast){
            result = new ArrayList<>();
        }
    }

    void print(){
        if(isFirst)
        {
            System.out.print("\t\t");
            for (int value : initialNumber) {
                System.out.print(value);
            }
            System.out.print(" +");
        }
        System.out.println();
        System.out.print("\t\t");
        for (int value : number) {
            System.out.print(value);
        }
        if(!isLast) {
            System.out.print(" +");
        }
    }
}
