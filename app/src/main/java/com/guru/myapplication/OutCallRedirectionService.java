package com.guru.myapplication;

import android.app.Service;
import android.app.role.RoleManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telecom.CallRedirectionService;
import android.telecom.PhoneAccountHandle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

/**
 * created by gurukathir.j on 14-09-2019
 */
@RequiresApi(api = Build.VERSION_CODES.Q)
public class OutCallRedirectionService extends CallRedirectionService {



RoleManager roleManager;
    @Override
    public void onPlaceCall(@NonNull Uri uri, @NonNull PhoneAccountHandle phoneAccountHandle, boolean b) {
        Log.e("Your",uri.toString());
       if(uri.toString().contains("9500181587"))
       {
           this.startActivity(new Intent(this,ChatBotActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
           cancelCall();
       }else
       {
           //placeCallUnmodified();
           redirectCall(uri,phoneAccountHandle,true);
          // roleManager = getSystemService(RoleManager.class);
          // roleManager.getRoleHolders();

       }
    }

    public OutCallRedirectionService() {
        super();
    }
}
