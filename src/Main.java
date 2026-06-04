import java.util.Scanner;

public class Main {
    private static ReservationService service = new ReservationService(new LoyaltyDiscountPolicy());
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        seedData();
        boolean running = true;

        System.out.println("Witaj w systemie MediaLab!");

        while (running) {
            printMenu();
            System.out.print("Wybór: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": showStudents(); break;
                    case "2": showEquipment(false); break;
                    case "3": showEquipment(true); break;
                    case "4": searchEquipment(); break;
                    case "5": createReservation(); break;
                    case "6": returnEquipment(); break;
                    case "7": showActiveReservations(); break;
                    case "8": service.printReport(); break;
                    case "0": running = false; break;
                    default: System.out.println("Błędny wybór, spróbuj ponownie.");
                }
            } catch (Exception e) {
                System.out.println("Błąd: " + e.getMessage());
            }
            System.out.println();
        }
        System.out.println("Zamykanie programu...");
    }

    private static void printMenu() {
        System.out.println("\n1. Wyświetl studentów");
        System.out.println("2. Wyświetl sprzęt");
        System.out.println("3. Wyświetl dostępny sprzęt (filtr)");
        System.out.println("4. Szukaj sprzętu po nazwie");
        System.out.println("5. Utwórz rezerwację");
        System.out.println("6. Zwróć sprzęt");
        System.out.println("7. Pokaż aktywne rezerwacje");
        System.out.println("8. Pokaż raport");
        System.out.println("0. Zakończ");
    }

    private static void showStudents() {
        service.getStudents().forEach(s -> System.out.println(s.getDisplayText()));
    }

    private static void showEquipment(boolean onlyAvailable) {
        service.getEquipments().stream()
                .filter(e -> !onlyAvailable || e.isAvailable())
                .forEach(e -> System.out.println(e.getDisplayText()));
    }

    private static void searchEquipment() {
        System.out.print("Podaj fragment nazwy: ");
        String query = scanner.nextLine().toLowerCase();
        service.getEquipments().stream()
                .filter(e -> e.getName().toLowerCase().contains(query))
                .forEach(e -> System.out.println(e.getDisplayText()));
    }

    private static void createReservation() {
        System.out.print("Podaj id studenta: ");
        String studentId = scanner.nextLine();
        System.out.print("Podaj id sprzętu: ");
        String equipId = scanner.nextLine();
        System.out.print("Podaj liczbę dni: ");
        int days = Integer.parseInt(scanner.nextLine());

        Reservation r = service.createReservation(studentId, equipId, days);
        System.out.println("Utworzono rezerwację " + r.getId());
        System.out.println("Sprzęt: " + r.getEquipment().getName());
        System.out.println(String.format("Koszt: %.2f PLN", r.getFinalCost()));
        System.out.println("Status: " + r.getStatus());
    }

    private static void returnEquipment() {
        System.out.print("Podaj id rezerwacji: ");
        String resId = scanner.nextLine();
        service.returnEquipment(resId);

        Reservation r = service.getReservations().stream().filter(x -> x.getId().equals(resId)).findFirst().get();
        int earnedPoints = (int) (r.getFinalCost() / 10);
        System.out.println("Zwrócono sprzęt. Student " + r.getStudent().getFullName() + " otrzymał " + earnedPoints + " punktów lojalnościowych.");
    }

    private static void showActiveReservations() {
        service.getReservations().stream()
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .forEach(r -> System.out.println(r.getDisplayText()));
    }

    private static void seedData() {
        service.addStudent(new Student("S001", "Anna Kowalska", "12c", 120));
        service.addStudent(new Student("S002", "Marek Nowak", "12c", 40));
        service.addStudent(new Student("S003", "Julia Zielińska", "13a", 0));

        service.addEquipment(new LaptopSet("E001", "Lenovo ThinkPad Lab", 80.0, 32, true));
        service.addEquipment(new LaptopSet("E002", "Dell XPS Demo", 100.0, 16, false));
        service.addEquipment(new CameraKit("E003", "Sony Content Kit", 90.0, 3, true));
        service.addEquipment(new CameraKit("E004", "Canon Interview Kit", 70.0, 1, true));
    }
}
