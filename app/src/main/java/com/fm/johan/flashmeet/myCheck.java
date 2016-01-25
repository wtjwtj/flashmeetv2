package com.fm.johan.flashmeet;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;


public class myCheck
{

    public static NetworkInfo getNetworkInfo(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    public static int state (Context context)
    {

        NetworkInfo info = myCheck.getNetworkInfo(context);
        if(info != null && info.isConnected())
        {
            if(info.getType() == ConnectivityManager.TYPE_WIFI) return 1;
            if(info.getType() == ConnectivityManager.TYPE_MOBILE) return 2;
        }
        else
            return -1;
        return 3;
    }

    public static int stateDetail (Context context)
    {
        NetworkInfo info = myCheck.getNetworkInfo(context);
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                return 0;
            } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                switch (info.getSubtype()) {
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                        return 1; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                        return 2; // ~ 14-64 kbps
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                        return 3; // ~ 50-100 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                        return 4; // ~ 400-1000 kbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                        return 5; // ~ 600-1400 kbps
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                        return 6; // ~ 100 kbps
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                        return 7; // ~ 2-14 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                        return 8; // ~ 700-1700 kbps
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                        return 9; // ~ 1-23 Mbps
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                        return 10; // ~ 400-7000 kbps
                    case TelephonyManager.NETWORK_TYPE_EHRPD: // API level 11
                        return 11; // ~ 1-2 Mbps
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: // API level 9
                        return 12; // ~ 5 Mbps
                    case TelephonyManager.NETWORK_TYPE_HSPAP: // API level 13
                        return 13; // ~ 10-20 Mbps
                    case TelephonyManager.NETWORK_TYPE_IDEN: // API level 8
                        return 14; // ~25 kbps
                    case TelephonyManager.NETWORK_TYPE_LTE: // API level 11
                        return 15; // ~ 10+ Mbps
                    // Unknown
                    case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                    default:
                        return 16;
                }
            } else return 17;
        }
        else return -1;
    }

    public static int getBatteryLevel(Context context)
    {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if(level == -1 || scale == -1) {
            return (int)(50.0f);
        }
        return (int)(((float)level / (float)scale) * 100.0f);
    }

    public static String getDetailString(Context context)
    {
        int var = stateDetail(context);
        switch (var)
        {
            case(0):return "Wifi";
            case(1):return "1xRTT";
            case(2):return "CDMA";
            case(3):return "EDGE";
            case(4):return "EVDO 0";
            case(5):return "EVDO A";
            case(6):return "GPRS";
            case(7):return "HSDPA";
            case(8):return "HSPA";
            case(9):return "HSUPA";
            case(10):return "UMTS";
            case(11):return "EHRPD";
            case(12):return "EVDO B";
            case(13):return "HSPAP";
            case(14):return "IDEN";
            case(15):return "LTE";
            case(16):return "TYPE_UNKNOWN";
            case(-1):return "No Connection";
        }
        return "ERROR 2";
    }

}
