import java.util.*;
import java.util.concurrent.Semaphore;

public class Account {
    private List<Pair<Integer, Integer>> logs;
    private Integer iD;
    private Integer balance;
    public Semaphore mtx;

    public Account(Integer iD, int balance){
        this.iD = iD;
        this.balance = balance;
        this.logs = new ArrayList<>();
        this.mtx = new Semaphore(1);
    }

    public List<Pair<Integer, Integer>> getLogs() {return logs;}
    public Integer getId() {return this.iD;}
    public Integer getBalance() {return this.balance;}

    public void alterBalance(Integer sum, Integer TransactionId){
        this.balance += sum;
        this.logs.add(new Pair<>(TransactionId, sum));
    }

    public void dropLast2(){
        this.logs.remove(this.logs.size()-1);
        this.logs.remove(this.logs.size()-1);
    }
}
