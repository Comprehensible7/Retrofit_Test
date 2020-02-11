package service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                            .baseUrl("Your Ip Addr")
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
    }
}
