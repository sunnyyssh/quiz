package com.example.quiz.network;

import android.content.Context;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtils {
    public static <T> void enqueueWithRetry(Call<T> call, Callback<T> callback) {
        call.enqueue(new RetryableCallback<T>(call, callback));
    }

    public static void showErrorMessage(Context context, Throwable t) {
        String message = "Network error: " + t.getMessage();
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

class RetryableCallback<T> implements Callback<T> {
    private final Call<T> call;
    private final Callback<T> callback;
    private int retryCount = 0;
    private static final int MAX_RETRIES = 3;

    RetryableCallback(Call<T> call, Callback<T> callback) {
        this.call = call;
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        callback.onResponse(call, response);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (retryCount++ < MAX_RETRIES) {
            call.clone().enqueue(this);
        } else {
            callback.onFailure(call, t);
        }
    }
}