package com.example.simplehttpclient.okhttp;

import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;

/**
 * Created by huangyanzhen on 2016/12/5.
 */

public abstract class HttpCallback<T> {

    public Type mType;

    public HttpCallback() {
        mType = getSuperClassTypeParameter(this.getClass());
    }

    private static Type getSuperClassTypeParameter(Class<?> subClass) {
        Type superClass = subClass.getGenericSuperclass();
        if (superClass instanceof  Class) {
            return null;
        }
        ParameterizedType parameterizedType = (ParameterizedType) superClass;
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

    public void onSuccess(T t) {};
    public void onFailure(Call call, IOException e) {};
    public void onError(int statusCode) {};
}
