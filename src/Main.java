
import org.h2.tools.Server;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static Scanner scanNew = new Scanner(System.in);
    static Scanner scanUser = new Scanner(System.in);


    public static void main(String[] args) throws SQLException {
        Server.createWebServer().start();
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE IF NOT EXISTS items (id IDENTITY, user_id INT, text VARCHAR, is_done BOOLEAN)");
        stmt.execute("CREATE TABLE IF NOT EXISTS users (id IDENTITY, name VARCHAR)");
        System.out.println("Please enter name your name....");
        String username = scanUser.nextLine();
        insertUser(conn, username);
        User user = selectUser(conn, username);



        while (true) {
            System.out.println("[1]. Create to-do item");
            System.out.println("[2]. Toggle to-do item");
            System.out.println("[3]. List to-do items");
            System.out.println("[4]. Delete an item");

            String option = scanner.nextLine();

            if (option.equals("1")) {
                System.out.println("Enter your to-do item:");
                String text = scanner.nextLine();


                insertItem(conn,user.id,text);
            }
            else if (option.equals("2")) {
                System.out.println("Enter the number of the item you want to toggle:");
                int itemNum = Integer.valueOf(scanner.nextLine());

                updateItem(conn, itemNum);
            }
            else if (option.equals("3")) {
                ArrayList<ToDoItem> items = selectItems(conn);

                for (ToDoItem item : items) {
                    String checkbox = "[ ]";
                    if (item.isDone) {
                        checkbox = "[x]";
                    }
                    System.out.printf("%s %d. %s\n", checkbox, item.itemId, item.text);
                }
            }
            else if (option.equals("4")) {
                System.out.println("Enter the ID of the item to be deleted: ");
                Integer itemNumber = Integer.parseInt(scanNew.nextLine());
                deleteItem(conn, itemNumber);

            }

            else {
                System.out.println("Invalid option");
            }
        }
    }


    public static void insertUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?)");
        stmt.setString(1, name);
        stmt.execute();
    }

    public static User selectUser(Connection conn, String name) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet set = stmt.executeQuery();
        set.next();

        return new User(set.getInt("id"), set.getString("name"));
    }

    public static ArrayList<ToDoItem> selectItems(Connection conn) throws SQLException {
        ArrayList<ToDoItem> items = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN users  ON items.user_id = users.id");
        ResultSet set = stmt.executeQuery();

        while (set.next()) {
            items.add(new ToDoItem(set.getString("items.text"), set.getBoolean("items.is_done"),
                    set.getInt("items.user_id"), set.getInt("items.id")));
        }

        return items;
    }

    public static void insertItem(Connection conn, Integer userId, String text) throws SQLException {

        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items VALUES (NULL, ?, ?, ?)");

        stmt.setInt(1, Integer.parseInt(String.valueOf(userId)));
        stmt.setString(2, text);
        stmt.setBoolean(3, false);

        stmt.execute();
    }

    public static void deleteItem(Connection conn, Integer itemId) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id = ?");
        stmt.setInt(1, itemId);
        stmt.execute();
    }
    public static void updateItem (Connection conn, int id) throws SQLException {
        PreparedStatement stmt = conn.prepareStatement("UPDATE items SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1, id);
        stmt.execute();
    }

}

