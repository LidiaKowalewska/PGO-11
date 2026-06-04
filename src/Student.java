public class Student implements Displayable {
    private String id;
    private String fullName;
    private String groupName;
    private int loyaltyPoints;

    public Student(String id, String fullName, String groupName, int loyaltyPoints) {
        this.id = id;
        this.fullName = fullName;
        this.groupName = groupName;
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public int getLoyaltyPoints() { return loyaltyPoints; }

    public void addLoyaltyPoints(int points) {
        if (points > 0) {
            this.loyaltyPoints += points;
        }
    }

    @Override
    public String getDisplayText() {
        return String.format("Student[%s] %s, Grupa: %s, Punkty: %d", id, fullName, groupName, loyaltyPoints);
    }
}