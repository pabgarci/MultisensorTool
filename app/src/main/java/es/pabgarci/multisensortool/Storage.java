package es.pabgarci.multisensortool;

import android.os.Bundle;

public class Storage extends Common {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        loadToolbar();

    }

}
