package fi.loezi.unifud;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import fi.loezi.unifud.model.Restaurant;
import fi.loezi.unifud.util.hoursFormatter.ExceptionalHoursFormatter;
import fi.loezi.unifud.util.hoursFormatter.RegularHoursFormatter;

public class RestaurantClickListener implements View.OnClickListener {

    private final Context context;
    private final Restaurant restaurant;

    public RestaurantClickListener(final Context context, final Restaurant restaurant) {

        this.context = context;
        this.restaurant = restaurant;
    }

    @Override
    public void onClick(final View v) {

        if (restaurant == null) {
            return;
        }

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.restaurant_info_fragment);

        getTextView(dialog, R.id.name).setText(restaurant.toString());
        getTextView(dialog, R.id.address).setText(restaurant.getAddress());

        final String businessRegular = RegularHoursFormatter.toString(restaurant.getBusinessRegular());
        getTextView(dialog, R.id.business).setText(businessRegular);

        final String businessExceptional = ExceptionalHoursFormatter.toString(restaurant.getBusinessException());
        setTextOrHide(businessExceptional, dialog, R.id.business_exceptions, R.id.business_exceptions_header);

        final String lunchRegular = RegularHoursFormatter.toString(restaurant.getLunchRegular());
        getTextView(dialog, R.id.lunch).setText(lunchRegular);

        final String lunchExceptional = ExceptionalHoursFormatter.toString(restaurant.getLunchException());
        setTextOrHide(lunchExceptional, dialog, R.id.lunch_exceptions, R.id.lunch_exceptions_header);

        final String bistroRegular = RegularHoursFormatter.toString(restaurant.getBistroRegular());
        final String bistroExceptional = ExceptionalHoursFormatter.toString(restaurant.getBistroException());
        setTextOrHide(bistroExceptional, dialog, R.id.bistro_exceptions, R.id.bistro_exceptions_header);
        if (bistroRegular.isEmpty() && getTextView(dialog, R.id.bistro_exceptions).getVisibility() == View.GONE) {
            // No bistro hours or exceptions, everything bistro
            dialog.findViewById(R.id.bistro_header).setVisibility(View.GONE);
            dialog.findViewById(R.id.bistro).setVisibility(View.GONE);
        } else {
            final TextView bistroView = (TextView) dialog.findViewById(R.id.bistro);

            dialog.findViewById(R.id.bistro_header).setVisibility(View.VISIBLE);
            bistroView.setVisibility(View.VISIBLE);

            bistroView.setText(bistroRegular);
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

    private void setTextOrHide(final String text, final Dialog parent, final int textViewId, final int headerId) {

        final TextView textView = getTextView(parent, textViewId);
        final TextView headerView = getTextView(parent, headerId);

        if (text == null || text.trim().equals("null") || text.trim().isEmpty()) {
            textView.setVisibility(View.GONE);
            headerView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            headerView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }

    private TextView getTextView(final Dialog parent, final int elementId) {

        final View view = parent.findViewById(elementId);
        if (view == null) {
            throw new IllegalArgumentException("No element with id " + elementId);
        } else if (!(view instanceof TextView)) {
            throw new IllegalArgumentException("Element with id " + elementId + " is not a TextView");
        }

        return (TextView) view;
    }
}
