import java.util.Scanner;

public class AppStart {
    public static void main(String args[]){
        Integer noTransactions;
        Scanner input = new Scanner(System.in);
        System.out.println("Input the number of threads");
        noTransactions = input.nextInt();
        Bank bank = new Bank(noTransactions);
        bank.work();
    }
}
