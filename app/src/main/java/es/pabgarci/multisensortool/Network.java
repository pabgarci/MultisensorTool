package es.pabgarci.multisensortool;

import android.content.Context;
import android.os.Bundle;
import android.telephony.*;
import android.widget.ListView;

public class Network extends Common {

    TelephonyManager tm;
    String[] auxArray;

    public String getPhoneType(){
        String aux="Phone type: ";
        switch (tm.getPhoneType()){
            case (TelephonyManager.PHONE_TYPE_CDMA):
                aux.concat("CDMA");
            break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                aux.concat("GSM");
            break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                aux.concat("SIP");
            break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                aux.concat("NONE");
            break;

        }
        return aux;
    }

    public String getNetworkType(){
        String aux="Network type: ";
        switch (tm.getNetworkType()){
            case (TelephonyManager.NETWORK_TYPE_1xRTT):
                aux.concat("1xRTT");
                break;
            case (TelephonyManager.NETWORK_TYPE_CDMA):
                aux.concat("CDMA");
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                aux.concat("EDGE");
                break;
            case (TelephonyManager.NETWORK_TYPE_EHRPD):
                aux.concat("EHRPD");
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                aux.concat("EVDO 0");
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_A):
                aux.concat("EVDO A");
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_B):
                aux.concat("EVDO B");
                break;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                aux.concat("GPRS");
                break;
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                aux.concat("HSDPA");
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPA):
                aux.concat("HSPA");
                break;
            case (TelephonyManager.NETWORK_TYPE_HSUPA):
                aux.concat("HSUPA");
                break;
            case (TelephonyManager.NETWORK_TYPE_IDEN):
                aux.concat("IDEN");
                break;
            case (TelephonyManager.NETWORK_TYPE_LTE):
                aux.concat("LTE");
                break;
            case (TelephonyManager.NETWORK_TYPE_UMTS):
                aux.concat("UMTS");
                break;
            case (TelephonyManager.NETWORK_TYPE_UNKNOWN):
                aux.concat("UNKNOWN");
                break;
        }
        return aux;
    }

    public String getSIMState(){
        String aux="SIM state: ";
        switch (tm.getSimState()){
            case (TelephonyManager.SIM_STATE_ABSENT):
                aux.concat("Absent");
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                aux.concat("Locked");
                break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
                aux.concat("PIN Required");
                break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
                aux.concat("PUK Required");
                break;
            case (TelephonyManager.SIM_STATE_READY):
                aux.concat("Ready");
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                aux.concat("UNKNOWN");
                break;
        }
        return aux;
    }

    public String isRoaming(){

        String isRoaming;
        boolean isRoamingAux=tm.isNetworkRoaming();
        if(isRoamingAux){
            isRoaming="YES";
        }else{
            isRoaming="NO";
        }
        return "Roaming: "+isRoaming;
    }


    public void setList(){
        ListAdapterSimple adapter = new ListAdapterSimple(Network.this,  auxArray = new String[]{
                "Device ID: " + tm.getDeviceId(),
                "Subscriber ID: " + tm.getSubscriberId(),
                "SIM State: " + getSIMState(),
                "SIM Serial number: " + tm.getSimSerialNumber(),
                "SIM Operator ID: " + tm.getSimOperator(),
                "SIM Operator: " + tm.getSimOperatorName(),
                "SIM Country (ISO): " + tm.getSimCountryIso(),
                "Roaming: " + isRoaming(),
                "Phone type: " + getPhoneType(),
                "Network type: " + getNetworkType(),
                "Network Operator ID: " + tm.getNetworkOperator(),
                "Network Operator: " + tm.getNetworkOperatorName(),
                "Network Country (ISO): " + tm.getNetworkCountryIso(),
                "Device software version: " + tm.getDeviceSoftwareVersion()
        });
        ListView list=(ListView)findViewById(R.id.listViewNetwork);
        list.setAdapter(adapter);
    }

    public void noPhone(){
        ListAdapterSimple adapter = new ListAdapterSimple(Network.this,  auxArray = new String[]{
                "Device without modem (SIM)."
        });
        ListView list=(ListView)findViewById(R.id.listViewNetwork);
        list.setAdapter(adapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        loadToolbar();
        tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (tm.getDeviceId() == null) {
            noPhone();
        } else {
            setList();
        }
    }

}
