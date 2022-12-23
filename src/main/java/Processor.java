import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.ArrayList;

public class Processor {

    protected List<List<Object>> rawValues;
    protected ArrayList<Response> responses;

    public Processor(List<List<Object>> values) throws IOException, GeneralSecurityException{
        this.rawValues = values;
    }

    protected void start() {
        this.responses = sortResponses(rawValues);
        System.out.println(responses);
    }
    private ArrayList<Response> sortResponses(List<List<Object>> rawValues) {
        ArrayList<Response> responses = new ArrayList<>();
        for(List<Object> rawResponse : rawValues) {
            responses.add(new Response(rawResponse));
        }
        return responses;
    }
}
