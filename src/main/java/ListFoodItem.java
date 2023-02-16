import java.awt.*;
import java.util.ArrayList;

public class ListFoodItem extends ListItem{
    protected ListComponent parentList;
    protected Object[] item;

    protected String itemName;
    protected int amount;
    protected String unit;
    protected int weight;
    protected String weightUnit;

    protected ArrayList<Component> components = new ArrayList<>();

    protected int xPos = 0;
    protected int yPos = 0;
    protected final int height = 55;
    protected final int width;
    protected int referenceX;
    protected int referenceY;

    protected boolean isSelected = false;

    protected ListFoodItem (ListComponent parentList, Object[] item) {

        this.parentList = parentList;
        this.item = item;
        this.itemName = (String) item[0];
        this.amount = (int) item[1];

        try {   this.unit = (String) item[2];
        } catch(NullPointerException e) {
            this.unit = null;
        }
        try {   this.weight = (int) item[3];
        } catch(NullPointerException e) {
            this.weightUnit = (String) item[4];
        }
        this.referenceX = parentList.xPos1;
        this.referenceY = parentList.yPos1;

        this.width = parentList.width - 40;

    }

    @Override
    protected void draw(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.fillRect(referenceX + xPos + 5, referenceY + yPos + 5, width, height);

        for(Component component : components) {
            component.draw(g);
        }
        drawText(g);
    }

    private void drawText(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("default", Font.PLAIN, 15));
        g.drawString(itemName, referenceX + xPos + 5 + 10, referenceY + yPos + 5 + 15/2 + height/2);

        String numAndUnit = amount + " " + unit;
        if(numAndUnit.substring(numAndUnit.length()-4).equals("null")) {
            numAndUnit = String.valueOf(amount);
        }
        g.drawString(numAndUnit, referenceX + xPos + 5 + width - (10 + g.getFontMetrics().stringWidth(numAndUnit)),
                referenceY + yPos + 5 + 15/2 + height/2);
    }

    @Override
    protected void addComponent(Component component) {

    }

    @Override
    protected void move(int xShift, int yShift) {
        xPos += xShift;
        yPos += yShift;
        for(Component component : components) {
            component.move(xShift, yShift);
        }
    }

    @Override
    protected void updateItem(int xShift, int yShift) {

    }
}
