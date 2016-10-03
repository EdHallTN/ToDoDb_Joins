/**
 * Created by EdHall on 9/6/16.
 */
public class ToDoItem {
    String text;
    Boolean isDone;
    Integer userId;
    Integer itemId;

    public ToDoItem(String text, Boolean isDone, Integer userId, Integer itemId) {
        this.text = text;
        this.isDone = isDone;
        this.userId = userId;
        this.itemId = itemId;
    }
}
