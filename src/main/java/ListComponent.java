import java.awt.*;

import java.util.ArrayList;

public class ListComponent implements Component  {

    protected Container parentContainer;
    private ArrayList<ListItem> items;
    protected int xPos1;
    protected int yPos1;
    protected int xPos2;
    protected int yPos2;

    protected int width;
    protected int height;

    protected Color bgColor;

    private int spacing;
    protected int lastSpacing;

    protected ScrollBarComponent scrollBar;

    private final boolean canScroll;
    private boolean toggleScroll;

    protected Response selectedResponse;

    private ListComponent(Container parentContainer, int xPos1, int yPos1, int width, int height,
                          int spacing, Color bgColor, boolean canScroll, int capacity) {
        this.parentContainer = parentContainer;
        this.items = new ArrayList<>();

        this.canScroll = canScroll;
        this.toggleScroll = true;
        this.xPos1 = xPos1;
        this.yPos1 = yPos1;
        this.xPos2 = xPos1 + width;
        this.yPos2 = yPos1 + height;
        this.width = width;
        this.height = height;

        this.bgColor = bgColor;
        this.spacing = spacing;

        this.scrollBar = new ScrollBarComponent(this, this.width - 30, 25, this.height - 10, capacity);
    }

    public void toggleScroll(boolean toggleScroll) {
        this.toggleScroll = toggleScroll;
    }

    public void setSpacing(int spacing) {
        this.spacing = spacing;
    }

    public int getSpacing() {
        return spacing;
    }

    @Override
    public void draw(Graphics2D g) {
        g.setColor(bgColor);
        g.fillRect(xPos1, yPos1, width, height);
        for(ListItem item : items) {
            switch(item.getClass().toString().substring(6)) {
                case "ListResponseItem":
                    if (((ListResponseItem) item).yPos < height - 5 && ((ListResponseItem) item).yPos >= 0) {
                        for(ButtonComponent button : ((ListResponseItem) item).buttons) {
                            button.disabled = false;
                        }
                        item.draw(g);
                    } else {
                        for(ButtonComponent button : ((ListResponseItem) item).buttons) {
                            button.disabled = true;
                        }
                    }
                    break;
                case "ListFoodItem":
                    if (((ListFoodItem) item).yPos < height - 5 && ((ListFoodItem) item).yPos >= 0) {
                        item.draw(g);
                    }
                    break;
            }
        }
        scrollBar.draw(g);

        if(selectedResponse != null) {
            drawSelectedResponse(g);
        }
    }

    protected void drawSelectedResponse(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("default", Font.PLAIN, 15));
        g.drawString("Act of Service:", 975, 70);
        g.drawString("Delivered by:", 975, 130);
        g.drawString("Email:", 975, 190);
        g.drawString("Date:", 975, 250);
        g.setFont(new Font("default", Font.PLAIN, 20));
        g.drawString("Items:", 975, 320);

        int actOfServiceFontSize = 18;
        g.setFont(new Font("default", Font.PLAIN, actOfServiceFontSize));
        while(g.getFontMetrics().stringWidth(selectedResponse.actOfService) > 400) {
            actOfServiceFontSize -= 1;
            g.setFont(new Font("default", Font.PLAIN, actOfServiceFontSize));
        }
        g.drawString(selectedResponse.actOfService, 975, 100);

        int delivererFontSize = 18;
        g.setFont(new Font("default", Font.PLAIN, delivererFontSize));
        while(g.getFontMetrics().stringWidth(selectedResponse.deliverer) > 400) {
            delivererFontSize -= 1;
            g.setFont(new Font("default", Font.PLAIN, delivererFontSize));
        }
        g.drawString(selectedResponse.deliverer, 975, 160);

        int emailFontSize = 18;
//        while(g.getFontMetrics().stringWidth(selectedResponse.emailAddress) > 400) {
//            emailFontSize -= 1;
            g.setFont(new Font("default", Font.PLAIN, emailFontSize));
//        }
        g.drawString(selectedResponse.emailAddress, 975, 220);

        int dateFontSize = 20;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String month = months[selectedResponse.getDate()[0] - 1];
        String date = month + " " + selectedResponse.getDate()[1] + ", " + selectedResponse.getDate()[2];
        g.setFont(new Font("default", Font.PLAIN, dateFontSize));

        g.drawString(date, 975, 280);
    }

    @Override
    public void move(int xShift, int yShift) {
        for(ListItem item : items) {
            item.move(xShift, yShift);
        }
    }

    @Override
    public void delete() {
        parentContainer.deleteComponent(this);
    }


    protected static ListComponent createList(Container parentContainer, int xPos1, int yPos1,
                                     int width, int height, int spacing, Color bgColor, boolean canScroll, int capacity) {
        ListComponent newList = new ListComponent(parentContainer, xPos1, yPos1, width, height, spacing, bgColor, canScroll, capacity);
        parentContainer.addComponent(newList);
        return newList;
    }

    protected void refreshListItems(ArrayList<ListItem> items) {
        this.items = items;
        int s = 0;
        for(ListItem item : items) {
            item.move(0, s);
            s += spacing;
            this.lastSpacing = s;
        }
    }

    protected ArrayList<ListItem> getListItems() {
        return items;
    }

    protected void addListElements(ListItem item) {
        items.add(item);
        item.move(0, lastSpacing+spacing);
    }



}
