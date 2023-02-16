import java.awt.Graphics2D;

public interface Component {

    void draw(Graphics2D g);

    void move(int xShift, int yShift);

    void delete();
}
