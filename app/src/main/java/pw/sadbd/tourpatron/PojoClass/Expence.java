package pw.sadbd.tourpatron.PojoClass;

public class Expence {
    private String expenseID;
    private String expenseName;
    private String costName;

    public Expence(String expenseID, String expenseName, String costName) {
        this.expenseID = expenseID;
        this.expenseName = expenseName;
        this.costName = costName;
    }
    public Expence() {

    }

    public String getExpenseID() {
        return expenseID;
    }

    public void setExpenseID(String expenseID) {
        this.expenseID = expenseID;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public String getCostName() {
        return costName;
    }

    public void setCostName(String costName) {
        this.costName = costName;
    }

    @Override
    public String toString() {
        return "Expence{" +
                "expenseID='" + expenseID + '\'' +
                ", expenseName='" + expenseName + '\'' +
                ", costName='" + costName + '\'' +
                '}';
    }
}
