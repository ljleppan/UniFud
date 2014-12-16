package fi.loezi.unifud;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.List;

import fi.loezi.unifud.model.Meal;
import fi.loezi.unifud.util.StringUtil;

public class MealClickListener implements ExpandableListView.OnChildClickListener {

    private final RestaurantListAdapter adapter;
    private final Context context;

    public MealClickListener(Context context, RestaurantListAdapter adapter) {

        this.context = context;
        this.adapter = adapter;
    }

    @Override
    public boolean onChildClick(final ExpandableListView parent,
                                final View v,
                                final int groupPosition,
                                final int childPosition,
                                final long id) {

        final Meal meal = (Meal) adapter.getChild(groupPosition, childPosition);

        if (meal == null) {
            return true;
        }

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.meal_info_fragment);

        final TextView nameView = (TextView) dialog.findViewById(R.id.name);
        nameView.setText(meal.toString());

        setTextOrHide(meal.getDiets(), dialog, R.id.diets);
        setTextOrHide(meal.getSpecialContents(), dialog, R.id.specialContents);
        setTextOrHide(meal.getNotes(), dialog, R.id.notes);
        setTextOrHide(meal.getIngredients(), dialog, R.id.ingredients);
        setTextOrHide(meal.getNutrition(), dialog, R.id.nutritions);

        final Button dialogButton = (Button) dialog.findViewById(R.id.dialogButtonOK);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

        return true;
    }

    private void setTextOrHide(final List<String> listOfStrings, final Dialog parent, final int elementId) throws IllegalArgumentException {

        final String text = StringUtil.toCommaSeparatedValues(listOfStrings);
        setTextOrHide(text, parent, elementId);
    }

    private void setTextOrHide(final String text, final Dialog parent, final int elementId) throws IllegalArgumentException{

        final View view = parent.findViewById(elementId);
        if (view == null) {
            throw new IllegalArgumentException("No element with id " + elementId);
        } else if (!(view instanceof TextView)) {
            throw new IllegalArgumentException("Element with id " + elementId + " is not a TextView");
        }

        final TextView textView = (TextView) view;

        if (text == null || text.trim().equals("null") || text.trim().isEmpty()) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(text);
        }
    }
}
