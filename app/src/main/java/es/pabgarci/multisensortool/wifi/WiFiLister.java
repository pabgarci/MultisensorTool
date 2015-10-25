package es.pabgarci.multisensortool.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.net.wifi.WifiManager;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import es.pabgarci.multisensortool.Common;
import es.pabgarci.multisensortool.R;


public class WiFiLister extends Common {

        private WifiManager manager;
        private WifiReceiver receiver;

        private ListView networksList;
        private WiFiListerAdapter adapter;
        private List<ScanResult> results;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_wi_fi_lister);
            loadToolbar();
            manager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
            receiver = new WifiReceiver();

            if(manager.isWifiEnabled()) {
                scanNetworks();
                networksList = networksList==null ? (ListView)findViewById(R.id.lstNetworks) : networksList;
                adapter = new WiFiListerAdapter(this, results);
                networksList.setAdapter(adapter);
            } else
                Toast.makeText(this, R.string.wifi_is_not_enabled_msg, Toast.LENGTH_LONG).show();

        }




        @Override
        public void onResume() {
            registerReceiver(receiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            super.onResume();
        }

        @Override
        public void onPause() {
            unregisterReceiver(receiver);
            super.onPause();
        }


        public void scanNetworks() {
            boolean scan = manager.startScan();

            if(scan) {
                results = manager.getScanResults();
                Toast.makeText(this, getString(R.string.networks_found_msg, results.size()), Toast.LENGTH_LONG).show();
            } else
                switch(manager.getWifiState()) {
                    case WifiManager.WIFI_STATE_DISABLING:
                        Toast.makeText(this, R.string.wifi_disabling_msg, Toast.LENGTH_LONG).show();
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:
                        Toast.makeText(this, R.string.wifi_disabled_msg, Toast.LENGTH_LONG).show();
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        Toast.makeText(this, R.string.wifi_enabling_msg, Toast.LENGTH_LONG).show();
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Toast.makeText(this, R.string.wifi_enabled_msg, Toast.LENGTH_LONG).show();
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Toast.makeText(this, R.string.wifi_unknown_state_msg, Toast.LENGTH_LONG).show();
                        break;
                }
        }

        class WifiReceiver extends BroadcastReceiver {

            public List<ScanResult> getResults() {
                return results;
            }

            public WifiManager getManager() {
                return manager;
            }

            @Override
            public void onReceive(Context c, Intent intent) {
                results = manager.getScanResults();
                adapter.notifyDataSetChanged();
            }
        }


}
