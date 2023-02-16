import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import javax.swing.*;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Writer {

    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

    protected void writeToSpreadSheet(Response responseToWrite, Processor processor) {
        try {
            processor.refreshData();

            final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            String spreadsheetId = "1x2vkid-swh80IBgYpq50Gjy5o0uufo50cks06cVRy_Q";
            String range = "Sheet1!";

            int column = 1;

            for (Response response : processor.responses) {
                if (response.getDateAsInt() < responseToWrite.getDateAsInt()) {
                    range += columnNumToLetter(column);
                    break;
                } else {
                    if (processor.responses.indexOf(response) == processor.responses.size() - 1) {
                        range += columnNumToLetter(column);
                        break;
                    }
                    if(response.written) {
                        column += 4;
                    }
                }
            }

            range += ":" + columnNumToLetter(column + 3);
            Sheets service =
                    new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, Main.getCredentials(HTTP_TRANSPORT))
                            .setApplicationName(APPLICATION_NAME)
                            .build();

            String shiftRange = "Sheet1!" + columnNumToLetter(column) + "1";
            ValueRange responses = service.spreadsheets().values()
                    .get(spreadsheetId, shiftRange).setValueRenderOption("FORMATTED_VALUE")
                    .execute();

            ValueRange responsesToShift = new ValueRange();
            if(responses.getValues() != null) {
                String maxSize =  columnNumToLetter(service.spreadsheets().values().get(spreadsheetId, "1:99").
                        setMajorDimension("COLUMNS").execute().getValues().size() + 4);
                shiftRange += ":" + maxSize;
                responsesToShift = new ValueRange().setValues(service.spreadsheets().values()
                        .get(spreadsheetId, shiftRange).setValueRenderOption("FORMATTED_VALUE")
                        .execute().getValues());
                shiftRange = "Sheet1!" + columnNumToLetter(column + 4) + ":" + maxSize;
                for(List<Object> response : responsesToShift.getValues()) {
                    for(int i=0;i<response.size();i++) {
                        try {
                            response.set(i, Integer.parseInt((String)response.get(i)));
                        } catch (Exception ignore) { }
                    }
                }
                service.spreadsheets().values()
                        .update(spreadsheetId, shiftRange, responsesToShift)
                        .setValueInputOption("RAW")
                        .execute();

                ClearValuesRequest clearRequest = new ClearValuesRequest();
                service.spreadsheets().values().clear(spreadsheetId, range, clearRequest).execute();
            }

            List<List<Object>> values = new ArrayList<>();
            values.add(Arrays.asList(responseToWrite.getDateAsString(), responseToWrite.actOfService));

            for(Object[] item : responseToWrite.getItems()) {
                values.add(Arrays.asList(item[0], item[1], item[2]));
            }

            ValueRange vr = new ValueRange().setValues(values).setMajorDimension("ROWS");
            service.spreadsheets().values()
                    .update(spreadsheetId, range, vr)
                    .setValueInputOption("RAW")
                    .execute();

            responseToWrite.written = true;
            int row = 1 + processor.responses.size();
            for(Response response : processor.responses) {
                if(response.equals(responseToWrite)) {
                    break;
                } else {
                    row--;
                }
            }
            spreadsheetId = "1J2osKAnpqZM1BiURJJRWgH4hP9FBKglya709vHCm8ek";
            range = "Form Responses 1!" + "I" + row;

            List<List<Object>> written = new ArrayList<>();

            written.add(Arrays.asList("written"));

            vr = new ValueRange().setValues(written);
            service.spreadsheets().values()
                    .update(spreadsheetId, range, vr)
                    .setValueInputOption("RAW")
                    .execute();

        } catch (IOException | GeneralSecurityException e) { System.out.println(e.toString());
        }

    }

    private static String columnNumToLetter(int columnNum) {
        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L",
                "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
        String letter = "";
        if((double)columnNum/26 > 1) {
            if(columnNum%26 == 0) {
                letter += columnNumToLetter((columnNum/26)-1);
            } else {
                letter += columnNumToLetter((columnNum-columnNum%26)/26);
            }
        }
        letter += letters[(columnNum-1)%26];
        return letter;
    }
}
