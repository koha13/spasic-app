package koha13.spasic.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import koha13.spasic.api.ResponseCallback;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.entity.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSongsViewModel{
    private static List<Song> allSongs = new ArrayList<>();

    public static List<Song> getAllSongs() {
        return allSongs;
    }

    public static void reset(){
        allSongs = new ArrayList<>();
    }

    public static void updateAllSongs(List<Song> songs) {
        allSongs = songs;
    }

    public static void fetchAllSongs(final ResponseCallback<List<Song>> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getAllSongs(0,3,"Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    updateAllSongs(response.body());
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

    public static void moreSongs(int page, final ResponseCallback<List<Song>> callback){
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getAllSongs(page,3,"Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    allSongs.addAll(response.body());
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
}
