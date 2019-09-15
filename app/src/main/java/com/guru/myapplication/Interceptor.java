package com.guru.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * created by gurukathir.j on 14-09-2019
 */
public class Interceptor extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String phoneNumber = getResultData();
            // No reformatted number, use the original
            phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            if(phoneNumber.equals("9500181587")){
                Toast.makeText(context,"use chatbot",Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(context,ChatBotActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                setResultData(null);
            }else
            {
                Toast.makeText(context,"use Calling",Toast.LENGTH_LONG).show();
                //setResultData(null);
            }

        // My app will bring up the call, so cancel the broadcast
       // setResultData(null);
    }
}
