package fi.loezi.unifud.util;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    public static List<String> asStringList(final JSONArray jsonArray) throws JSONException{

        final List<String> strings = new ArrayList<String>();

        for (int i = 0; i < jsonArray.length(); i++) {
            strings.add(jsonArray.getString(i));
        }

        return strings;
    }
}
