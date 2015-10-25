package es.pabgarci.multisensortool.wifi;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import es.pabgarci.multisensortool.R;

public class WiFiListerAdapter extends BaseAdapter {

        private final Context context;
        private final List<ScanResult> results;


        public WiFiListerAdapter(Context context, List<ScanResult> results) {
            this.context = context;
            this.results = results;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ScanResult result = results.get(position);

            if(convertView==null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.list_wifi_g, null);
            }

            String stringCapabilities;

            TextView txtSSID = (TextView)convertView.findViewById(R.id.txtSSID);
            TextView txtBSSID = (TextView)convertView.findViewById(R.id.txtBSSID);
            TextView txtCapabilities = (TextView)convertView.findViewById(R.id.txtCapabilities);
            TextView txtFrecuency = (TextView)convertView.findViewById(R.id.txtFrecuency);
            TextView txtLevel = (TextView)convertView.findViewById(R.id.txtLevel);

            txtSSID.setText(result.SSID+"\n("+result.BSSID+")");
            //txtBSSID.setText(result.BSSID);

            stringCapabilities = result.capabilities.replace("]","]\n");
            stringCapabilities = stringCapabilities.replace("ESS","Open");

            txtCapabilities.setText(stringCapabilities+" ");
            txtFrecuency.setText(Integer.toString(result.frequency)+" ");
            txtLevel.setText(Integer.toString(result.level)+" ");

            return convertView;
        }


}
