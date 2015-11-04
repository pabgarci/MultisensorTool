package es.pabgarci.multisensortool;

import android.content.Context;
import android.os.Bundle;
import android.telephony.*;
import android.widget.ListView;

public class Network extends Common {

    TelephonyManager tm;
    String[] auxArray;

    public String getPhoneType(){
        String aux="";
        switch (tm.getPhoneType()){
            case (TelephonyManager.PHONE_TYPE_CDMA):
                aux="CDMA";
            break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                aux="GSM";
            break;
            case (TelephonyManager.PHONE_TYPE_SIP):
                aux="SIP";
            break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                aux="NONE";
            break;

        }
        return aux;
    }

    public String getNetworkType(){
        String aux="";
        switch (tm.getNetworkType()){
            case (TelephonyManager.NETWORK_TYPE_1xRTT):
                aux="1xRTT";
                break;
            case (TelephonyManager.NETWORK_TYPE_CDMA):
                aux="CDMA";
                break;
            case (TelephonyManager.NETWORK_TYPE_EDGE):
                aux="EDGE";
                break;
            case (TelephonyManager.NETWORK_TYPE_EHRPD):
                aux="EHRPD";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_0):
                aux="EVDO 0";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_A):
                aux="EVDO A";
                break;
            case (TelephonyManager.NETWORK_TYPE_EVDO_B):
                aux="EVDO B";
                break;
            case (TelephonyManager.NETWORK_TYPE_GPRS):
                aux="GPRS";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSDPA):
                aux="HSDPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSPA):
                aux="HSPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_HSUPA):
                aux="HSUPA";
                break;
            case (TelephonyManager.NETWORK_TYPE_IDEN):
                aux="IDEN";
                break;
            case (TelephonyManager.NETWORK_TYPE_LTE):
                aux="LTE";
                break;
            case (TelephonyManager.NETWORK_TYPE_UMTS):
                aux="UMTS";
                break;
            case (TelephonyManager.NETWORK_TYPE_UNKNOWN):
                aux="UNKNOWN";
                break;
        }
        return aux;
    }

    public String getSIMState(){
        String aux="";
        switch (tm.getSimState()){
            case (TelephonyManager.SIM_STATE_ABSENT):
                aux=getResources().getString(R.string.network_absent);
                break;
            case (TelephonyManager.SIM_STATE_NETWORK_LOCKED):
                aux=getResources().getString(R.string.network_locked);
                break;
            case (TelephonyManager.SIM_STATE_PIN_REQUIRED):
                aux="PIN"+getResources().getString(R.string.network_required);
                break;
            case (TelephonyManager.SIM_STATE_PUK_REQUIRED):
                aux="PUK"+getResources().getString(R.string.network_required);
                break;
            case (TelephonyManager.SIM_STATE_READY):
                aux=getResources().getString(R.string.network_ready);
                break;
            case (TelephonyManager.SIM_STATE_UNKNOWN):
                aux=getResources().getString(R.string.network_unknown);
                break;
        }
        return aux;
    }

    public String isRoaming(){

        String isRoaming;
        boolean isRoamingAux=tm.isNetworkRoaming();
        if(isRoamingAux){
            isRoaming=getResources().getString(R.string.yes);
        }else{
            isRoaming=getResources().getString(R.string.no);
        }
        return isRoaming;
    }


    public void setList(){
        ListAdapterSimple adapter = new ListAdapterSimple(Network.this,  auxArray = new String[]{
                getResources().getString(R.string.network_device_id)+": " + tm.getDeviceId(),
                getResources().getString(R.string.network_subscriber_id)+": " + tm.getSubscriberId(),
                getResources().getString(R.string.network_sim_state)+": " + getSIMState(),
                getResources().getString(R.string.network_sim_serial)+": " + tm.getSimSerialNumber(),
                getResources().getString(R.string.network_sim_operator_id)+ ": " + tm.getSimOperator(),
                getResources().getString( R.string.network_sim_operator)+": " + tm.getSimOperatorName(),
                getResources().getString(R.string.network_sim_country)+" (ISO): " + tm.getSimCountryIso(),
                "Roaming: " + isRoaming(),
                getResources().getString(R.string.network_phone_type)+": " + getPhoneType(),
                getResources().getString(R.string.network_type)+": " + getNetworkType(),
                getResources().getString(R.string.network_operator_id)+": " + tm.getNetworkOperator(),
                getResources().getString(R.string.network_operator)+": " + tm.getNetworkOperatorName(),
                getResources().getString(R.string.network_country)+" (ISO): "+ tm.getNetworkCountryIso(),
                getResources().getString(R.string.network_software)+": " + tm.getDeviceSoftwareVersion()
        });
        ListView list=(ListView)findViewById(R.id.listViewNetwork);
        list.setAdapter(adapter);
    }

    public void noPhone(){
        ListAdapterSimple adapter = new ListAdapterSimple(Network.this,  auxArray = new String[]{
                getResources().getString(R.string.text_no_sim)
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
