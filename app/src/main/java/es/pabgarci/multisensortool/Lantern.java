package es.pabgarci.multisensortool;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class Lantern extends Common {
    Camera cam;
    Camera.Parameters params;
    ImageView imageViewLantern;

    PackageManager pm;

    private boolean STATE = false;
    private boolean CAMERA;

    public void noCamera() {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_supported), Toast.LENGTH_SHORT).show();
    }

    private boolean isFlashSupported(PackageManager packageManager){
        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lantern);
        loadToolbar();
        setPreferences();
        imageViewLantern=(ImageView)findViewById(R.id.imageViewLantern);
        imageViewLantern.setImageResource(R.drawable.btn_switch_off);
        pm = getPackageManager();
        if(isFlashSupported(pm)){
            CAMERA=true;
            cam = Camera.open();
            params = cam.getParameters();
        }else{
            CAMERA=false;
        }

        imageViewLantern.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (CAMERA) {
                    if (STATE) {
                        params = cam.getParameters();
                        params = cam.getParameters();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                        cam.setParameters(params);
                        cam.startPreview();
                        imageViewLantern.setImageResource(R.drawable.btn_switch_off);
                        STATE = false;
                    }else{
                        params = cam.getParameters();
                        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(params);
                        cam.stopPreview();
                        imageViewLantern.setImageResource(R.drawable.btn_switch_on);
                        STATE = true;
                    }

                }else{
                    noCamera();
                }}});

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(CAMERA) {
            cam.release();
        }
    }

    public void setPreferences() {
        PreferenceManager.setDefaultValues(this, R.xml.preference_wifi, false);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
    }



}
