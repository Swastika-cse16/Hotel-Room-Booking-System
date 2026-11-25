import java.util.*;
import java.io.*;

class Room {
    int number, price, nights;
    String type, guest;
    boolean booked;

    Room(int n, String t, int p) {
        number = n;
        type = t;
        price = p;
        guest = "";
        nights = 0;
        booked = false;
    }
}

public class Main {
    static ArrayList<Room> rooms = new ArrayList<>();
    static HashMap<Integer, Room> map = new HashMap<>();
    static Scanner sc = new Scanner(System.in);

    static void addRoom(Room r) {
        rooms.add(r);
        map.put(r.number, r);
    }

    static void loadData() {
        try {
            File f = new File("rooms.txt");

            if (!f.exists()) {
                addRoom(new Room(101, "Single", 1500));
                addRoom(new Room(102, "Double", 2500));
                addRoom(new Room(103, "Suite", 5000));
                saveData();
                return;
            }

            BufferedReader br = new BufferedReader(new FileReader(f));
            String line;

            while ((line = br.readLine()) != null) {
                String[] a = line.split(",");
                Room r = new Room(Integer.parseInt(a[0]), a[1],
                                  Integer.parseInt(a[2]));

                if (a.length >= 6) {
                    r.guest = a[3];
                    r.nights = Integer.parseInt(a[4]);
                    r.booked = Boolean.parseBoolean(a[5]);
                }
                addRoom(r);
            }
            br.close();

            if (rooms.isEmpty()) {
                addRoom(new Room(101, "Single", 1500));
                addRoom(new Room(102, "Double", 2500));
                addRoom(new Room(103, "Suite", 5000));
            }
        } catch (Exception e) {
            System.out.println("Error loading data.");
        }
    }

    static void saveData() {
        try {
            PrintWriter pw = new PrintWriter("rooms.txt");

            for (Room r : rooms) {
                pw.println(r.number + "," + r.type + "," + r.price + ","
                        + r.guest + "," + r.nights + "," + r.booked);
            }
            pw.close();
        } catch (Exception e) {
            System.out.println("Error saving data.");
        }
    }

    static void viewRooms() {
        int count = 0;
        System.out.println("\nRoom Details");

        for (Room r : rooms) {
            String status = r.booked ? "Booked" : "Available";
            System.out.println(r.number + " | " + r.type + " | "
                    + r.price + " | " + status);

            if (!r.booked)
                count++;
        }
        System.out.println("Available rooms: " + count);
    }

    static void bookRoom() {
        System.out.print("Enter room number: ");
        int n = sc.nextInt();
        sc.nextLine();

        Room r = map.get(n);

        if (r == null) {
            System.out.println("Room " + n + " not found.");
            return;
        }

        if (r.booked) {
            System.out.println("Room already booked.");
            return;
        }

        System.out.print("Enter guest name: ");
        r.guest = sc.nextLine().replace(",", " ");

        System.out.print("Enter number of nights: ");
        r.nights = sc.nextInt();
        sc.nextLine();

        if (r.nights <= 0) {
            r.guest = "";
            r.nights = 0;
            System.out.println("Invalid number of nights.");
            return;
        }

        r.booked = true;
        System.out.println("Room " + n + " booked for " + r.guest
                + ". Total: " + (r.price * r.nights));
    }

    static void searchRoom() {
        System.out.print("Enter room type: ");
        String type = sc.nextLine();
        boolean found = false;

        for (Room r : rooms) {
            if (r.type.equalsIgnoreCase(type) && !r.booked) {
                System.out.println(r.number + " | " + r.type + " | "
                        + r.price + " | Available");
                found = true;
            }
        }

        if (!found)
            System.out.println("No available room found.");
    }

    static void checkOut() {
        System.out.print("Enter room number: ");
        int n = sc.nextInt();
        sc.nextLine();

        Room r = map.get(n);

        if (r == null || !r.booked) {
            System.out.println("Room not found or not booked.");
            return;
        }

        int bill = r.price * r.nights;
        r.booked = false;
        r.guest = "";
        r.nights = 0;

        System.out.println("Room " + n + " checked out. Bill: "
                + bill + ". Status: Available");
    }

    static void sortRooms() {
        for (int i = 0; i < rooms.size() - 1; i++) {
            int max = i;

            for (int j = i + 1; j < rooms.size(); j++) {
                if (rooms.get(j).price > rooms.get(max).price)
                    max = j;
            }

            Room temp = rooms.get(i);
            rooms.set(i, rooms.get(max));
            rooms.set(max, temp);
        }

        System.out.println("Rooms sorted by price (High to Low):");
        for (Room r : rooms)
            System.out.println(r.number + " | " + r.price);
    }

    public static void main(String[] args) {
        loadData();

        while (true) {
            System.out.println("\n--- HOTEL ROOM BOOKING SYSTEM ---");
            System.out.println("1. View Rooms");
            System.out.println("2. Book Room");
            System.out.println("3. Search Available Room");
            System.out.println("4. Check Out");
            System.out.println("5. Sort Rooms by Price");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1: viewRooms(); break;
                case 2: bookRoom(); break;
                case 3: searchRoom(); break;
                case 4: checkOut(); break;
                case 5: sortRooms(); break;
                case 6:
                    saveData();
                    System.out.println("Data saved. Thank you!");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}