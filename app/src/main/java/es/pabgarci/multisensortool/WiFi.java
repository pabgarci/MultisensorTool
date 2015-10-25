package es.pabgarci.multisensortool;

import android.os.Bundle;


public class WiFi extends Common {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi);
        loadToolbar();

    }

}
