package fi.loezi.unifud;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {



    @Override
    protected void onCreate(final Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }
}
