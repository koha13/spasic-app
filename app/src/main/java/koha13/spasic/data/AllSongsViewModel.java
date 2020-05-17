package koha13.spasic.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import koha13.spasic.api.ResponseCallBack;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.model.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSongsViewModel extends ViewModel {
    private static MutableLiveData<List<Song>> allSongs = new MutableLiveData<>();

    public MutableLiveData<List<Song>> getAllSongs() {
        return allSongs;
    }

    public static void updateAllSongs(List<Song> songs) {
        allSongs.setValue(songs);
    }

    public static void fetchAllSongs(final ResponseCallBack<List<Song>> callBack) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.getAllSongs("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIn0.YfClARxxRTXTnXeVO-0cqQ81V2gjllSbG5IrNmUvN9s").enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    updateAllSongs(response.body());
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
            public void onFailure(Call<List<Song>> call, Throwable t) {
                t.printStackTrace();
                if (callBack != null)
                    callBack.onFailed(t);
            }
        });
    }
}
