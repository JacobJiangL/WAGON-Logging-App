import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ScrollBarComponent {

    protected ListComponent parentList;

    protected int xPos;
    protected int scrollYPos = 0;
    protected int scrollHeight;
    protected int scrollPos = 0;

    protected int width;
    protected int height;

    protected Rectangle2D.Double hitbox;

    protected boolean pressed;

    protected int referenceX;
    protected int referenceY;

    protected int prevY = -1;
    private final int capacity;


    protected ScrollBarComponent(ListComponent parentList, int xPos, int width, int height, int capacity) {
        this.parentList = parentList;

        this.xPos = xPos;

        this.referenceX = parentList.xPos1;
        this.referenceY = parentList.yPos1;

        this.width = width;
        this.height = height;
        this.capacity = capacity;

        this.configureScrollHeight();
    }

    public boolean checkPressed(double xClick, double yClick) {
        try {
            this.pressed = this.hitbox.contains(new Point2D.Double(xClick, yClick));
            return this.pressed;
        } catch(NullPointerException e) {
            return false;
        }
    }

    protected void release() {
        this.pressed = false;
        this.prevY = -1;
    }

    public void draw(Graphics2D g) {
        this.configureScrollHeight();
        g.setColor(Color.WHITE);
        g.fillRect(referenceX + xPos, referenceY + 5 + scrollYPos, width, scrollHeight);

        g.setColor(new Color(220, 220, 220));
        g.drawRect(referenceX + xPos, referenceY + 5, width, height);
    }

    protected void scroll(int currentY) {
        if(prevY != -1) {
            scrollYPos += currentY - prevY;
        }
        if (scrollYPos > height - scrollHeight) {
            scrollYPos = height - scrollHeight;
        } else if (scrollYPos < 0) {
            scrollYPos = 0;
        } else {
            prevY = currentY;
        }
        createHitbox();

        int relativeScrollPos = -1 * scrollYPos/scrollHeight - scrollPos;
        parentList.move(0, parentList.getSpacing() * relativeScrollPos);
        scrollPos += relativeScrollPos;
    }

    protected void configureScrollHeight() {
        int temp = parentList.getListItems().size() - (capacity-1);
        if(temp <= 0) {temp=1;}

        this.scrollHeight = this.height / temp;
        createHitbox();
    }

    private void createHitbox() {
        try {
            this.hitbox = new Rectangle2D.Double(referenceX + xPos, referenceY + 5 + scrollYPos, width, scrollHeight);
        } catch (NullPointerException ignored) {}
    }
}
