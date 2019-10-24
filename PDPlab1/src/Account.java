import java.util.*;
import java.util.concurrent.Semaphore;

public class Account {
    private List<Pair<Integer, Integer>> logs;
    private Integer iD;
    private Integer balance;
    private String prettyLogs;
    public Semaphore mtx;

    public Account(Integer iD, int balance){
        this.iD = iD;
        this.balance = balance;
        this.logs = new ArrayList<>();
        this.mtx = new Semaphore(1);
        this.prettyLogs = "\nAccount" + this.iD + " :";
    }

    public List<Pair<Integer, Integer>> getLogs() {return logs;}
    public Integer getId() {return this.iD;}
    public Integer getBalance() {return this.balance;}

    public void alterBalance(Integer sum, Integer TransactionId, Integer AccountId){
        this.balance += sum;
        this.logs.add(new Pair<>(TransactionId, sum));
        if (AccountId != null){
            if (sum > 0){
                this.prettyLogs += "\n\t#" + TransactionId + " received " + sum + " from Account" + AccountId;
            }
            else{
                this.prettyLogs += "\n\t#" + TransactionId + " sent " + -sum + " to Account" + AccountId;
            }
        } else {
            String target = "\n\t#" + TransactionId;
            String replacement = "\n\t#" + TransactionId + " FAILED TASK:";
            this.prettyLogs = this.prettyLogs.replaceAll(target, replacement);
        }
    }

    public void dropLast2(){
        this.logs.remove(this.logs.size()-1);
        this.logs.remove(this.logs.size()-1);
    }

    public void prettyPrint(){
        System.out.println(this.prettyLogs);
        System.out.println("Balance: " + this.balance + "\n");
    }
}
