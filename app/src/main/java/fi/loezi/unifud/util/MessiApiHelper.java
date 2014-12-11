package fi.loezi.unifud.util;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MessiApiHelper {

    private final static Map<Integer, String> campuses;

    static {
        campuses = new HashMap<Integer, String>();

        campuses.put(1, "keskusta");
        campuses.put(2, "kumpula");
        campuses.put(3, "meilahti");
        campuses.put(5, "viikki");
        campuses.put(5, "metropolia");
    }

    public static String getCampus(int campusId) {

        return campuses.get(campusId);
    }

    public static int getDateOffset() {

        final Calendar now = Calendar.getInstance();
        final int day = now.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                return 0;
            case Calendar.TUESDAY:
                return 1;
            case Calendar.WEDNESDAY:
                return 2;
            case Calendar.THURSDAY:
                return 3;
            case Calendar.FRIDAY:
                return 4;
            case Calendar.SATURDAY:
                return 5;
            case Calendar.SUNDAY:
                return 6;
            default:
                return 0;
        }
    }
}
