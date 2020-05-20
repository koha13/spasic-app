package koha13.spasic.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.io.IOException;
import java.util.List;

import koha13.spasic.api.ResponseCallback;
import koha13.spasic.api.RetrofitClient;
import koha13.spasic.api.SpasicApi;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;
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
        mAPIService.getAllPlaylists("Bearer " + UserData.user.getToken()).enqueue(new Callback<List<Playlist>>() {
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

    public static void addSongToPl(final int plId, final Song song) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.addSongToPl(plId, song.getId(),
                "Bearer " + UserData.user.getToken())
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            List<Playlist> pls = allPlaylists.getValue();
                            for (Playlist pl : pls) {
                                if (pl.getId() == plId) {
                                    pl.getSongs().add(song);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });

    }

    public static void deleteSongFromPl(final int plId, final Song song) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.deleteSongFromPl(plId, song.getId(),
                "Bearer " + UserData.user.getToken())
                .enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        if (response.isSuccessful()) {
                            List<Playlist> pls = allPlaylists.getValue();
                            for (Playlist pl : pls) {
                                if (pl.getId() == plId) {
                                    pl.getSongs().remove(song);
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
    }

    public static void createAndAddSongToPl(String plName, final Song song) {
        SpasicApi mAPIService = RetrofitClient.getAPIService();
        mAPIService.createPl(plName, "Bearer " + UserData.user.getToken()).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if(response.isSuccessful()){
                    List<Playlist> pls = allPlaylists.getValue();
                    pls.add(response.body());
                    addSongToPl(response.body().getId(), song);
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {

            }
        });
    }
}
