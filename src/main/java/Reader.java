import java.util.ArrayList;
import java.lang.StringBuilder;

public class Reader {
    protected ArrayList<String> amountUnits = new ArrayList<>() {
        {
            add("cups"); add("cup");
            add("boxes"); add("box");
            add("trays"); add("tray");
            add("cans"); add("can");
            add("bags"); add("bag");
            add("pans"); add("pan");
        }
    };
    protected ArrayList<String> weightUnits = new ArrayList<>() {
        {
            add("lbs"); add("lb");
            add("pounds"); add("pound");

            add("ounces"); add("ounce");
            add("grams"); add("gram");
        }
    };
    protected ArrayList<String> numbers = new ArrayList<>() {
        {
            add("one"); add("two"); add("three"); add("four"); add("five"); //1-5
            add("six"); add("seven"); add("eight"); add("nine"); add("ten"); //6-10
            add("eleven"); add("twelve"); add("thirteen"); add("fourteen"); add("fifteen"); //11-15
            add("sixteen"); add("seventeen"); add("eighteen"); add("nineteen"); add("twenty"); //16-20
            add("thirty"); add("forty"); add("fifty"); add("sixty"); add("seventy"); //30-70
            add("eighty"); add("ninety"); add("hundred"); add("thousand"); add("million"); //80-100, 1k, 1m



        }
    };

    public Object[] processResponse(String r) {
        String response = r.toLowerCase().trim();
        Object[] processedResponse = new Object[5]; // 5 slots - 1: food, 2: amount of food, 3: unit of food, 4: amount of weight, 5: unit of weight
        for(String unit : amountUnits) { // parse through each amount unit
            int[] numOfUnit = {-999,-999};
            if(response.contains(" " + unit + " ") ||
                    response.contains(" " + unit) || response.contains(unit + " ") ||
                    response.contains("-" + unit) || response.contains(unit + "-")) {

                if (response.contains(" " + unit + " ")) {
                    numOfUnit = findNumBeforeUnit(response, " " + unit + " "); // grab info for slots 2 and 3

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(" " + unit + " ", ""); // remove grabbed info from original text
                } else if (response.contains(" " + unit)) {
                    numOfUnit = findNumBeforeUnit(response, " " + unit);

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(" " + unit, "");
                } else if (response.contains(unit + " ")) {
                    numOfUnit = findNumBeforeUnit(response, unit + " ");

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(unit + " ", "");
                } else if (response.contains("-" + unit)) {
                    numOfUnit = findNumBeforeUnit(response, "-" + unit);

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace("-" + unit, "");
                } else if (response.contains(unit + "-")) {
                    numOfUnit = findNumBeforeUnit(response, unit + "-");

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(unit + "-", "");
                }
                if(numOfUnit[1] >= 0) { // set slot 2
                    processedResponse[1] = numOfUnit[1];
                } else {
                    processedResponse[1] = 1;
                }
                processedResponse[2] = unit; // set slot 3
            }
        }
        for(String unit : weightUnits) { // parse through weight info
            int[] numOfUnit = {-999, -999};
            if(response.contains(" " + unit + " ") ||
                    response.contains(" " + unit) || response.contains(unit + " ") ||
                    response.contains("-" + unit) || response.contains(unit + "-")) {
                if (response.contains(" " + unit + " ")) {
                    numOfUnit = findNumBeforeUnit(response, " " + unit + " "); // grab weight info for slots 4 and 5

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(" " + unit + " ", ""); // remove grabbed info from original text
                } else if (response.contains(" " + unit)) {
                    numOfUnit = findNumBeforeUnit(response, " " + unit);

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(" " + unit, "");
                } else if (response.contains(unit + " ")) {
                    numOfUnit = findNumBeforeUnit(response, unit + " ");

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(unit + " ", "");
                } else if (response.contains("-" + unit)) {
                    numOfUnit = findNumBeforeUnit(response, "-" + unit);

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace("-" + unit, "");
                } else if (response.contains(unit + "-")) {
                    numOfUnit = findNumBeforeUnit(response, unit + "-");

                    response = deleteNumFromString(response, numOfUnit);

                    response = response.replace(unit + "-", "");
                }
                if(numOfUnit[1] >= 0) {
                    processedResponse[3] = numOfUnit[1]; // set slot 4
                } else {
                    processedResponse[3] = null;
                }
                processedResponse[4] = unit; // set slot 5
            }
        }
        response = response.trim();
        while (response.startsWith("of ") || response.startsWith(",")) {
            if(response.startsWith("of ")) { // trim leading "of"s
                response = response.substring(2).trim();
            } else if (response.startsWith(",")) { // trim leading ","s
                response = response.substring(1).trim();
            }

        }
        while (response.endsWith(" of") || response.endsWith(",")) {
            if(response.endsWith(" of")) { // trim trailing "of"s
                response = response.substring(0, response.length() - 2).trim();
            } else if (response.endsWith(",")) { // trim trailing ","s
                response = response.substring(0, response.length() - 1).trim();
            }
        }
        processedResponse[0] = capitalizeWordsInString(response);
        System.out.println(response);
        return processedResponse;
    }
    private int[] findNumBeforeUnit(String response, String unit) {
        try {
            boolean isNum = false;
            int numStartingIndex = response.indexOf(unit) - 1;
            while ("0123456789".indexOf(response.charAt(numStartingIndex)) != -1) { // parse backward to find connected number characters
                isNum = true; // if number unit is found mark that there is a number

                if(numStartingIndex > 0) {
                    numStartingIndex -= 1;
                } else {
                    numStartingIndex = -1;
                    break;
                }
            }
            numStartingIndex += 1;
            if(isNum) {
                return new int[]{numStartingIndex,
                        Integer.parseInt(response.substring(numStartingIndex, response.indexOf(unit)))}; // grab full number
            } else {
                return new int[]{-999,-999};
            }
        } catch (IndexOutOfBoundsException e) {
            return new int[]{-999,-999};
        }
    }
    private String deleteNumFromString(String string, int[] numberData) {
        StringBuilder builder = new StringBuilder(string);
        try {
            builder.delete(numberData[0], numberData[0] + Integer.toString(numberData[1]).length());
            return builder.toString();
        } catch (StringIndexOutOfBoundsException e) {
            return string;
        }
    }
    private String capitalizeWordsInString(String s) {
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
