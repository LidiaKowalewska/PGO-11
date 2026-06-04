import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private List<Student> students = new ArrayList<>();
    private List<Equipment> equipments = new ArrayList<>();
    private List<Reservation> reservations = new ArrayList<>();
    private DiscountPolicy discountPolicy;
    private int reservationCounter = 1;

    public ReservationService(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public void addStudent(Student student) { students.add(student); }
    public void addEquipment(Equipment equipment) { equipments.add(equipment); }

    public List<Student> getStudents() { return students; }
    public List<Equipment> getEquipments() { return equipments; }
    public List<Reservation> getReservations() { return reservations; }

    public Reservation createReservation(String studentId, String equipmentId, int days) {
        if (days < 1 || days > 14) {
            throw new ReservationException("Liczba dni musi być z zakresu 1-14.");
        }

        Student student = students.stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new ReservationException("Nie znaleziono studenta o podanym ID."));

        Equipment equipment = equipments.stream()
                .filter(e -> e.getId().equals(equipmentId))
                .findFirst()
                .orElseThrow(() -> new ReservationException("Nie znaleziono sprzętu o podanym ID."));

        if (!equipment.isAvailable()) {
            throw new ReservationException("Wybrany sprzęt nie jest obecnie dostępny.");
        }

        String resId = String.format("R%03d", reservationCounter++);
        Reservation reservation = new Reservation(resId, student, equipment, days, discountPolicy);

        equipment.setAvailable(false);
        reservations.add(reservation);

        return reservation;
    }

    public void returnEquipment(String reservationId) {
        Reservation reservation = reservations.stream()
                .filter(r -> r.getId().equals(reservationId))
                .findFirst()
                .orElseThrow(() -> new ReservationException("Nie znaleziono rezerwacji."));

        if (reservation.getStatus() != ReservationStatus.ACTIVE) {
            throw new ReservationException("Ta rezerwacja nie jest już aktywna.");
        }

        reservation.completeReservation();
        reservation.getEquipment().setAvailable(true);

        int earnedPoints = (int) (reservation.getFinalCost() / 10);
        reservation.getStudent().addLoyaltyPoints(earnedPoints);
    }

    public void printReport() {
        System.out.println("--- RAPORT ---");
        double totalRevenue = 0;

        System.out.println("Zakończone rezerwacje:");
        for (Reservation r : reservations) {
            if (r.getStatus() == ReservationStatus.RETURNED) {
                System.out.println(r.getDisplayText());
                totalRevenue += r.getFinalCost();
            }
        }

        System.out.println("Łączny przychód: " + String.format("%.2f PLN", totalRevenue));

        Student topStudent = students.stream()
                .max((s1, s2) -> Integer.compare(s1.getLoyaltyPoints(), s2.getLoyaltyPoints()))
                .orElse(null);

        if (topStudent != null) {
            System.out.println("Najlepszy student: " + topStudent.getFullName() + " (" + topStudent.getLoyaltyPoints() + " pkt)");
        }
    }
}