package com.hellokiki.rrorequest;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 黄麒羽 on 2017/12/14.
 */

public class HttpManager {

    public static String BaseUrl;

    private int CONNENTTIMEOUT = 6;
    private int READTIMEOUT = 20;
    private int WRITETIMEOUT = 20;

    private static HttpManager mHttpManager;
    private Retrofit mRetrofit;

    private HttpManager() {
        initRetrofit();
    }

    public static HttpManager getInstance() {
        if (mHttpManager == null) {
            synchronized (HttpManager.class){
                mHttpManager = new HttpManager();
            }
        }
        return mHttpManager;
    }

    public static void baseUrl(String url) {
        BaseUrl = url;
    }

    /**
     * 统一的调度转换器
     * @param <T>
     * @return
     */
    public static  <T> ObservableTransformer<T, T> applySchedulers() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .unsubscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }


    public <T> T  create(Class<T> service){
       return mRetrofit.create(service);
    }


    private void initRetrofit() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNENTTIMEOUT, TimeUnit.SECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITETIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        return builder.build();
    }


}