import org.checkerframework.checker.units.qual.A;

import java.awt.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// awt/swing imports


public class PageContainer implements Container {

    protected boolean scrolling = false;

    protected GUI parentGUI;
    protected ArrayList<Component> components = new ArrayList<>();
    protected ArrayList<ButtonComponent> buttons = new ArrayList<>();
    protected ArrayList<ListComponent> lists = new ArrayList<>();

    protected GUI.PageType pageID;
    public PageContainer(GUI parentGUI, GUI.PageType pageID) {
        this.parentGUI = parentGUI;
        this.pageID = pageID;

        this.createLists();
        this.createButtons();
    }
    private void createButtons () {
        switch(pageID) {
            case REQUEST_LIST:
                break;
            case REQUEST_DETAILS:
                break;
        }
    }
    private void createLists () {
        switch(pageID) {
            case REQUEST_LIST:
                ListComponent newList = ListComponent.createList(this, 10, 35, 910, 810,
                        115, Color.LIGHT_GRAY, true, 7);
                ArrayList<ListItem> responseItems = new ArrayList<>();
                for(Response response : parentGUI.processor.responses) {
                    if(!response.written) {
                        responseItems.add(new ListResponseItem(newList, response));
                    }
                }
                newList.refreshListItems(responseItems);
                break;
            case REQUEST_DETAILS:
                break;
        }
    }

    @Override
    public void draw(Graphics2D g) {
        for(Component component : components) {
            component.draw(g);
        }
    }

    protected void checkScroll(int xClick, int yClick) {

    }
    @Override
    public void addComponent(Component component) {
        switch(component.getClass().toString().substring(6)) {
            case "ButtonComponent":
                components.add(component);
                buttons.add((ButtonComponent) component);
                break;
            case "ListComponent":
                components.add(component);
                lists.add((ListComponent) component);
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
            case "ListComponent":
                lists.remove((ListComponent) component);
        }
    }

    public ArrayList<ButtonComponent> getButtons() {
        ArrayList<ButtonComponent> buttonList = new ArrayList<>(buttons);
        for(ListComponent list : lists) {
            for(ListItem item : list.getListItems()) {
                switch(item.getClass().toString().substring(6)) {
                    case "ListResponseItem":
                        for (Component component : ((ListResponseItem) item).components) {
                            if (component.getClass().toString().substring(6).equals("ButtonComponent")) {
                                buttonList.add((ButtonComponent) component);
                            }
                        }
                        break;
                }
            }
        }


        return buttonList;
    }
}
