package koha13.spasic.data;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import koha13.spasic.api.ResponseCallback;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.entity.Album;
import koha13.spasic.entity.Artist;
import koha13.spasic.entity.Song;
import koha13.spasic.entity.User;
import koha13.spasic.model.ChangePassRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FetchApiImpl {
    public static void searchSong(String key, int page, final ResponseCallback<List<Song>> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.searchSong(key, page, 10, "Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<List<Song>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }

    public static void searchAlbum(String key, int page, final ResponseCallback<List<Album>> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.searchAlbum(key, page, 10, "Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<List<Album>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }

    public static void searchArtist(String key, int page, final ResponseCallback<List<Artist>> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.searchArtist(key, page, 10, "Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }

    public static void getSongByAlbum(String key, final ResponseCallback<List<Song>> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getSongByAlbum(key, "Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<List<Song>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }

    public static void getSongByArtist(String key, final ResponseCallback<List<Song>> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getSongByArtist(key, "Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<List<Song>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }

    public static void upPlay(int id) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.upPlay(id, "Bearer " + UserData.user.getToken()).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                Log.d("Upplay","Done");
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {

            }
        });
    }

    public static void getRank(final ResponseCallback<List<Song>> callback){
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getRank("Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
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
            public void onFailure(Call<List<Song>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
            }
        });
    }

    public static void changePassword(String oldPass, String newPass, final ResponseCallback<User> callback){
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.changePass(new ChangePassRequest(oldPass, newPass), "Bearer " + UserData.user.getToken()).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
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
