package com.example.simplehttpclient.okhttp;

import android.os.Handler;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by huangyanzhen on 2016/12/5.
 */

public class OkHttpManager {

    private static OkHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mHandler;
    private Gson mGson;

    private OkHttpManager() {
        initOkHttp();
        mHandler = new Handler();
        mGson = new Gson();
    }

    public static synchronized OkHttpManager getInstance() {
        if (mInstance == null) {
            mInstance = new OkHttpManager();
        }
        return mInstance;
    }

    private void initOkHttp() {
        mOkHttpClient = new OkHttpClient().newBuilder()
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .connectTimeout(2000, TimeUnit.MILLISECONDS).build();
    }

    public void request(SimpleHttpClient client, final HttpCallback callback) {

        if (callback == null) {
            throw new NullPointerException("callback is null");
        }
        mOkHttpClient.newCall(client.buildRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailureMessage(callback, call, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String result = response.body().string();

                    if (callback.mType == null || callback.mType == String.class) {
                        sendOnSuccessMessage(callback, result);
                    } else {
                        try {
                            sendOnSuccessMessage(callback, mGson.fromJson(result, callback.mType));
                        } catch (Exception e) {
                            sendOnErrorMessage(callback, response.code());
                        }
                    }

                    if (response.body() != null) {
                        response.body().close();
                    }
                } else {
                    sendOnErrorMessage(callback, response.code());
                }
            }
        });
    }

    private void sendFailureMessage(final HttpCallback callback, final Call call, final IOException e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(call, e);
            }
        });
    }

    private void sendOnErrorMessage(final HttpCallback callback, final int code) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(code);
            }
        });
    }

    private void sendOnSuccessMessage(final HttpCallback callback, final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(obj);
            }
        });
    }
}
