package fi.loezi.unifud.util;

import android.util.SparseArray;

import java.util.Calendar;

public class MessiApiHelper {

    public static final int DAYS_VISIBLE = 14;
    public static final String HIGH_PRICE_INDICATOR = "maukkaasti";

    private final static SparseArray<String> campuses;

    static {
        campuses = new SparseArray<String>();

        campuses.put(1, "keskusta");
        campuses.put(2, "kumpula");
        campuses.put(3, "meilahti");
        campuses.put(5, "viikki");
        campuses.put(6, "metropolia");
        campuses.put(999, "finedining");
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
