package koha13.spasic.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import koha13.spasic.api.ResponseCallback;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.entity.Playlist;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPlaylistsViewModel extends ViewModel {
    private static MutableLiveData<List<Playlist>> allPlaylists = new MutableLiveData<>();

    public static MutableLiveData<List<Playlist>> getAllPlaylists() {
        return allPlaylists;
    }

    public static void fetchAllPlaylists(final ResponseCallback<List<Playlist>> callback) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getAllPlaylists("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIn0.YfClARxxRTXTnXeVO-0cqQ81V2gjllSbG5IrNmUvN9s").enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    allPlaylists.setValue(response.body());
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
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                t.printStackTrace();
                if (callback != null)
                    callback.onFailed(t);
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
