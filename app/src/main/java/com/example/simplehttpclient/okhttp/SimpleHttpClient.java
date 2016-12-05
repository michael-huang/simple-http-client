package com.example.simplehttpclient.okhttp;

import android.net.Uri;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by huangyanzhen on 2016/12/5.
 */

public class SimpleHttpClient {

    private Builder mBuilder;
    private static final String TAG = SimpleHttpClient.class.getSimpleName();

    private SimpleHttpClient(Builder builder) {
        mBuilder = builder;
    }

    public Request buildRequest() {

        Request.Builder builder = new Request.Builder();
        if (mBuilder.method.equals("GET")) {
            builder.get();
            builder.url(buildGetRequestParam());
        } else if (mBuilder.method.equals("POST")) {
            try {
                builder.post(buildRequestBody());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            builder.url(mBuilder.url);
        }

        return builder.build();
    }

    private String buildGetRequestParam() {
        if (mBuilder.mParams.size() <= 0) {
            return mBuilder.url;
        }
        Uri.Builder builder = Uri.parse(mBuilder.url).buildUpon();
        for (RequestParam p : mBuilder.mParams) {
            builder.appendQueryParameter(p.getKey(), p.getObject() == null ? "" : p.getObject().toString());
        }
        String url = builder.build().toString();
        Log.d(TAG, "the GET url = " + url);

        return url;
    }

    private RequestBody buildRequestBody() throws JSONException{
        if (mBuilder.isJsonParam) {
            JSONObject jsonObject = new JSONObject();
            for (RequestParam p : mBuilder.mParams) {
                jsonObject.put(p.getKey(), p.getObject());
            }
            String json = jsonObject.toString();
            Log.d(TAG, "request json = " + json);
            return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
        }

        FormBody.Builder builder = new FormBody.Builder();
        for(RequestParam p : mBuilder.mParams) {
            builder.add(p.getKey(), p.getObject() == null ? "" : p.getObject().toString());
        }
        return builder.build();
    }

    public void enqueue(HttpCallback callback) {
        OkHttpManager.getInstance().request(this, callback);
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder {

        private String url;
        private  String method;
        private boolean isJsonParam;
        private List<RequestParam> mParams;

        private Builder() {
            method = "GET";
        }

        public SimpleHttpClient build() {
            return new SimpleHttpClient(this);
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder get() {
            method = "GET";
            return this;
        }

        /**
         * Form
         * @return
         */
        public Builder post() {
            method = "POST";
            return this;
        }

        /**
         * JSON
         * @return
         */
        public Builder json() {
            isJsonParam = true;
            return post();
        }

        public Builder addParam(String key, Object value) {
            if (mParams == null) {
                mParams = new ArrayList<>();
            }
            mParams.add(new RequestParam(key, value));

            return this;
        }
    }
}
