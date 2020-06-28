package koha13.spasic.data;

import android.util.Log;

import com.google.gson.Gson;

import koha13.spasic.api.ResponseCallback;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.entity.User;
import koha13.spasic.model.APIError;
import koha13.spasic.model.LoginRequest;
import lombok.SneakyThrows;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserData {
    public static User user = new User();

    public static void login(LoginRequest loginRequest, final ResponseCallback<User> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.login(loginRequest).enqueue(new Callback<User>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if (callback != null)
                        callback.onDataSuccess(response.body());
                } else {
                    if (callback != null) {
                        Gson gson = new Gson();
                        APIError apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        callback.onDataFail(apiError.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }

    public static void signup(LoginRequest loginRequest, final ResponseCallback<User> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.signup(loginRequest).enqueue(new Callback<User>() {
            @SneakyThrows
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if (callback != null)
                        callback.onDataSuccess(response.body());
                } else {
                    if (callback != null) {
                        Gson gson = new Gson();
//                        Log.d("Error", response.errorBody().string());
                        APIError apiError = gson.fromJson(response.errorBody().string(), APIError.class);
                        callback.onDataFail(apiError.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }
}
