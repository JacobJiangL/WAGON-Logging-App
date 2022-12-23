import java.util.ArrayList;
import java.lang.StringBuilder;
import java.util.Arrays;

public class Reader {
    protected ArrayList<String> amountUnits = new ArrayList<>() {
        { // unit keywords to be recognized in responses
            add("cups"); add("cup");
            add("boxes"); add("box");
            add("trays"); add("tray");
            add("cans"); add("can");
            add("bags"); add("bag");
            add("pans"); add("pan");
            add("packs"); add("pack");
            add("package"); add("packages");
            add("jar"); add("jar");
        }
    };
    protected ArrayList<String> weightUnits = new ArrayList<>() {
        { // weight unit keywords to be recognized in responses
            add("lbs"); add("lb");
            add("pounds"); add("pound");

            add("ounces"); add("ounce");
            add("grams"); add("gram");
        }
    };
    protected ArrayList<String> numbers = new ArrayList<>() {
        { // currently unimplemented
            add("one"); add("two"); add("three"); add("four"); add("five"); //1-5
            add("six"); add("seven"); add("eight"); add("nine"); add("ten"); //6-10
            add("eleven"); add("twelve"); add("thirteen"); add("fourteen"); add("fifteen"); //11-15
            add("sixteen"); add("seventeen"); add("eighteen"); add("nineteen"); add("twenty"); //16-20
        }
    };

    public Object[][] splitAndProcessResponses(String r) {
        String[] unfilteredResponses = r.split("\n");
        ArrayList<String> responses = new ArrayList<>();

        for(String response : unfilteredResponses) {
            if(!response.trim().equals("")) {
                responses.add(response);
            }
        }

        Object[][] processResponses = new Object[responses.size()][];

        for(int i=0; i < responses.size(); i++) {
            processResponses[i] = processResponse(responses.get(i));
        }
        return processResponses;
    }

    public Object[] processResponse(String r) {
        String response = r.toLowerCase().trim().replaceAll(" +", " ");
        ArrayList<String> separatedResponse = new ArrayList<>(Arrays.asList(response.split(" ")));
        Object[] processedResponse = new Object[5]; // 5 slots - 1: food, 2: amount of food, 3: unit of food, 4: amount of weight, 5: unit of weight
        for(String unit : amountUnits) { // parse through each amount unit
            int[] numOfUnit = {-999,-999};
            for(int i = 0; i<separatedResponse.size(); i++) {
                String word = separatedResponse.get(i);
                if(word.equals(unit)) {
                    if(i != 0) {
                        numOfUnit = findNumInString(separatedResponse.get(i-1), false);
//                        separatedResponse.set(i - 1, deleteNumFromString(separatedResponse.get(i-1), numOfUnit));
                        processedResponse[1] = numOfUnit[1];
                    }
                    processedResponse[2] = unit;
                    separatedResponse.remove(i); // remove unit word after logging it
                    if(numOfUnit[0]>=0) {
                        separatedResponse.remove(i-1);  // delete number from word before
                    }


                    if(numOfUnit[1] >= 0) { // set slot 2
                        processedResponse[1] = numOfUnit[1];
                    } else {
                        processedResponse[1] = null; // set value as null in case of -999 (no number)
                    }
                }
            }
            response = String.join(" ", separatedResponse);

        }
        for(String unit : weightUnits) { // parse through each weight unit
            int[] numOfUnit = {-999,-999};
            for(int i = 0; i<separatedResponse.size(); i++) { // parse through each word of split response, checking if it matches weight unit
                String word = separatedResponse.get(i);
                if(word.equals(unit)) {
                    if(i != 0) {
                        numOfUnit = findNumInString(separatedResponse.get(i-1), false); // check if word unit keyword before has a number
//                        separatedResponse.set(i - 1, deleteNumFromString(separatedResponse.get(i-1), numOfUnit));

                        processedResponse[3] = numOfUnit[1]; // set as -999 if no number
                    }
                    processedResponse[4] = unit;
                    separatedResponse.remove(i); // remove unit word after logging it
                    if(numOfUnit[0]>=0) {
                        separatedResponse.remove(i-1);  // delete number from word before
                    }

                    if(numOfUnit[1] >= 0) {          // set slot 4 (num of food)
                        processedResponse[3] = numOfUnit[1];
                    } else {
                        processedResponse[3] = null; // set value as null in case of -999 (no number)
                    }
                }
            }
            response = String.join(" ", separatedResponse);
        }
        response = response.trim();
        while (response.startsWith("of ") || response.startsWith(",") || response.startsWith(".")) {
            if(response.startsWith("of ") || response.startsWith("of,")) {                                    // trim leading "of"s
                response = response.substring(2).trim();
            } else if (response.startsWith(",")) {                              // trim leading ","s
                response = response.substring(1).trim();
            } else if (response.startsWith(".")) {                              // trim leading "."s
                response = response.substring(1).trim();
            }

        }
        while (response.endsWith(" of") || response.endsWith(",") || response.endsWith(".")) {
            if(response.endsWith(" of") || response.endsWith(",of")) {                                        // trim trailing "of"s
                response = response.substring(0, response.length() - 2).trim();
            } else if (response.endsWith(",")) {                                // trim trailing ","s
                response = response.substring(0, response.length() - 1).trim();
            } else if (response.endsWith(".")) {                                // trim trailing "."s
                response = response.substring(0, response.length() - 1).trim();
            }
        }
        if(findNumInString(response, true)[1] >= 0 && processedResponse[1] == null) { // if (number with no associated keyword) && (number of item hasn't been assigned), then assign number as the number of item
            processedResponse[1] = findNumInString(response, true)[1];
            response = deleteNumFromString(response, findNumInString(response, true)).trim();
        } else if (processedResponse[1] == null) { // default number of item as 1
            processedResponse[1] = 1;
        }
        processedResponse[0] = capitalizeWordsInString(response); // capitalize food item name (remaining part of response) & log it
        return processedResponse;
    }
    private int[] findNumInString(String response, boolean startFromBeginning) { // check if a number is in a string and return its index + the number itself
        if(response.equals("")) { // error when no name for item cause everything else is removed and then str is just "" (can't have charAt(0) of empty str)
            return new int[]{-999, -999};
        }

        if(startFromBeginning) { // check start of string
            boolean isNum = false;
            int numEndingIndex = 0;
            while ("0123456789".indexOf(response.charAt(numEndingIndex)) != -1) { // parse backward to find connected number characters
                isNum = true;

                if(numEndingIndex < response.length() - 1) { // measure to prevent creation of out of bounds exception
                    numEndingIndex += 1;
                } else {
                    numEndingIndex = response.length();
                    break;
                }
            }
            if (isNum) {
                return new int[]{0, Integer.parseInt(response.substring(0, numEndingIndex))}; // starting index always 0
            } else {
                return new int[]{-999, -999};
            }
        } else { // check end of string
            try {
                boolean isNum = false;
                int numStartingIndex = response.length() - 1;
                while ("0123456789".indexOf(response.charAt(numStartingIndex)) != -1) { // parse backward to find connected number characters
                    isNum = true; // if number unit is found mark that there is a number

                    if (numStartingIndex > 0) {
                        numStartingIndex -= 1;
                    } else {
                        numStartingIndex = -1; // to counter the following "index += 1" in the case where it should = 0
                        break;
                    }
                }
                numStartingIndex += 1; // algorithm index is always 1 less than should be
                if (isNum) {
                    return new int[]{numStartingIndex,
                            Integer.parseInt(response.substring(numStartingIndex))}; // grab full number
                } else { // if there is something but no number, return no number
                    return new int[]{-999, -999};
                }
            } catch (IndexOutOfBoundsException e) { // if there is null before keyword, return no number
                return new int[]{-999, -999};
            }
        }
    }
    private String deleteNumFromString(String string, int[] numberData) {
        StringBuilder builder = new StringBuilder(string);
        try {
            builder.delete(numberData[0], numberData[0] + Integer.toString(numberData[1]).length()); // delete from starting index to last index
            return builder.toString();
        } catch (StringIndexOutOfBoundsException e) {
            return string;
        }
    }
    private String capitalizeWordsInString(String s) { // split on spaces, in each word capitalize char at first index, rejoin w/ spaces
        String[] strings = s.split(" ");
        for (int i = 0; i<strings.length; i++) {
            if(strings[i].length() > 1) {
                strings[i] = strings[i].substring(0, 1).toUpperCase() + strings[i].substring(1);
            } else {
                strings[i] = strings[i].toUpperCase();
            }
        }
        return String.join(" ", strings);
    }

}
