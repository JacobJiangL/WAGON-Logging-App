import java.awt.Graphics2D;

public interface Container {

    void draw(Graphics2D g);

    void addComponent(Component component);

    void deleteComponent(Component component);

}
