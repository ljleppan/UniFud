package fi.loezi.unifud.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fi.loezi.unifud.model.Meal;
import fi.loezi.unifud.model.Menu;
import fi.loezi.unifud.model.Restaurant;

public class MessiApiParser {

    public static List<Restaurant> parseRestaurants(final String json) throws JSONException{

        final List<Restaurant> restaurants = new ArrayList<Restaurant>();

        final JSONArray restaurantArray = new JSONObject(json).getJSONArray("data");

        for (int i = 0; i < restaurantArray.length(); i++) {

            final JSONObject restaurantJSON = restaurantArray.getJSONObject(i);
            final Restaurant restaurant = new Restaurant();

            restaurant.setAreaCode(restaurantJSON.getInt("areacode"));
            restaurant.setId(restaurantJSON.getInt("id"));
            restaurant.setName(restaurantJSON.getString("name"));

            restaurants.add(restaurant);
        }

        return restaurants;
    }

    public static void addRestaurantDetailsFromJSON(final Restaurant restaurant, final String json) throws JSONException {

        final JSONObject restaurantObject = new JSONObject(json);

        addRestaurantAddressFromJSON(restaurant, restaurantObject);
        addRestaurantHoursFromJSON(restaurant, restaurantObject);
        addMenusFromJSON(restaurant, restaurantObject);
    }

    private static void addRestaurantAddressFromJSON(final Restaurant restaurant, final JSONObject restaurantObject) throws JSONException{

        final JSONObject informationObject = restaurantObject.getJSONObject("information");

        final String address =  informationObject.getString("address")
                    + "\n"
                    + informationObject.getString("zip")
                    + " "
                    + informationObject.getString("city");

        restaurant.setAddress(address);
    }

    private static void addRestaurantHoursFromJSON(final Restaurant restaurant, final JSONObject restaurantObject) throws JSONException {

        final JSONObject informationObject = restaurantObject.getJSONObject("information");

        final JSONObject businessObject = informationObject.getJSONObject("business");
        restaurant.setBusinessRegular(getRegularHours(businessObject));
        restaurant.setBusinessException(getExceptionHours(businessObject));

        final JSONObject lunchObject = informationObject.getJSONObject("lounas");
        restaurant.setLunchRegular(getRegularHours(lunchObject));
        restaurant.setLunchException(getExceptionHours(lunchObject));

        final JSONObject bistroObject = informationObject.getJSONObject("bistro");
        restaurant.setBistroRegular(getRegularHours(bistroObject));
        restaurant.setBistroException(getExceptionHours(bistroObject));
    }

    private static String getRegularHours(final JSONObject informationObject) throws JSONException{

        String regularHoursString = "";

        final JSONArray regularHoursArray = informationObject.getJSONArray("regular");

        for (int i = 0; i < regularHoursArray.length(); i++) {

            final JSONObject regularHoursObject = regularHoursArray.getJSONObject(i);

            final JSONArray whenArray = regularHoursObject.getJSONArray("when");
            final List<String> days = new ArrayList<String>();

            for (int j = 0; j < whenArray.length(); j++) {

                final String day = whenArray.getString(j);

                if (!day.equals("previous") && !day.equals("false")) {
                    days.add(day);
                }
            }

            final String dayString = StringUtil.toCommaSeparatedValues(days);
            if (!dayString.trim().isEmpty()) {
                regularHoursString += dayString;
            }

            final String open = regularHoursObject.getString("open");
            final String close = regularHoursObject.getString("close");

            String timeString = null;
            if (!open.equals("null")
                    && !open.isEmpty()
                    && !close.equals("null")
                    && !close.isEmpty()) {

                timeString = regularHoursObject.getString("open")
                        + " - "
                        + regularHoursObject.getString("close");
            }

            if (timeString != null) {
                if (!dayString.isEmpty()) {
                    regularHoursString += ": ";
                }
                regularHoursString += timeString;
            }

            if (i < regularHoursArray.length() - 1) {
                //not last round
                regularHoursString += "\n";
            }
        }

        return regularHoursString;
    }

    private static String getExceptionHours(JSONObject informationObject) throws JSONException {

        String exceptionHoursString = "";

        final JSONArray exceptionsArray = informationObject.getJSONArray("exception");

        for (int i = 0; i < exceptionsArray.length(); i++) {


            final JSONObject exceptionObject = exceptionsArray.getJSONObject(i);

            final String from = exceptionObject.getString("from");
            final String to = exceptionObject.getString("to");

            if (!from.equals("null") && !from.isEmpty()) {
                exceptionHoursString += from;

                if (!to.equals("null") && !to.isEmpty()) {
                    exceptionHoursString += " - " +  to;
                }

                exceptionHoursString += ": ";
            }

            final String open = exceptionObject.getString("open");
            final String close = exceptionObject.getString("close");
            final boolean closed = exceptionObject.getBoolean("closed");

            if (closed) {
                exceptionHoursString += "closed";
            } else if (!open.equals("null")
                    && !open.isEmpty()
                    && !close.equals("null")
                    && !close.isEmpty()) {
                exceptionHoursString += open + " - " + close;
            }

            if (i < exceptionsArray.length() - 1) {
                //Not last round
                if (!exceptionHoursString.endsWith("\n")) {
                    //Previous round added something
                    exceptionHoursString += "\n";
                }
            }
        }

        return exceptionHoursString;
    }

    private static void addMenusFromJSON(final Restaurant restaurant, final JSONObject restaurantObject) throws JSONException {

        final List<Menu> menus = new ArrayList<Menu>();

        final JSONArray dateArray = restaurantObject.getJSONArray("data");

        for (int i = 0; i < 14; i++) {

            final Menu menu = new Menu();

            final JSONObject menuObject = dateArray.getJSONObject(i);
            menu.setDate(menuObject.getString("date_en"));
            menu.setMeals(new ArrayList<Meal>());

            final JSONArray mealArray = menuObject.getJSONArray("data");
            for (int j = 0; j < mealArray.length(); j++) {

                final JSONObject meal = mealArray.getJSONObject(j);

                final String price = meal.getJSONObject("price").getString("name");
                final String name = meal.getString("name_en");
                final String ingredients = meal.getString("ingredients");
                final String nutrition = meal.getString("nutrition");

                final JSONObject metaInfo = meal.getJSONObject("meta");

                final List<String> diets = new ArrayList<String>();
                final JSONArray dietsArray = metaInfo.getJSONArray("0");
                for(int k = 0; k < dietsArray.length(); k++) {
                    diets.add(dietsArray.getString(k));
                }

                final List<String> specialContents = new ArrayList<String>();
                final JSONArray specialContentsArray = metaInfo.getJSONArray("1");
                for(int k = 0; k < specialContentsArray.length(); k++) {
                    specialContents.add(specialContentsArray.getString(k));
                }

                final List<String> notes = new ArrayList<String>();
                final JSONArray notesArray = metaInfo.getJSONArray("2");
                for(int k = 0; k < notesArray.length(); k++) {
                    notes.add(notesArray.getString(k));
                }

                menu.getMeals().add(new Meal(price, name, ingredients, nutrition, diets, specialContents, notes));
            }

            menus.add(menu);
        }

        restaurant.setMenus(menus);
    }
}
