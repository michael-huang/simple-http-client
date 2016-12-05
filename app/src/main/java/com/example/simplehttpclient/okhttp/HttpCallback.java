package com.example.simplehttpclient.okhttp;

import java.io.IOException;

import okhttp3.Call;

/**
 * Created by huangyanzhen on 2016/12/5.
 */

public interface HttpCallback<T> {
    void onSuccess(T t);
    void onFailure(Call call, IOException e);
    void onError(int statusCode);
}
