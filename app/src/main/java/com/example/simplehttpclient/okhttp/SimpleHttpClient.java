package com.example.simplehttpclient.okhttp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangyanzhen on 2016/12/5.
 */

public class SimpleHttpClient {

    private SimpleHttpClient() {}

    public void enqueue(HttpCallback callback) {

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
            return new SimpleHttpClient();
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
