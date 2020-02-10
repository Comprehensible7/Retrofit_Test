package service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                            .baseUrl("http://192.168.0.63:9090/")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
    }
}
