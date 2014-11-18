package fi.loezi.unifud;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> restaurantNames;
    private Map<String, List<String>> restaurantMeals;

    public ExpandableListAdapter(final Context context,
                                 final List<String> restaurantNames,
                                 final Map<String, List<String>> restaurantMeals) {

        this.context = context;
        this.restaurantNames = restaurantNames;
        this.restaurantMeals = restaurantMeals;
    }

    @Override
    public Object getChild(final int groupPosition, final int childPosititon) {

        final String restaurantName = restaurantNames.get(groupPosition);
        final String meal = restaurantMeals.get(restaurantName).get(childPosititon);

        return meal;
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

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            final LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.meal_view, null);
        }

        final TextView restaurantMeal = (TextView) convertView.findViewById(R.id.RestaurantMeal);
        restaurantMeal.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(final int groupPosition) {

        final String restaurantName = restaurantNames.get(groupPosition);
        final List<String> meals = restaurantMeals.get(restaurantName);

        return meals.size();
    }

    @Override
    public Object getGroup(final int groupPosition) {

        return this.restaurantNames.get(groupPosition);
    }

    @Override
    public int getGroupCount() {

        return this.restaurantNames.size();
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

        final String headerTitle = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.restaurant_view, null);
        }

        TextView restaurantName = (TextView) convertView.findViewById(R.id.restaurantName);
        restaurantName.setTypeface(null, Typeface.BOLD);
        restaurantName.setText(headerTitle);

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
}
