package fi.loezi.unifud;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import fi.loezi.unifud.model.Restaurant;
import fi.loezi.unifud.util.StringUtil;


public class RestaurantListLongClickListener implements AdapterView.OnItemLongClickListener {

    private final RestaurantListAdapter adapter;
    private final Context context;

    public RestaurantListLongClickListener(final Context context, final RestaurantListAdapter adapter) {

        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public boolean onItemLongClick(final AdapterView<?> parent,
                                   final View view,
                                   final int position,
                                   final long id) {

        final int itemType = ExpandableListView.getPackedPositionType(id);

        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

            return false;
        }

        if (itemType == ExpandableListView.PACKED_POSITION_TYPE_GROUP) {

            final int groupPosition = ExpandableListView.getPackedPositionGroup(id);
            restaurantLongClick(groupPosition);

            return true;
        }

        return false;
    }

    private void restaurantLongClick(final int groupPosition) {

        final Restaurant restaurant = (Restaurant) adapter.getGroup(groupPosition);

        if (restaurant == null) {
            return;
        }

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.restaurant_info_fragment);

        final TextView nameView = (TextView) dialog.findViewById(R.id.name);
        nameView.setText(restaurant.toString());

        final TextView addressView = (TextView) dialog.findViewById(R.id.address);
        addressView.setText(StringUtil.parseAddressLine(restaurant));

        final TextView businessView = (TextView) dialog.findViewById(R.id.business);
        businessView.setText(restaurant.getBusinessRegular());

        if (restaurant.getBusinessException().trim().isEmpty()) {
            dialog.findViewById(R.id.business_exceptions_header).setVisibility(View.GONE);
            dialog.findViewById(R.id.business_exceptions).setVisibility(View.GONE);
        } else {
            dialog.findViewById(R.id.business_exceptions_header).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.business_exceptions).setVisibility(View.VISIBLE);

            final TextView businessExceptionsView = (TextView) dialog.findViewById(R.id.business_exceptions);
            businessExceptionsView.setText(restaurant.getBusinessException());
        }

        final TextView lunchView = (TextView) dialog.findViewById(R.id.lunch);
        lunchView.setText(restaurant.getLunchRegular());

        if (restaurant.getLunchException().trim().isEmpty()) {
            dialog.findViewById(R.id.lunch_exceptions_header).setVisibility(View.GONE);
            dialog.findViewById(R.id.lunch_exceptions).setVisibility(View.GONE);
        } else {
            dialog.findViewById(R.id.lunch_exceptions_header).setVisibility(View.VISIBLE);
            dialog.findViewById(R.id.lunch_exceptions).setVisibility(View.VISIBLE);

            final TextView lunchExceptionsView = (TextView) dialog.findViewById(R.id.lunch_exceptions);
            lunchExceptionsView.setText(restaurant.getLunchException());
        }


        final Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}

