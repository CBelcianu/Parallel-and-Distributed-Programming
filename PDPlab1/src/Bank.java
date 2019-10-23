import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Bank {
    private List<Account> accounts;
    private List<Thread> threads;
    private Integer tid;
    private Integer noTranscations;
    private CheckerThread checkerThread;

    public Bank(Integer noTransactions){
        this.accounts = new ArrayList<>();
        this.threads = new ArrayList<>();
        this.tid = 1;
        this.noTranscations = noTransactions;

        for(int i=1; i<=10; i++){
            this.accounts.add(new Account(i, 7000));
        }

        for(int i=1; i<noTransactions; i++){
            Random random = new Random();
            int index1 = random.nextInt(this.accounts.size());
            int index2 = random.nextInt(this.accounts.size());
            while(index1==index2){
                index2 = random.nextInt(this.accounts.size());
            }
            Account src = this.accounts.get(index1);
            Account dest = this.accounts.get(index2);
            int sum = random.nextInt(3500);
            Transaction transaction = new Transaction(src, dest, sum, this.tid);
            this.threads.add(new Thread(this.tid, transaction));
            this.tid += 1;
        }
        this.checkerThread = new CheckerThread(this.noTranscations, this.accounts);
    }

    public void work(){
        ExecutorService executorService = Executors.newFixedThreadPool(this.noTranscations);
        this.threads.forEach(executorService::execute);
        executorService.execute(this.checkerThread);
        try {
            executorService.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        for(Account acc:this.accounts){
            System.out.println("AccountId: " + acc.getId());
            System.out.println(acc.getLogs());
            System.out.println("Balance: " + acc.getBalance());
        }
        CheckerTask checkerTask = new CheckerTask(this.noTranscations, this.accounts);
        checkerTask.run();

    }
}
