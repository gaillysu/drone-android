package com.dayton.drone.network;

import android.content.Context;

import com.dayton.drone.R;
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

    private void startSpiceManager()
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
        return context.getString(R.string.token);
    }
}
