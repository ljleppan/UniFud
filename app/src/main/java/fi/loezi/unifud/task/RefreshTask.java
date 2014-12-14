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

                final JSONObject restaurant = restaurantArray.getJSONObject(i);

                final int areaCode = restaurant.getInt("areacode");
                if(!preferences.getBoolean("show_" + MessiApiHelper.getCampus(areaCode), false)) {
                    continue;
                }

                final int id = restaurant.getInt("id");
                final String name = restaurant.getString("name");
                final List<Menu> menus = getMenus(id);

                restaurants.add(new Restaurant(areaCode, id, name, menus));

                publishProgress(i + 2, restaurantArray.length() + 1);
            }

        } catch (JSONException exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
        }

        Collections.sort(restaurants);

        return restaurants;
    }

    private List<Menu> getMenus(final int restaurantId) {

        final List<Menu> menus = new ArrayList<Menu>();
        final String json = getJson(BASE_URL + "restaurant/" + restaurantId);

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

        final String json;
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

                Log.d("RefreshTask", "listData contains " + listData.size() + " restaurants");

                listAdapter.notifyDataSetChanged();

                fragment.maybeExpandList();
            }
        }

        pagerAdapter.notifyDataSetChanged();
        pager.invalidate();

        progressBar.setVisibility(View.INVISIBLE);
    }
}
