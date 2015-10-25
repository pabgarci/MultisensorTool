package es.pabgarci.multisensortool;


import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

public class Screen extends Common{


    private TextView textViewOrientation;
    int orientation;

    public void setOrientation() {
        orientation = getResources().getConfiguration().orientation;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                textViewOrientation.setText("Screen orientation: Landscape");
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                textViewOrientation.setText("Screen orientation: Portrait");
                break;
        }
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen);
        loadToolbar();
        textViewOrientation = (TextView)findViewById(R.id.textViewOrientation);
        setOrientation();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

            setOrientation();



    }

}
