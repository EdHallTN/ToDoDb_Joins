import org.junit.Assert;
import org.junit.Test;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by EdHall on 9/29/16.
 */
public class MainTest {
    @Test
    public void insertUser() throws SQLException {
        String name = "Frank";
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO users VALUES (NULL, ?)");
        stmt.setString(1, name);
        assert stmt.execute();
    }

    @Test
    public void selectUser() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        String name = "Frank";
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM users WHERE name = ?");
        stmt.setString(1, name);
        ResultSet set = stmt.executeQuery();
        set.next();

        Assert.assertNotNull(name);
    }

    @Test
    public  void selectItems() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        ArrayList<ToDoItem> items = new ArrayList<>();

        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM items INNER JOIN users  ON items.user_id = users.id");
        ResultSet set = stmt.executeQuery();

        while (set.next()) {
            items.add(new ToDoItem(set.getString("items.text"), set.getBoolean("items.is_done"),
                    set.getInt("items.user_id"), set.getInt("items.id")));
        }

        Assert.assertNotNull(items);
    }

    @Test
    public  void deleteItem() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement stmt = conn.prepareStatement("DELETE FROM items WHERE id = ?");
        int itemId = 3;
        stmt.setInt(1, itemId);
        assert stmt.execute();

    }

    @Test
    public  void updateItem () throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        int id = 3;
        PreparedStatement stmt = conn.prepareStatement("UPDATE items SET is_done = NOT is_done WHERE id = ?");
        stmt.setInt(1, id);
        assert stmt.execute();
    }

    @Test
    public void insertItem() throws SQLException {
        Connection conn = DriverManager.getConnection("jdbc:h2:./main");
        PreparedStatement stmt = conn.prepareStatement("INSERT INTO items VALUES (NULL, ?, ?, ?)");

        Integer userId = 2;
        String text = "run";
        stmt.setInt(1, Integer.parseInt(String.valueOf(userId)));
        stmt.setString(2, text);
        stmt.setBoolean(3, false);

        assert stmt.execute();
    }

}
