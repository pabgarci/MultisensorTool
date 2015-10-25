package es.pabgarci.multisensortool.wifi;

import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import es.pabgarci.multisensortool.R;
import es.pabgarci.multisensortool.WiFi;


public class WiFiInfo extends WiFi {

    final Handler handler = new Handler();

    final long MS=500;

    TextView textView5GHZ;
    TextView textViewFrequency;
    TextView textViewSpeed;
    TextView textViewIP;
    TextView textViewSSID;
    TextView textViewBSSID;
    TextView textViewRSSI;
    TextView textViewMAC;

    boolean boolean5GHZ;


    Runnable runnable = new Runnable() {
        public void run() {
            setInfo();
            handler.postDelayed(runnable, MS);
        }
    };


        public void set5GHZ(){
        if(android.os.Build.VERSION.SDK_INT >= 21) {
            boolean5GHZ = wifiManager.is5GHzBandSupported();

            textViewFrequency.setText(String.format("%s: %s", getResources().getString(R.string.link_frequency), wifiInfo.getFrequency() + WifiInfo.FREQUENCY_UNITS));

            if(boolean5GHZ) {
                textView5GHZ.setText(R.string.yes);
            }else{
                textView5GHZ.setText(R.string.no);
            }

        }else {
            textView5GHZ.setText(String.format("%s: %s", getResources().getString(R.string.ghz_support), getResources().getString(R.string.not_available)));
            textViewFrequency.setText(String.format("%s: %s", getResources().getString(R.string.current_frequency), getResources().getString(R.string.not_available)));
        }


    }

    public void setIP(){
        int myIp = wifiInfo.getIpAddress();

        int intMyIp3 = myIp/0x1000000;
        int intMyIp3mod = myIp%0x1000000;

        int intMyIp2 = intMyIp3mod/0x10000;
        int intMyIp2mod = intMyIp3mod%0x10000;

        int intMyIp1 = intMyIp2mod/0x100;
        int intMyIp0 = intMyIp2mod%0x100;

        textViewIP.setText(String.format("%s: %s.%s.%s.%s", "IP",String.valueOf(intMyIp0), String.valueOf(intMyIp1), String.valueOf(intMyIp2), String.valueOf(intMyIp3)));

    }

    public void setInfo(){
        wifiInfo = wifiManager.getConnectionInfo();
        set5GHZ();
        textViewSpeed.setText(String.format("%s: %s", getResources().getString(R.string.link_speed), wifiInfo.getLinkSpeed() + WifiInfo.LINK_SPEED_UNITS));
        textViewMAC.setText(String.format("MAC: %s", wifiInfo.getMacAddress()));
        setIP();
        textViewSSID.setText(String.format("SSID: %s", wifiInfo.getSSID()));
        textViewBSSID.setText(String.format("BSSID: %s", wifiInfo.getBSSID()));
        textViewRSSI.setText(String.format("Power: %s dbm", String.valueOf(wifiInfo.getRssi())));


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_info);
        loadToolbar();

        textView5GHZ = (TextView) findViewById(R.id.textView_5ghz);
        textViewFrequency = (TextView) findViewById(R.id.textView_frequency);
        textViewSpeed = (TextView) findViewById(R.id.textView_speed);
        textViewIP = (TextView) findViewById(R.id.textView_ip);
        textViewSSID = (TextView) findViewById(R.id.textView_ssid);
        textViewBSSID = (TextView) findViewById(R.id.textView_bssid);
        textViewRSSI = (TextView) findViewById(R.id.textView_rssi);
        textViewMAC = (TextView) findViewById(R.id.textView_mac);


        if(wifiManager.isWifiEnabled()){
            runnable.run();
        }else{

        }



    }

}
