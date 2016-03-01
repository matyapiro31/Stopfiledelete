package net.surlinter.akira.stopfiledelete;

import android.preference.PreferenceFragment;

public class AkiraPreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
    }
}
