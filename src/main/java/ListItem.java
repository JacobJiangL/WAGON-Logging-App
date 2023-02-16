import java.awt.Graphics2D;
import java.util.ArrayList;

public abstract class ListItem {

    protected ArrayList<Component> components;

    protected abstract void draw(Graphics2D g);

    protected abstract void addComponent(Component component);

    protected abstract void move(int xShift, int yShift);

    protected abstract void updateItem(int xShift, int yShift);

}
