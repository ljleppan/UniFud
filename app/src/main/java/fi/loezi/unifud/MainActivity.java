package fi.loezi.unifud;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import fi.loezi.unifud.model.Restaurant;
import fi.loezi.unifud.task.RefreshTask;
import fi.loezi.unifud.util.MessiApiHelper;

public class MainActivity extends FragmentActivity {

    private ViewPager pager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.activity_main);

        pager = (ViewPager) findViewById(R.id.pager);

        final PagerAdapter pagerAdapter = new RestaurantListPagerAdapter(this, getSupportFragmentManager(), new ArrayList<Restaurant>());
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(MessiApiHelper.getDateOffset());
    }

    @Override
    protected void onResume() {

        super.onResume();

        refresh();
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() == MessiApiHelper.getDateOffset()) {
            super.onBackPressed();
        } else {
            pager.setCurrentItem(MessiApiHelper.getDateOffset());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

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

        new RefreshTask(this).execute();
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
