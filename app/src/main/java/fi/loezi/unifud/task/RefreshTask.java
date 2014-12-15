package fi.loezi.unifud.task;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.loezi.unifud.R;
import fi.loezi.unifud.RestaurantListAdapter;
import fi.loezi.unifud.RestaurantListFragment;
import fi.loezi.unifud.model.Meal;
import fi.loezi.unifud.model.Menu;
import fi.loezi.unifud.model.Restaurant;
import fi.loezi.unifud.util.MessiApiHelper;
import fi.loezi.unifud.RestaurantListPagerAdapter;
import fi.loezi.unifud.util.StringUtil;

public class RefreshTask extends AsyncTask<Void, Integer, List<Restaurant>> {

    private static final String BASE_URL = "http://messi.hyyravintolat.fi/publicapi/";

    private final HttpClient client;
    private final SharedPreferences preferences;
    private final ProgressBar progressBar;
    private final Activity caller;

    public RefreshTask(final Activity caller) {

        this.client = new DefaultHttpClient();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(caller.getBaseContext());
        this.progressBar = (ProgressBar) caller.findViewById(R.id.progressBar);
        this.caller = caller;
    }

    @Override
    protected List<Restaurant> doInBackground(final Void... params) {

        publishProgress(0, 1);

        final String json = getJson(BASE_URL + "restaurants");

        final List<Restaurant> restaurants = new ArrayList<Restaurant>();
        try {
            final JSONArray restaurantArray = new JSONObject(json).getJSONArray("data");

            publishProgress(1, restaurantArray.length() + 1);

            for (int i = 0; i < restaurantArray.length(); i++) {

                final JSONObject restaurantJSON = restaurantArray.getJSONObject(i);
                final Restaurant restaurant = new Restaurant();

                final int areaCode = restaurantJSON.getInt("areacode");
                if(!preferences.getBoolean("show_" + MessiApiHelper.getCampus(areaCode), true)) {
                    continue;
                }

                restaurant.setAreaCode(areaCode);
                restaurant.setId(restaurantJSON.getInt("id"));
                restaurant.setName(restaurantJSON.getString("name"));

                getRestaurantDetails(restaurant);

                restaurants.add(restaurant);

                publishProgress(i + 2, restaurantArray.length() + 1);
            }

        } catch (JSONException exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
        }

        Collections.sort(restaurants);

        return restaurants;
    }

    private void getRestaurantDetails(final Restaurant restaurant) {

        final String json = getJson(BASE_URL + "restaurant/" + restaurant.getId());

        parseRestaurantInformation(restaurant, json);

        final List<Menu> menus = parseMenus(json);
        restaurant.setMenus(menus);
    }

    private void parseRestaurantInformation(final Restaurant restaurant, final String json) {

        final JSONObject informationObject;
        final JSONObject businessObject;
        final JSONObject lunchObject;
        try {
            informationObject = new JSONObject(json).getJSONObject("information");
            businessObject = informationObject.getJSONObject("business");
            lunchObject = informationObject.getJSONObject("lounas");
        } catch (Exception exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
            return;
        }

        final String address = getAddressString(informationObject);
        if (address == null) {
            return;
        } else {
            restaurant.setAddress(address);
        }

        final String businessRegular = getRegularHours(businessObject);
        if (businessRegular == null) {
            return;
        } else {
            restaurant.setBusinessRegular(businessRegular);
        }

        final String businessException = getExceptions(businessObject);
        if (businessException == null) {
            return;
        } else {
            restaurant.setBusinessException(businessException);
        }

        final String lunchRegular = getRegularHours(lunchObject);
        if (lunchRegular == null) {
            return;
        } else {
            restaurant.setLunchRegular(lunchRegular);
        }

        final String lunchException = getExceptions(lunchObject);
        if (lunchException != null) {
            restaurant.setLunchException(lunchException);
        }
    }

    private String getAddressString(final JSONObject informationObject) {

        final String address;
        try {
            address = informationObject.getString("address")
                    + "\n"
                    + informationObject.getString("zip")
                    + " "
                    + informationObject.getString("city");
        } catch (Exception exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
            return null;
        }

        return address;
    }

    private String getRegularHours(final JSONObject informationObject) {

        String regularHoursString = "";
        try {
            final JSONArray regularHoursArray = informationObject.getJSONArray("regular");

            for (int i = 0; i < regularHoursArray.length(); i++) {

                final JSONObject regularHoursObject = regularHoursArray.getJSONObject(i);

                final JSONArray whenArray = regularHoursObject.getJSONArray("when");
                final List<String> days = new ArrayList<String>();

                for (int j = 0; j < whenArray.length(); j++) {
                    try {
                        final String day = whenArray.getString(j);
                        if (day.equals("previous") || day.equals("false")) {
                            continue;
                        } else {
                            days.add(day);
                        }
                    } catch (Exception exception) {
                        // Was boolean literal False, continue.
                        continue;
                    }
                }

                regularHoursString += StringUtil.toCommaSeparatedValues(days)
                        + ": "
                        + regularHoursObject.getString("open")
                        + " - "
                        + regularHoursObject.getString("close");

                if (i < regularHoursArray.length() - 1) {
                    //not last round
                    regularHoursString += "\n";
                }
            }
        } catch (Exception exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
            return null;
        }

        return regularHoursString;
    }

    private String getExceptions(JSONObject informationObject) {

        String exceptionHoursString = "";

        try {
            final JSONArray exceptionsArray = informationObject.getJSONArray("exception");

            for (int i = 0; i < exceptionsArray.length(); i++) {

                final JSONObject exceptionObject = exceptionsArray.getJSONObject(i);

                final String from = exceptionObject.getString("from");
                final String to = exceptionObject.getString("to");

                if (from.equals("null") && to.equals("null")) {
                    continue;
                }

                exceptionHoursString += from;

                if (!to.equals("null")) {
                    exceptionHoursString += " - " +  to;
                }

                exceptionHoursString += ": ";

                final String open = exceptionObject.getString("open");
                final String close = exceptionObject.getString("close");
                final boolean closed = exceptionObject.getBoolean("closed");

                if (closed) {
                    exceptionHoursString += "closed";
                } else {
                    exceptionHoursString += open + " - " + close;
                }

                if (i < exceptionsArray.length() - 1) {
                    //not last round
                    exceptionHoursString += "\n";
                }
            }
        } catch (Exception exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
            return null;
        }

        return exceptionHoursString;

    }

    private List<Menu> parseMenus(String json) {

        final List<Menu> menus = new ArrayList<Menu>();

        final JSONArray dateArray;
        try {
            dateArray = new JSONObject(json).getJSONArray("data");
        } catch (Exception exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
            return menus;
        }

        for (int i = 0; i < 14; i++) {

            final Menu menu = new Menu();
            try {
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

            } catch (JSONException exception) {
                Log.e("RefreshTask", "Failed to fetch/parse JSON" + exception);
            }

            menus.add(menu);
        }

        return menus;
    }

    private String getJson(final String url) {

        final HttpGet request = new HttpGet(url);

        final HttpResponse response;
        try {
            response = client.execute(request);
        } catch (Exception exception) {
            Log.e("RefreshTask", "Failure when communicating with remote server: " + exception);
            return null;
        }


        try {
            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception exception) {
            Log.e("RefreshTask", "Failed to read remote server response: " + exception);
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(final Integer... progress) {

        progressBar.setProgress(progress[0]);
        progressBar.setMax(progress[1]);
    }

    @Override
    protected void onPreExecute() {

        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(final List<Restaurant> restaurants) {

        final ViewPager pager = (ViewPager) caller.findViewById(R.id.pager);
        final RestaurantListPagerAdapter pagerAdapter = (RestaurantListPagerAdapter) pager.getAdapter();

        for (int i = 0; i < pagerAdapter.getCount(); i++) {

            RestaurantListFragment fragment = (RestaurantListFragment) pagerAdapter.getItem(i);
            RestaurantListAdapter listAdapter = fragment.getListAdapter();

            if (listAdapter != null) {

                List<Restaurant> listData = listAdapter.getRestaurants();
                listData.clear();
                listData.addAll(restaurants);

                listAdapter.notifyDataSetChanged();

                if (fragment.shouldExpandGroups()) {
                    fragment.expandGroups();
                }
            }
        }

        pagerAdapter.notifyDataSetChanged();
        pager.invalidate();

        progressBar.setVisibility(View.INVISIBLE);
    }
}
