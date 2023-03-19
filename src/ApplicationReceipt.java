import java.util.List;
import java.util.Map;

public class ApplicationReceipt {
    private final User user;
    private final Map<User, List<Pet>> receiptMap;

    public ApplicationReceipt(User user, Map<User, List<Pet>> petData) {
        this.user = user;
        this.receiptMap = petData;
    }

    public User getUser() {
        return user;
    }

    public Map<User, List<Pet>> getReceiptMap() {
        return receiptMap;
    }
}


