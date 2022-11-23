
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.util.ArrayList;
public class Button {

    protected int xPos;
    protected int yPos;

    protected int width;
    protected int height;

    protected Rectangle2D.Double hitbox;

    protected boolean pressed;
    private String label = "";
    private final String identifier;

    public Button(String label, int xPos, int yPos, int width, int height, String identifier, ArrayList<Button> buttons) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.width = width;
        this.height = height;
        this.label = label;
        this.identifier = identifier;
        createHitbox();

        buttons.add(this);
    }

    //create point on mouse click, check if point is within rectangle hitbox of button
    public boolean checkPressed(double xClick, double yClick) {
        this.pressed = this.hitbox.contains(new Point2D.Double(xClick, yClick));
        return this.pressed;
    }
    public void release() {
        this.pressed = false;
    }

    public void render(Graphics2D g) {
        if (this.pressed) {
            g.setColor(Color.WHITE);
            g.fillRect(xPos, yPos, width, height);
            g.setColor(new Color(215, 215, 215));
            g.fillRect(xPos + 10, yPos + 10, width - 10, height - 10);
        } else {
            g.setColor(new Color(215, 215, 215));
            g.fillRect(xPos, yPos, width, height);
            g.setColor(Color.WHITE);
            g.fillRect(xPos + 10, yPos + 10, width - 10, height - 10);
        }

        g.setFont(new Font("default", Font.PLAIN, height - 6));
        g.fillRect(this.xPos, this.yPos, width, height);

        g.setColor(Color.BLACK);
        g.drawString(label, xPos + width / 4, yPos + height / 2 + (height - 6) / 2);
    }

    private void createHitbox() {
        this.hitbox = new Rectangle2D.Double(xPos, yPos, width, height);
    }

    public String getIdentifier() {
        return this.identifier;
    }
    public void move(int xShift, int yShift) {
        this.xPos += xShift;
        this.yPos += yShift;
        createHitbox();
    }

}