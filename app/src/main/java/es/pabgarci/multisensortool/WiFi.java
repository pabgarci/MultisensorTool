package es.pabgarci.multisensortool;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import es.pabgarci.multisensortool.wifi.WiFiInfo;
import es.pabgarci.multisensortool.wifi.WiFiLister;


public class WiFi extends Common {

    public WifiManager wifiManager;
    public WifiInfo wifiInfo;

    ListView list;

    String[] web;

    Integer[] imageId = {
            R.drawable.ic_list_white_48dp, //Accelerometer
            R.drawable.ic_network_wifi_white_48dp //Accelerometer Logger

    };

    public void setList(){
        ListAdapter adapter = new
                ListAdapter(WiFi.this, web, imageId);
        list=(ListView)findViewById(R.id.listViewWiFi);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        Intent intent = new Intent(getApplicationContext(), WiFiLister.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(getApplicationContext(), WiFiInfo.class);
                        startActivity(intent2);
                        break;
                }
            }
        });
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi);
        loadToolbar();
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        web = getResources().getStringArray(R.array.menu_items_wifi);
        setList();

    }

}
