package fi.loezi.unifud;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import fi.loezi.unifud.model.Meal;
import fi.loezi.unifud.model.Menu;
import fi.loezi.unifud.model.Restaurant;

public class RestaurantListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<Restaurant> restaurants;
    private final int dateOffset;

    public RestaurantListAdapter(final Context context,
                                 final List<Restaurant> restaurants,
                                 final int dateOffset) {

        this.context = context;
        this.restaurants = restaurants;
        this.dateOffset = dateOffset;
    }

    @Override
    public Object getChild(final int groupPosition, final int childPosition) {

        final Menu menu = restaurants.get(groupPosition).getMenus().get(dateOffset);

        if (childPosition >= menu.getMeals().size()) {
            return null;
        } else {
            return menu.getMeals().get(childPosition);
        }
    }

    @Override
    public long getChildId(final int groupPosition, final int childPosition) {

        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition,
                             final int childPosition,
                             final boolean isLastChild,
                             View convertView,
                             final ViewGroup parent) {

        final Meal meal = (Meal) getChild(groupPosition, childPosition);

        if (convertView == null) {
            final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.meal_view, null);
        }

        final TextView restaurantMeal = (TextView) convertView.findViewById(R.id.RestaurantMeal);

        if (meal == null) {
            restaurantMeal.setText("<no meals>");
        } else {
            restaurantMeal.setText(meal.toString());
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(final int groupPosition) {

        final int count = restaurants.get(groupPosition).getMenus().get(dateOffset).getMeals().size();

        if (count == 0) {
            return 1;
        } else {
            return count;
        }
    }

    @Override
    public Object getGroup(final int groupPosition) {

        return restaurants.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return this.restaurants.size();
    }

    @Override
    public long getGroupId(final int groupPosition) {

        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition,
                             final boolean isExpanded,
                             View convertView,
                             final ViewGroup parent) {

        final Restaurant restaurant = (Restaurant) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.restaurant_view, null);
        }

        final TextView restaurantName = (TextView) convertView.findViewById(R.id.restaurantName);
        restaurantName.setTypeface(null, Typeface.BOLD);
        restaurantName.setText(restaurant.toString());

        final ImageView infoButton = (ImageView) convertView.findViewById(R.id.infoButton);
        infoButton.setOnClickListener(new RestaurantClickListener(context, restaurant));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {

        return false;
    }

    @Override
    public boolean isChildSelectable(final int groupPosition, final int childPosition) {

        return true;
    }

    public List<Restaurant> getRestaurants() {

        return this.restaurants;
    }
}
