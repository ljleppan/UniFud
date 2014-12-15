package fi.loezi.unifud;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

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

        final TextView dietsView = (TextView) dialog.findViewById(R.id.diets);
        dietsView.setText(StringUtil.toCommaSeparatedValues(meal.getDiets()));

        if (meal.getSpecialContents().isEmpty()) {
            dialog.findViewById(R.id.specialContents).setVisibility(View.GONE);
        } else {
            final TextView specialContentsView = (TextView) dialog.findViewById(R.id.specialContents);
            specialContentsView.setVisibility(View.VISIBLE);
            specialContentsView.setText(StringUtil.toCommaSeparatedValues(meal.getSpecialContents()));
        }

        if (meal.getNotes().isEmpty()) {
            dialog.findViewById(R.id.notes).setVisibility(View.GONE);
        } else {
            final TextView notesView = (TextView) dialog.findViewById(R.id.notes);
            notesView.setVisibility(View.VISIBLE);
            notesView.setText(StringUtil.toCommaSeparatedValues(meal.getNotes()));
        }

        if (meal.getIngredients().equals("null")) {
            dialog.findViewById(R.id.ingredients).setVisibility(View.GONE);
        } else {
            final TextView ingredientsView = (TextView) dialog.findViewById(R.id.ingredients);
            ingredientsView.setVisibility(View.VISIBLE);
            ingredientsView.setText(meal.getIngredients());
        }

        if (meal.getNutrition().equals("null")) {
            dialog.findViewById(R.id.nutritions).setVisibility(View.GONE);
        } else {
            final TextView nutritionsView = (TextView) dialog.findViewById(R.id.nutritions);
            nutritionsView.setVisibility(View.VISIBLE);
            nutritionsView.setText(meal.getNutrition());
        }


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
}
