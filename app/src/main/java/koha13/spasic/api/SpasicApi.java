package koha13.spasic.api;

import java.util.List;

import koha13.spasic.model.Playlist;
import koha13.spasic.model.Song;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface SpasicApi {
    @GET("songs")
    Call<List<Song>> getAllSongs(@Header("Authorization") String token);

    @GET("playlists")
    Call<List<Playlist>> getAllPlaylists(@Header("Authorization") String token);
}
