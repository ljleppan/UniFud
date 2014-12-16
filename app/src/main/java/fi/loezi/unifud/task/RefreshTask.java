package fi.loezi.unifud.task;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fi.loezi.unifud.R;
import fi.loezi.unifud.RestaurantListAdapter;
import fi.loezi.unifud.RestaurantListFragment;
import fi.loezi.unifud.model.Restaurant;
import fi.loezi.unifud.util.HttpWorker;
import fi.loezi.unifud.util.MessiApiParser;
import fi.loezi.unifud.util.MessiApiHelper;
import fi.loezi.unifud.RestaurantListPagerAdapter;

public class RefreshTask extends AsyncTask<Void, Integer, List<Restaurant>> {

    private static final String BASE_URL = "http://messi.hyyravintolat.fi/publicapi/";

    private final Activity caller;
    private final HttpWorker http;
    private final SharedPreferences preferences;
    private final ProgressBar progressBar;

    private Exception exception;

    public RefreshTask(final Activity caller) {

        this.caller = caller;
        this.http = new HttpWorker();
        this.preferences = PreferenceManager.getDefaultSharedPreferences(caller.getBaseContext());
        this.progressBar = (ProgressBar) caller.findViewById(R.id.progressBar);
    }

    @Override
    protected List<Restaurant> doInBackground(final Void... params) {

        publishProgress(0, 1);

        final String json;
        try {
            json = http.getJson(BASE_URL + "restaurants");
        } catch (IOException exception) {
            this.exception = exception;
            return null;
        }

        List<Restaurant> restaurants;
        try {
            restaurants = MessiApiParser.parseRestaurants(json);
        } catch (JSONException exception) {
            this.exception = exception;
            return null;
        }

        restaurants = filterByCampus(restaurants);
        Collections.sort(restaurants);

        int progress = 1;
        for (Restaurant restaurant : restaurants) {

            publishProgress(progress, restaurants.size() + 1);
            progress++;

            try {
                addRestaurantDetails(restaurant);
            } catch (IOException exception) {
                this.exception = exception;
                return null;
            } catch (JSONException exception) {
                this.exception = exception;
                return null;
            }
        }

        return restaurants;
    }

    private List<Restaurant> filterByCampus(List<Restaurant> restaurants) {

        final List<Restaurant> filtered = new ArrayList<Restaurant>();

        for (Restaurant restaurant : restaurants) {

            final int areaCode = restaurant.getAreaCode();
            if(preferences.getBoolean("show_" + MessiApiHelper.getCampus(areaCode), true)) {
                filtered.add(restaurant);
            }
        }

        return filtered;
    }

    private void addRestaurantDetails(final Restaurant restaurant) throws IOException, JSONException {

        final String json = http.getJson(BASE_URL + "restaurant/" + restaurant.getId());
        MessiApiParser.addRestaurantDetailsFromJSON(restaurant, json);
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
