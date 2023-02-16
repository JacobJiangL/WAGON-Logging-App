import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Processor {

    protected List<List<Object>> rawValues;
    protected ArrayList<Response> responses;

    public Processor(List<List<Object>> values) {
        this.rawValues = values;
    }

    protected void start() {
        this.responses = sortResponses(rawValues);
        System.out.println(responses);
    }

    protected void refreshData() throws IOException, GeneralSecurityException{
        this.rawValues = Main.refreshData();
    }
    private ArrayList<Response> sortResponses(List<List<Object>> rawValues) {
        ArrayList<Response> responses = new ArrayList<>();
        for(List<Object> rawResponse : rawValues) {
            responses.add(new Response(rawResponse));
        }
        Collections.reverse(responses);
        return responses;
    }
}
