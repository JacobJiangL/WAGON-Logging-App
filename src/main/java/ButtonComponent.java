
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ButtonComponent implements Component {

    Container parentContainer;

    protected int xPos1;
    protected int yPos1;

    protected int width;
    protected int height;

    protected int fontSize;

    protected Rectangle2D.Double hitbox;

    protected boolean pressed;
    protected boolean disabled;

    private String label = "";
    private ButtonFunction function;


    private ButtonComponent(Container parentContainer, String label, int xPos1, int yPos1, int width, int height,
                            int fontSize, ButtonFunction function) {
        this.parentContainer = parentContainer;

        this.xPos1 = xPos1;
        this.yPos1 = yPos1;
        this.width = width;
        this.height = height;

        this.label = label;
        this.fontSize = fontSize;

        this.function = function;
        createHitbox();
    }

    //create point on mouse click, check if point is within rectangle hitbox of button
    public boolean checkPressed(double xClick, double yClick) {
        this.pressed = this.hitbox.contains(new Point2D.Double(xClick, yClick));
        if(this.disabled) {
            return false;
        }
        return this.pressed;
    }

    public void release() {
        this.pressed = false;
    }

    public void executeCommand() {
        function.executeCommand(null);
    }

    @Override
    public void draw(Graphics2D g) {
        if (this.pressed) {
            g.setColor(new Color(255, 255, 255));
            g.fillRect(xPos1, yPos1, width, height);
            g.setColor(new Color(205, 205, 205));
            g.fillRect(xPos1 + 1, yPos1 + 1, width - 2, height - 2);
        } else {
            g.setColor(new Color(205, 205, 205));
            g.fillRect(xPos1, yPos1, width, height);
            g.setColor(new Color(255, 255, 255));
            g.fillRect(xPos1 + 1, yPos1 + 1, width - 2, height - 2);
        }

        g.setFont(new Font("default", Font.PLAIN, fontSize));
        int textWidth = g.getFontMetrics().stringWidth(this.label);

        g.setColor(Color.BLACK);
        g.drawString(label, xPos1 + (width-textWidth)/2, yPos1 + (height+fontSize)/2);
    }

    private void createHitbox() {
        this.hitbox = new Rectangle2D.Double(xPos1, yPos1, width, height);
    }

    public void move(int xShift, int yShift) {
        this.xPos1 += xShift;
        this.yPos1 += yShift;
        createHitbox();
    }

    @Override
    public void delete() {
        parentContainer.deleteComponent(this);
    }

    public static ButtonComponent createButton(Container container, String label, int xPos, int yPos, int width, int height,
                                               int fontSize, ButtonFunction function) {
        ButtonComponent newButton = new ButtonComponent(container, label, xPos, yPos, width, height, fontSize, function);
        container.addComponent(newButton);
        return newButton;
    }

    protected void setFunc(ButtonFunction function) {
        this.function = function;
    }
}