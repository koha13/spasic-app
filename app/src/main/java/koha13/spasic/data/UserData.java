package koha13.spasic.data;

import java.io.IOException;
import java.util.List;

import koha13.spasic.api.ResponseCallback;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.User;
import koha13.spasic.model.LoginRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserData {
    public static User user = new User();

    public static void login(LoginRequest loginRequest, final ResponseCallback<User> callback){
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.login(loginRequest).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    if (callback != null)
                        callback.onDataSuccess(response.body());
                } else {
                    if (callback != null) {
                        try {
                            callback.onDataFail(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
