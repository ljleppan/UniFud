package fi.loezi.unifud.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fi.loezi.unifud.model.ExceptionalHours;
import fi.loezi.unifud.model.Meal;
import fi.loezi.unifud.model.Menu;
import fi.loezi.unifud.model.RegularHours;
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
        restaurant.setBusinessException(getExceptionalHours(businessObject));

        final JSONObject lunchObject = informationObject.getJSONObject("lounas");
        restaurant.setLunchRegular(getRegularHours(lunchObject));
        restaurant.setLunchException(getExceptionalHours(lunchObject));

        final JSONObject bistroObject = informationObject.getJSONObject("bistro");
        restaurant.setBistroRegular(getRegularHours(bistroObject));
        restaurant.setBistroException(getExceptionalHours(bistroObject));
    }

    private static List<RegularHours> getRegularHours(final JSONObject informationObject) throws JSONException{

        List<RegularHours> hours = new ArrayList<RegularHours>();

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

            final String open = regularHoursObject.getString("open");
            final String close = regularHoursObject.getString("close");

            hours.add(new RegularHours(days, open, close));

        }

        return hours;
    }

    private static List<ExceptionalHours> getExceptionalHours(JSONObject informationObject) throws JSONException {

        List<ExceptionalHours> hours = new ArrayList<ExceptionalHours>();

        final JSONArray exceptionsArray = informationObject.getJSONArray("exception");

        for (int i = 0; i < exceptionsArray.length(); i++) {

            final JSONObject exceptionObject = exceptionsArray.getJSONObject(i);

            final String from = exceptionObject.getString("from");
            final String to = exceptionObject.getString("to");
            final String open = exceptionObject.getString("open");
            final String close = exceptionObject.getString("close");
            final boolean closed = exceptionObject.getBoolean("closed");

            hours.add(new ExceptionalHours(from, to, closed, open, close));
        }

        return hours;
    }

    private static void addMenusFromJSON(final Restaurant restaurant, final JSONObject restaurantObject) throws JSONException {

        final List<Menu> menus = new ArrayList<Menu>();
        final JSONArray dateArray = restaurantObject.getJSONArray("data");

        for (int i = 0; i < MessiApiHelper.DAYS_VISIBLE; i++) {

            final JSONObject menuObject = dateArray.getJSONObject(i);

            final Menu menu = new Menu();
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

                final List<String> diets = JsonUtil.asStringList(metaInfo.getJSONArray("0"));
                final List<String> specialContents = JsonUtil.asStringList(metaInfo.getJSONArray("1"));
                final List<String> notes = JsonUtil.asStringList(metaInfo.getJSONArray("2"));

                menu.getMeals().add(new Meal(price, name, ingredients, nutrition, diets, specialContents, notes));
            }

            menus.add(menu);
        }

        restaurant.setMenus(menus);
    }
}
