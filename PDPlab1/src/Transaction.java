public class Transaction {
    private Integer iD;
    private Account src;
    private Account dest;
    private Integer sum;
    private boolean failure;

    public Transaction(Account src, Account dest, Integer sum, Integer iD){
        this.src = src;
        this.dest = dest;
        this.sum = sum;
        failure = false;
        this.iD = iD;
    }

    public Account getSrc() {return this.src;}
    public Account getDest() {return this.dest;}
    public Integer getSum() {return this.sum;}
    public Integer getId() {return this.iD;}
    public boolean isFailure() {return this.failure;}
    public void setStatus(boolean failure) {this.failure = failure;}
}
