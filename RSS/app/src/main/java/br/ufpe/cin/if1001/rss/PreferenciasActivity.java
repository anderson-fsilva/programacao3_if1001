package br.ufpe.cin.if1001.rss;

import android.app.Activity;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class PreferenciasActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferencias);
    }

    public static class RssPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }
    }
}