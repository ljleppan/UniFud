package fi.loezi.unifud;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fi.loezi.unifud.model.Restaurant;

public class RestaurantListFragment extends Fragment {

    private RestaurantListAdapter listAdapter;
    private ExpandableListView list;

    public static RestaurantListFragment newInstance(final int position, final ArrayList<Restaurant> restaurants) {

        final Bundle arguments = new Bundle();
        arguments.putInt("position", position);
        arguments.putParcelableArrayList("restaurants", restaurants);

        final RestaurantListFragment fragment = new RestaurantListFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             final ViewGroup container,
                             final Bundle savedInstanceState) {

        super.onCreateView(inflater, container, savedInstanceState);

        return inflater.inflate(R.layout.restaurant_list_view, container, false);
    }

    @Override
    public void onViewCreated(final View view, final Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        final List<Restaurant> restaurants = getArguments().getParcelableArrayList("restaurants");
        final int position = getArguments().getInt("position");

        String dateString = "No data";
        if (restaurants.size() > 0) {
            dateString = restaurants.get(0).getMenus().get(position).getDate();
        }
        ((TextView) view.findViewById(R.id.dateDisplay)).setText(dateString);

        list = (ExpandableListView) view.findViewById(R.id.restaurantList);

        if (listAdapter == null) {
            listAdapter = new RestaurantListAdapter(getActivity(), restaurants, position);
            list.setAdapter(listAdapter);
        }

        list.setOnItemLongClickListener(new RestaurantListLongClickListener(getActivity(), listAdapter));
        list.setOnChildClickListener(new MealClickListener(getActivity(), listAdapter));

        if(shouldExpandGroups()) {
            expandGroups();
        }
    }

    public RestaurantListAdapter getListAdapter() {

        return this.listAdapter;
    }

    public boolean shouldExpandGroups() {

        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        return preferences.getBoolean("start_open", false);
    }

    public void expandGroups() {

        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            list.expandGroup(i);
        }
    }
}
