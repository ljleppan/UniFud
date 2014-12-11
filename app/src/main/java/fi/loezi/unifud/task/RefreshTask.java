package fi.loezi.unifud.task;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
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

import fi.loezi.unifud.ExpandableListAdapter;
import fi.loezi.unifud.R;
import fi.loezi.unifud.model.Meal;
import fi.loezi.unifud.model.Restaurant;
import fi.loezi.unifud.util.MessiApiHelper;

public class RefreshTask extends AsyncTask<Integer, Integer, List<Restaurant>> {

    private static final String BASE_URL = "http://messi.hyyravintolat.fi/publicapi/";

    private final HttpClient client;
    private final SharedPreferences preferences;
    private final ExpandableListView restaurantList;
    private final ProgressBar progressBar;
    private final Activity caller;

    public RefreshTask(final Activity caller) {

        this.client = new DefaultHttpClient();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(caller.getBaseContext());
        this.restaurantList = (ExpandableListView) caller.findViewById(R.id.restaurantList);
        this.progressBar = (ProgressBar) caller.findViewById(R.id.progressBar);
        this.caller = caller;
    }

    @Override
    protected List<Restaurant> doInBackground(final Integer... params) {

        final String json = getJson(BASE_URL + "restaurants");

        final List<Restaurant> restaurants = new ArrayList<Restaurant>();
        try {
            final JSONArray restaurantArray = new JSONObject(json).getJSONArray("data");

            publishProgress(1, restaurantArray.length() + 1);

            for (int i = 0; i < restaurantArray.length(); i++) {

                final JSONObject restaurant = restaurantArray.getJSONObject(i);

                final int areacode = restaurant.getInt("areacode");
                if(!preferences.getBoolean("show_" + MessiApiHelper.getCampus(areacode), false)) {
                    continue;
                }

                final int id = restaurant.getInt("id");
                final String name = restaurant.getString("name");
                final List<Meal> menu = getMenu(id, params[0]);

                restaurants.add(new Restaurant(areacode, id, name, menu));

                publishProgress(i + 2, restaurantArray.length() + 1);
            }

        } catch (JSONException exception) {
            Log.e("RefreshTask", "Failed to parse JSON: " + exception);
        }

        Collections.sort(restaurants);

        return restaurants;
    }

    private List<Meal> getMenu(final int restaurantId, final int dayOffset) {

        final String json = getJson(BASE_URL + "restaurant/" + restaurantId);

        final List<Meal> meals = new ArrayList<Meal>();
        try {
            final JSONArray dateArray = new JSONObject(json).getJSONArray("data");
            final JSONArray mealArray = dateArray.getJSONObject(dayOffset).getJSONArray("data");

            for (int i = 0; i < mealArray.length(); i++) {

                final JSONObject meal = mealArray.getJSONObject(i);

                final String price = meal.getJSONObject("price").getString("name");
                final String name = meal.getString("name_en");

                meals.add(new Meal(price, name));

            }

        } catch (JSONException exception) {
            Log.e("RefreshTask", "Failed to fetch/parse JSON" + exception);
        }

        return meals;
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

        final ExpandableListAdapter listAdapter = new ExpandableListAdapter(caller, restaurants);
        restaurantList.setAdapter(listAdapter);

        if (preferences.getBoolean("start_open", false)) {
            for (int i = 0; i < listAdapter.getGroupCount(); i++) {
                restaurantList.expandGroup(i);
            }
        }

        progressBar.setVisibility(View.INVISIBLE);
    }
}
