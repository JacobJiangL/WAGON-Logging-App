import java.awt.*;
import java.util.ArrayList;

public class ListResponseItem extends ListItem implements Container {

    protected ListComponent parentList;
    protected Response response;

    protected ArrayList<Component> components = new ArrayList<>();
    protected ArrayList<ButtonComponent> buttons = new ArrayList<>();

    protected int xPos = 0;
    protected int yPos = 0;
    protected final int height = 110;
    protected final int width;
    protected int referenceX;
    protected int referenceY;

    protected boolean isSelected = false;

    protected ListResponseItem(ListComponent parentList, Response response) {

        this.parentList = parentList;
        this.response = response;

        this.referenceX = parentList.xPos1;
        this.referenceY = parentList.yPos1;

        this.width = parentList.width - 40;

        this.createButtons();
    }


    @Override
    public void draw(Graphics2D g) {
        if(parentList.selectedResponse != response) {
            isSelected = false;
        }
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(950, 35, 450, 810);
        g.setColor(Color.WHITE);
        g.fillRect(955, 40, 440, 800);
        if(parentList.selectedResponse == null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("default", Font.PLAIN, 30));
            g.drawString("No Form Selected", 950 + (450-g.getFontMetrics().stringWidth("No Form Selected"))/2,
                    35 + 810/2);
        }

        g.setColor(Color.WHITE);
        g.fillRect(referenceX + xPos + 5, referenceY + yPos + 5, width, height);

        for(Component component : components) {
            component.draw(g);
        }

        drawText(g);
    }

    private void drawText(Graphics2D g) {
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String month = months[response.getDate()[0] - 1];
        String date = month + " " + response.getDate()[1];
        String year = String.valueOf(response.getDate()[2]);

        int dateFontSize = 18;

        g.setFont(new Font("default", Font.PLAIN, dateFontSize));
        g.drawString(date, referenceX + xPos + 765, referenceY + yPos + height/2-5);
        g.drawString(year, referenceX + xPos + 765, referenceY + yPos + height/2 + dateFontSize +5); //  +g.getFontMetrics().stringWidth(date)/2 - g.getFontMetrics().stringWidth(year)/2

        int destinationFontSize = 16;

        g.setFont(new Font("default", Font.PLAIN, destinationFontSize));
        while(g.getFontMetrics().stringWidth(response.actOfService) > 210) {
            destinationFontSize -= 1;
            g.setFont(new Font("default", Font.PLAIN, destinationFontSize));
        }
        g.drawString(response.actOfService, referenceX + xPos + 230, referenceY + yPos + height/2 + dateFontSize/2);

        int delivererFontSize = 16;

        g.setFont(new Font("default", Font.PLAIN, delivererFontSize));
        while(g.getFontMetrics().stringWidth(response.deliverer) > 280) {
            delivererFontSize -= 1;
            g.setFont(new Font("default", Font.PLAIN, delivererFontSize));
        }

        g.drawString(response.deliverer, referenceX + xPos + 460, referenceY + yPos + height/2 + delivererFontSize/2);
    }



    protected void move(int xShift, int yShift) {
        xPos += xShift;
        yPos += yShift;
        for(Component component : components) {
            component.move(xShift, yShift);
        }
    }

    private void createButtons() {
        ButtonFunction func = (Void) -> {clicked();};
        ButtonComponent.createButton(this, "View Form",
                referenceX + 20, referenceY + 5 + (height-40)/2,
                180, 40, 20, func);
    }

    @Override
    public void addComponent(Component component) {
        switch(component.getClass().toString().substring(6)) {
            case "ButtonComponent":
                components.add(component);
                buttons.add((ButtonComponent) component);
                break;
        }
    }

    @Override
    public void deleteComponent(Component component) {
        components.remove(component);
        switch(component.getClass().toString().substring(6)) {
            case "ButtonComponent":
                buttons.remove((ButtonComponent) component);
                break;
        }
    }

    @Override
    protected void updateItem(int xShift, int yShift) {

    }

    private void clicked() {
        this.isSelected = true;
        this.parentList.selectedResponse = this.response;

        ListComponent newList = ListComponent.createList(this.parentList.parentContainer, 975, 335,
                400, 425, 60, Color.LIGHT_GRAY, false, 7);
        ArrayList<ListItem> foodItems = new ArrayList<>();
        for(Object[] items : response.getItems()) {
            foodItems.add(new ListFoodItem(newList, items));
        }
        newList.refreshListItems(foodItems);

        ButtonFunction func = (Void) -> {placeholder();};
        ButtonComponent newButton = ButtonComponent.createButton(this.parentList.parentContainer, "Add To Sheet", 1050, 780, 250, 45, 20,
                func);
        newButton.setFunc((Void) -> {addForm(newButton, newList);});
    }

    protected void addForm(ButtonComponent buttonComponent, ListComponent listComponent) {
        Writer writer = new Writer();

        if(parentList.parentContainer.getClass().toString().substring(6).equals("PageContainer")) {
            PageContainer pageContainer = (PageContainer)parentList.parentContainer;
            writer.writeToSpreadSheet(response, pageContainer.parentGUI.processor);

            ArrayList<ListItem> responseItems = new ArrayList<>();
            for(Response response : pageContainer.parentGUI.processor.responses) {
                if(!response.written) {
                    responseItems.add(new ListResponseItem(parentList, response));
                }
            }
            parentList.refreshListItems(responseItems);
            parentList.selectedResponse = null;
            listComponent.delete();
            buttonComponent.delete();
        }

    }
    protected void placeholder() { }
}
