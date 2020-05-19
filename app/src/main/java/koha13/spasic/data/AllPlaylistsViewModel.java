package koha13.spasic.data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import koha13.spasic.api.ResponseCallBack;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.model.Playlist;
import koha13.spasic.model.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPlaylistsViewModel extends ViewModel {
    private static MutableLiveData<List<Playlist>> allPlaylists = new MutableLiveData<>();

    public static MutableLiveData<List<Playlist>> getAllPlaylists() {
        return allPlaylists;
    }

    public static void fetchAllPlaylists(final ResponseCallBack<List<Playlist>> callBack) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getAllPlaylists("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIn0.YfClARxxRTXTnXeVO-0cqQ81V2gjllSbG5IrNmUvN9s").enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    allPlaylists.setValue(response.body());
                    if (callBack != null)
                        callBack.onDataSuccess(response.body());
                } else {
                    if (callBack != null) {
                        try {
                            callBack.onDataFail(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                t.printStackTrace();
                if (callBack != null)
                    callBack.onFailed(t);
            }
        });
    }

    public static Playlist getPlByID(int id) {
        List<Playlist> pls = allPlaylists.getValue();
        for (int i = 0; i < pls.size(); i++) {
            if (pls.get(i).getId() == id) {
                return pls.get(i);
            }
        }
        return new Playlist();
    }
}
