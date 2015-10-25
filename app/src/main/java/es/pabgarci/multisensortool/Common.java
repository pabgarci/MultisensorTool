package es.pabgarci.multisensortool;

import android.app.Dialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

public class Common extends FilterActivity{

    public void loadToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
    }

    public void showAboutDialog(String clase) {

        View view;

        Dialog helpDialog = new Dialog(this);

        helpDialog.setCancelable(true);
        helpDialog.setCanceledOnTouchOutside(true);
        helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch (clase){
            case "main":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "accelerometer":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "logger":
                view = getLayoutInflater().inflate(R.layout.about,null);
                break;
            case "vector":
                view = getLayoutInflater().inflate(R.layout.about, null);
                 break;
            case "compass":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "gps":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "gravity":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "gyroscope":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "humidity":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "light":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "linearacceleration":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "magneticfield":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "network":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "orientation":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "pressure":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "proximity":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "storage":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "temperature":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            case "wifi":
                view = getLayoutInflater().inflate(R.layout.about, null);
                break;
            default:
                view = getLayoutInflater().inflate(R.layout.about, null);

        }


        helpDialog.setContentView(view);

        helpDialog.show();
    }

    public void showHelpDialog(String clase){
        Dialog helpDialog = new Dialog(this);
        View view;

        helpDialog.setCancelable(true);
        helpDialog.setCanceledOnTouchOutside(true);
        helpDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        switch (clase){
            case "main":
                view = getLayoutInflater().inflate(R.layout.help_home, null);
                break;
            case "accelerometer":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "logger":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "vector":
                view = getLayoutInflater().inflate(R.layout.help_vector,null);
                break;
            case "compass":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "gps":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "gravity":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "gyroscope":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "humidity":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "light":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "linearacceleration":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "magneticfield":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "network":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "orientation":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "pressure":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "proximity":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "storage":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "temperature":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            case "wifi":
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);
                break;
            default:
                view = getLayoutInflater().inflate(R.layout.help_gyroscope,null);

        }


        helpDialog.setContentView(view);

        helpDialog.show();
    }
}
