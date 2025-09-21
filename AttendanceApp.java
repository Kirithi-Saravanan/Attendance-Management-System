import java.util.*;

enum Role {
    ADMIN, FACULTY, STUDENT
}

enum AttendanceStatus {
    PRESENT, ABSENT
}

class User {
    private final String username;
    private final String password;
    private final Role role;

    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }
}

class Attendance {
    private final String studentName;
    private final String date;
    private final AttendanceStatus status;

    public Attendance(String studentName, String date, AttendanceStatus status) {
        this.studentName = studentName;
        this.date = date;
        this.status = status;
    }

    public String getStudentName() { return studentName; }
    public String getDate() { return date; }
    public AttendanceStatus getStatus() { return status; }
}

public class AttendanceApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<User> users = new ArrayList<>();
    private static final List<Attendance> records = new ArrayList<>();

    public static void main(String[] args) {
        seedUsers();

        System.out.println("===== Welcome to Attendance Tracker =====");
        System.out.print("Username: ");
        String uname = scanner.nextLine();
        System.out.print("Password: ");
        String pwd = scanner.nextLine();

        User user = authenticate(uname, pwd);

        if (user == null) {
            System.out.println(" Login failed. Please restart the program.");
            return;
        }

        switch (user.getRole()) {
            case ADMIN -> adminPanel();
            case FACULTY -> facultyPanel();
            case STUDENT -> studentPanel(user.getUsername());
        }
    }

    // Authentication
    private static User authenticate(String uname, String pwd) {
        return users.stream()
                .filter(u -> u.getUsername().equals(uname) && u.getPassword().equals(pwd))
                .findFirst()
                .orElse(null);
    }

    // Panels
    private static void adminPanel() {
        while (true) {
            System.out.println("\n--- Admin Dashboard ---");
            System.out.println("1. Register New User");
            System.out.println("2. View All Attendance");
            System.out.println("3. Logout");
            System.out.print("Select option: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> displayAllAttendance();
                case 3 -> { return; }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void facultyPanel() {
        while (true) {
            System.out.println("\n--- Faculty Dashboard ---");
            System.out.println("1. Record Attendance");
            System.out.println("2. View Attendance Records");
            System.out.println("3. Logout");
            System.out.print("Select option: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> recordAttendance();
                case 2 -> displayAllAttendance();
                case 3 -> { return; }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    private static void studentPanel(String studentName) {
        while (true) {
            System.out.println("\n--- Student Dashboard ---");
            System.out.println("1. View My Attendance");
            System.out.println("2. Logout");
            System.out.print("Select option: ");
            int choice = readInt();

            switch (choice) {
                case 1 -> showStudentAttendance(studentName);
                case 2 -> { return; }
                default -> System.out.println("Invalid option, try again.");
            }
        }
    }

    //  Functions
    private static void registerUser() {
        System.out.print("Enter new username: ");
        String uname = scanner.nextLine();
        System.out.print("Enter password: ");
        String pwd = scanner.nextLine();
        System.out.print("Role (ADMIN/FACULTY/STUDENT): ");
        String roleInput = scanner.nextLine().toUpperCase();

        try {
            Role role = Role.valueOf(roleInput);
            users.add(new User(uname, pwd, role));
            System.out.println(" User registered successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println(" Invalid role. Allowed values: ADMIN, FACULTY, STUDENT");
        }
    }

    private static void recordAttendance() {
        System.out.print("Student name: ");
        String student = scanner.nextLine();
        System.out.print("Date (YYYY-MM-DD): ");
        String date = scanner.nextLine();
        System.out.print("Status (PRESENT/ABSENT): ");
        String statusInput = scanner.nextLine().toUpperCase();

        try {
            AttendanceStatus status = AttendanceStatus.valueOf(statusInput);
            records.add(new Attendance(student, date, status));
            System.out.println(" Attendance recorded for " + student);
        } catch (IllegalArgumentException e) {
            System.out.println(" Invalid status. Use PRESENT or ABSENT.");
        }
    }

    private static void displayAllAttendance() {
        if (records.isEmpty()) {
            System.out.println("⚠ No attendance records available.");
            return;
        }
        System.out.println("\n--- Attendance Records ---");
        records.forEach(r ->
                System.out.println(r.getStudentName() + " | " + r.getDate() + " | " + r.getStatus()));
    }

    private static void showStudentAttendance(String student) {
        boolean anyFound = false;
        for (Attendance r : records) {
            if (r.getStudentName().equalsIgnoreCase(student)) {
                System.out.println(r.getDate() + " | " + r.getStatus());
                anyFound = true;
            }
        }
        if (!anyFound) {
            System.out.println("⚠ No records found for " + student);
        }
    }

    private static void seedUsers() {
        users.add(new User("admin", "admin123", Role.ADMIN));
        users.add(new User("faculty", "fac123", Role.FACULTY));
        users.add(new User("student", "stud123", Role.STUDENT));
    }

    private static int readInt() {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid number: ");
            }
        }
    }
}


