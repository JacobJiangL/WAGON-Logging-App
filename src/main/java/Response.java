import java.util.List;

public class Response {

    protected List<Object> rawResponse;
    protected int[] date;
    protected Object[][] items;
    protected String actOfService;
    protected String deliverer;
    protected String emailAddress;

    public Response(List<Object> rawResponse) {
        this.rawResponse = rawResponse;
        setItems();
        setDate();
        this.actOfService = (String) rawResponse.get(3);
        this.deliverer = (String) rawResponse.get(2);
        this.emailAddress = (String) rawResponse.get(1);
    }
    private void setItems() {
        Reader r = new Reader();
        this.items = r.splitAndProcessResponses((String) rawResponse.get(4));
    }
    private void setDate() {
        String rawDate = (String) rawResponse.get(0); // x/x/x x:x:x
        String[] stringDate = rawDate.split(" ")[0].split("/"); // x/x/x -> x, x, x
        int[] date = new int[3];
        for(int i=0; i < stringDate.length; i++) {
            date[i] = Integer.parseInt(stringDate[i]); // month, day, year
        }
        this.date = date;
    }
    private Object[][] getItems() {
        return items;
    }
    private int[] getDate() {
        return date;
    }
}
