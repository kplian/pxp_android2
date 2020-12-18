package com.vouz.mobileV2.retrofit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PXPServices {
    @POST("lib/rest/seguridad/App/AndroidVersion")
    Call<ResponseBody> getAppVersion();

    @GET("health.php")
    Call<ResponseBody> getWebViewHealth();
}
