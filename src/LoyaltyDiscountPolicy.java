public class LoyaltyDiscountPolicy implements DiscountPolicy {
    @Override
    public double applyDiscount(Student student, double price) {
        if (student.getLoyaltyPoints() >= 100) {
            return price * 0.9; // 10% zniżki
        }
        return price;
    }
}