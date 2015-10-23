package es.pabgarci.multisensortool;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
        ListView list;

        String[] web = {
                "Accelerometer",
                "Compass",
                "Gravity",
                "GPS",
                "Gyroscope",
                "Humidity",
                "Light",
                "Linear Acceleration",
                "Magnetic Field",
                "Network",
                "Orientation",
                "Pressure",
                "Proximity",
                "Storage",
                "Temperature",
                "WiFi"
        } ;
        Integer[] imageId = {
                R.drawable.ic_car_white_48dp, //Accelerometer
                R.drawable.compass_invert, //Compass
                R.drawable.ic_weight_white_48dp, //Gravity
                R.drawable.ic_gps_fixed_white_48dp, //GPS
                R.drawable.gyroscope, //Gyroscope
                R.drawable.ic_water_white_48dp, //Humidity
                R.drawable.ic_wb_incandescent_white_48dp, //Light
                R.drawable.ic_vector_curve_white_48dp, //Linear Acceleration
                R.drawable.ic_magnet_white_48dp, //Magnetic Field
                R.drawable.ic_network_cell_white_48dp, //Network
                R.drawable.ic_screen_rotation_white_48dp, //Orientation
                R.drawable.pressure, //Pressure
                R.drawable.ic_ruler_white_48dp, //Proximity
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


    public void setList(){
        ListAdapter adapter = new
                ListAdapter(MainActivity.this, web, imageId);
        list=(ListView)findViewById(R.id.listView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch(web[position]){
                    case "Accelerometer":
                    Intent intent = new Intent(getApplicationContext(), Accelerometer.class);
                    startActivity(intent);
                        break;
                    case "Compass":
                        Intent intent15 = new Intent(getApplicationContext(), Compass.class);
                        startActivity(intent15);
                        break;
                    case "Gravity":
                        Intent intent1 = new Intent(getApplicationContext(), Gravity.class);
                        startActivity(intent1);
                        break;
                    case "GPS":
                        Intent intent2 = new Intent(getApplicationContext(), GPS.class);
                        startActivity(intent2);
                        break;
                    case "Gyroscope":
                        Intent intent3 = new Intent(getApplicationContext(), Gyroscope.class);
                        startActivity(intent3);
                        break;
                    case "Humidity":
                        Intent intent4 = new Intent(getApplicationContext(), Humidity.class);
                        startActivity(intent4);
                        break;
                    case "Light":
                        Intent intent5 = new Intent(getApplicationContext(), Light.class);
                        startActivity(intent5);
                        break;
                    case "Linear Acceleration":
                        Intent intent6 = new Intent(getApplicationContext(), Accelerometer.class);
                        startActivity(intent6);
                        break;
                    case "Magnetic Field":
                        Intent intent7 = new Intent(getApplicationContext(), MagneticField.class);
                        startActivity(intent7);
                        break;
                    case "Network":
                        Intent intent8 = new Intent(getApplicationContext(), Network.class);
                        startActivity(intent8);
                        break;
                    case "Orientation":
                        Intent intent9 = new Intent(getApplicationContext(), Orientation.class);
                        startActivity(intent9);
                        break;
                    case "Pressure":
                        Intent intent10 = new Intent(getApplicationContext(), Pressure.class);
                        startActivity(intent10);
                        break;
                    case "Proximity":
                        Intent intent11 = new Intent(getApplicationContext(), Proximity.class);
                        startActivity(intent11);
                        break;
                    case "Storage":
                        Intent intent12 = new Intent(getApplicationContext(), Storage.class);
                        startActivity(intent12);
                        break;
                    case "Temperature":
                        Intent intent13 = new Intent(getApplicationContext(), Temperature.class);
                        startActivity(intent13);
                        break;
                    case "WiFi":
                        Intent intent14 = new Intent(getApplicationContext(), WiFi.class);
                        startActivity(intent14);
                        break;
                }

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadToolbar();
        setList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
