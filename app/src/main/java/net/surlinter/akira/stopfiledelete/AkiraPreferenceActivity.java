package net.surlinter.akira.stopfiledelete;

import android.app.Activity;
import android.preference.PreferenceActivity;

public class AkiraPreferenceActivity extends Activity {
    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new AkiraPreferenceFragment()).commit();


    }
}
