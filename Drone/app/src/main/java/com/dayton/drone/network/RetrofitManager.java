package com.dayton.drone.network;

import android.content.Context;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.request.SpiceRequest;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by med on 16/4/29.
 */
public class RetrofitManager {
    private Context context;
    private SpiceManager spiceManager;

    public  RetrofitManager(Context context){
        this.context = context;
        spiceManager = new SpiceManager(RetrofitService.class);
        
        startSpiceManager();
    }

    public void startSpiceManager()
    {
        if(!spiceManager.isStarted()) {
            spiceManager.start(context);
        }
    }
    public void stopSpiceManager()
    {
        if(spiceManager!=null) {
            spiceManager.shouldStop();
        }
    }

    public void execute(SpiceRequest request, RequestListener listener){
        spiceManager.execute(request, listener);
    }
    public String getAccessToken(){
        return "ZQpFYPBMqFbUQq8E99FztS2x6yQ2v1Ei";
    }
}
