package es.pabgarci.multisensortool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import es.pabgarci.multisensortool.config.FilterConfigActivity;
import es.pabgarci.multisensortool.config.MainConfigActivity;

public class MainActivity extends Common {
        ListView list;

        String[] web;

        Integer[] imageId = {
                R.drawable.ic_car_white_48dp, //Accelerometer
                R.drawable.ic_chart_areaspline_white_48dp, //Accelerometer Logger
                R.drawable.vector, //Accelerometer Vector
                R.drawable.compass_invert, //Compass
                R.drawable.ic_weight_white_48dp, //Gravity
                R.drawable.ic_gps_fixed_white_48dp, //GPS
                R.drawable.gyroscope, //Gyroscope
                R.drawable.ic_water_white_48dp, //Humidity
                R.drawable.ic_flashlight_white_48dp, //Lantern
                R.drawable.ic_wb_incandescent_white_48dp, //Light
                R.drawable.ic_vector_curve_white_48dp, //Linear Acceleration
                R.drawable.ic_magnet_white_48dp, //Magnetic Field
                R.drawable.ic_network_cell_white_48dp, //Network
                R.drawable.pressure, //Pressure
                R.drawable.ic_ruler_white_48dp, //Proximity
                R.drawable.ic_screen_rotation_white_48dp, //Screen
                R.drawable.ic_sd_storage_white_48dp, //Storage
                R.drawable.ic_temperature_celsius_white_48dp, //Temperature
                R.drawable.ic_network_wifi_white_48dp //WiFi

        };


    public void loadToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setSubtitle(R.string.app_surname);
    }

    public void notAvailableYet(){
        Toast.makeText(getApplicationContext(), R.string.text_not_available_yet, Toast.LENGTH_SHORT).show();
    }


    public void setList(){
        ListAdapter adapter = new
                ListAdapter(MainActivity.this, web, imageId);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), Accelerometer.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent16 = new Intent(getApplicationContext(), AccelerometerLogger.class);
                        startActivity(intent16);
                        break;
                    case 2:
                        Intent intent17 = new Intent(getApplicationContext(), AccelerometerVector.class);
                        startActivity(intent17);
                        break;
                    case 3:
                        Intent intent15 = new Intent(getApplicationContext(), Compass.class);
                        startActivity(intent15);
                        break;
                    case 4:
                        Intent intent1 = new Intent(getApplicationContext(), Gravity.class);
                        startActivity(intent1);
                        break;
                    case 5:
                        Intent intent2 = new Intent(getApplicationContext(), GPS.class);
                        startActivity(intent2);
                        break;
                    case 6:
                        Intent intent3 = new Intent(getApplicationContext(), Gyroscope.class);
                        startActivity(intent3);
                        break;
                    case 7:
                        Intent intent4 = new Intent(getApplicationContext(), Humidity.class);
                        startActivity(intent4);
                        break;
                    case 8:
                        Intent intent18 = new Intent(getApplicationContext(), Lantern.class);
                        startActivity(intent18);
                        break;
                    case 9:
                        Intent intent5 = new Intent(getApplicationContext(), Light.class);
                        startActivity(intent5);
                        break;
                    case 10:
                        notAvailableYet();
                        //Intent intent6 = new Intent(getApplicationContext(), LinearAcceleration.class);
                        //startActivity(intent6);
                        break;
                    case 11:
                        Intent intent7 = new Intent(getApplicationContext(), MagneticField.class);
                        startActivity(intent7);
                        break;
                    case 12:
                        Intent intent8 = new Intent(getApplicationContext(), Network.class);
                        startActivity(intent8);
                        break;
                    case 13:
                        Intent intent10 = new Intent(getApplicationContext(), Pressure.class);
                        startActivity(intent10);
                        break;
                    case 14:
                        Intent intent11 = new Intent(getApplicationContext(), Proximity.class);
                        startActivity(intent11);
                        break;
                    case 15:
                        Intent intent9 = new Intent(getApplicationContext(), Screen.class);
                        startActivity(intent9);
                        break;
                    case 16:
                        notAvailableYet();
                        //Intent intent12 = new Intent(getApplicationContext(), Storage.class);
                        //startActivity(intent12);
                        break;
                    case 17:
                        Intent intent13 = new Intent(getApplicationContext(), Temperature.class);
                        startActivity(intent13);
                        break;
                    case 18:
                        Intent intent14 = new Intent(getApplicationContext(), WiFi.class);
                        startActivity(intent14);
                        break;
                }

            }
        });

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        web = getResources().getStringArray(R.array.menu_items);
        loadToolbar();
        setList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
                showAboutDialog("main");
                return true;
        }else if(id == R.id.action_settings){
            Intent intent = new Intent(getApplicationContext(), MainConfigActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
