public class Reservation implements Displayable {
    private String id;
    private Student student;
    private Equipment equipment;
    private int days;
    private ReservationStatus status;
    private double finalCost;

    public Reservation(String id, Student student, Equipment equipment, int days, DiscountPolicy discountPolicy) {
        this.id = id;
        this.student = student;
        this.equipment = equipment;
        this.days = days;
        this.status = ReservationStatus.ACTIVE;
        this.finalCost = calculateTotalCost(discountPolicy);
    }

    public String getId() { return id; }
    public Student getStudent() { return student; }
    public Equipment getEquipment() { return equipment; }
    public ReservationStatus getStatus() { return status; }
    public double getFinalCost() { return finalCost; }

    public void completeReservation() {
        this.status = ReservationStatus.RETURNED;
    }

    private double calculateTotalCost(DiscountPolicy discountPolicy) {
        double priceBeforeDiscount = equipment.calculateDailyPrice() * days;
        return discountPolicy.applyDiscount(student, priceBeforeDiscount);
    }

    @Override
    public String getDisplayText() {
        return String.format("Rezerwacja [%s] | Status: %s | Koszt: %.2f PLN | Sprzęt: %s | Student: %s",
                id, status, finalCost, equipment.getName(), student.getFullName());
    }
}