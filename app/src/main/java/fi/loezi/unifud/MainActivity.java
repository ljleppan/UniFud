package fi.loezi.unifud;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.loezi.unifud.Service.RestaurantCampusService;
import fi.loezi.unifud.Service.UnicafeAPI;
import fi.loezi.unifud.model.Restaurant;

public class MainActivity extends Activity {

    private final UnicafeAPI api = new UnicafeAPI();

    private SharedPreferences preferences;
    private ExpandableListAdapter listAdapter;
    private ExpandableListView restaurantList;
    private List<String> restaurants;
    private Map<String, List<String>> meals;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {

        super.onResume();

        preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        restaurantList = (ExpandableListView) findViewById(R.id.restaurantList);

        refresh();

        listAdapter = new ExpandableListAdapter(this, restaurants, meals);
        restaurantList.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                return true;

            case R.id.action_settings:
                showSettingsDialog();
                return true;

            case R.id.action_about:
                showAboutDialog();
                return true;

            default:
                return false;
        }
    }

    private void refresh() {

        final List<Restaurant> newRestaurants;
        try {
            newRestaurants = filterByCampus(api.get());
        } catch (IOException e) {
            Log.e("MainActivity", "Error refreshing data");
            e.printStackTrace();
            return;
        }

        restaurants = new ArrayList<String>();
        meals = new HashMap<String, List<String>>();

        for (Restaurant restaurant : newRestaurants) {
            restaurants.add(restaurant.getName());
            meals.put(restaurant.getName(), restaurant.getMeals());
        }
    }

    private List<Restaurant> filterByCampus(final List<Restaurant> restaurants) {

        final Map<String, String> restaurantCampuses = RestaurantCampusService.getRestaurantCampusMap();

        final List<Restaurant> filteredList = new ArrayList<Restaurant>();

        for(Restaurant restaurant : restaurants) {

            final String campus = restaurantCampuses.get(restaurant.getName());

            if(preferences.getBoolean("show_" + campus, false)) {
                filteredList.add(restaurant);
            }
        }

        return filteredList;
    }

    public void showAboutDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_fragment);
        dialog.setTitle("UniFud");

        final TextView text = (TextView) dialog.findViewById(R.id.dialogText);
        text.setText("Created by Leo Leppänen (leo.leppanen@helsinki.fi)\n" +
                "\n" +
                "Data provided by HYY Ravintolat\n" +
                "\n" +
                "Icons by Freepik and Icon Works from www.flaticon.com, licensed under CC BY 3.0"
        );

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void showSettingsDialog() {

        final Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
