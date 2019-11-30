import java.util.Scanner;

public class AppStart {;
    public static void main(String[] args){
        Scanner in = new Scanner(System.in);
        System.out.println("Input the size of the array (must be a power of 2): ");
        int size =  in.nextInt();
        int[] b = new int[size+1];
        long startTime;
        long endTime;
        long elapsedTime;

        for(int i=0; i<size; i++){
            b[i]=i+1;
        }
        Adder adder = new Adder(b[size-1],b);
        startTime = System.currentTimeMillis();
        adder.Algorithm();
        endTime = System.currentTimeMillis();
        elapsedTime = endTime-startTime;

        StringBuilder result= new StringBuilder();
        for(int k=1;k<size+1;k++){
            result.append(b[k]).append(' ');
        }
        System.out.println("\n"+result+"\n");
        System.out.println("BENCHMARK: Prefix scan algorithm took " + elapsedTime + " us.");
    }
}
