import java.util.List;

public class Response {

    protected List<Object> rawResponse;

    private int[] date;
    private Object[][] items;
    protected String actOfService;
    protected String deliverer;
    protected String emailAddress;

    protected boolean written;

    public Response(List<Object> rawResponse) {
        this.rawResponse = rawResponse;
        this.actOfService = (String) rawResponse.get(3);
        this.deliverer = (String) rawResponse.get(2);
        this.emailAddress = (String) rawResponse.get(1);

        setItems();
        setDate();

        this.written = rawResponse.get(rawResponse.size()-1).equals("written");
    }

    private void setItems() {
        Reader r = new Reader();
        this.items = r.splitAndProcessResponses((String) rawResponse.get(4));
    }

    private void setDate() {
        String rawDate = (String) rawResponse.get(0); // xx/xx/xxxx xx:xx:xxxx
        String[] stringDate = rawDate.split(" ")[0].split("/"); // xx/xx/xxxx -> xx, xx, xxxx
        int[] date = new int[3];
        for(int i=0; i < stringDate.length; i++) {
            date[i] = Integer.parseInt(stringDate[i]); // month, day, year
        }
        this.date = date;
    }

    public Object[][] getItems() {
        return items;
    }

    public int[] getDate() {
        return date;
    }

    public int getDateAsInt() {
        return date[0] * 100 + date[1] + date[2] * 10000; // yyyymmdd
    }

    public String getDateAsString() {
        return date[0] + "/" + date[1] + "/" + date[2];
    }
}
