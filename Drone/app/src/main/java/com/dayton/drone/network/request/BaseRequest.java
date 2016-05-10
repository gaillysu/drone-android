package com.dayton.drone.network.request;



import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import org.apache.commons.codec.binary.Base64;

/**
 * Created by med on 16/4/29.
 */
public abstract class BaseRequest<T,R> extends RetrofitSpiceRequest<T,R> {
    public final String CONTENT_TYPE = "application/json";
    public BaseRequest(Class<T> clazz, Class<R> retrofitedInterfaceClass) {
        super(clazz, retrofitedInterfaceClass);
    }
    //for HTTP "get"/"header", hasn't body, so needn't implement this function, others need do it.
    public interface BaseRetroRequestBody<B> {
        public B  buildRequestBody();
    }
    public String buildAuthorization(){
        String authorization = "Basic ";
        String username = "apps";
        String password = "med_app_development";
        return authorization + new String(new Base64().encode(new String(username+":"+password).getBytes()));
    }
}

