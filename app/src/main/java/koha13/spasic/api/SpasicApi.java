package koha13.spasic.api;

import java.util.List;

import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;
import koha13.spasic.entity.User;
import koha13.spasic.model.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpasicApi {
    @GET("songs")
    Call<List<Song>> getAllSongs(@Header("Authorization") String token);

    @GET("playlists")
    Call<List<Playlist>> getAllPlaylists(@Header("Authorization") String token);

    @POST("auth/login")
    Call<User> login(@Body LoginRequest loginRequest);

    @POST("playlists/{id}/song")
    Call<Object> addSongToPl(@Path("id") int id, @Query("idSong") int idSong, @Header("Authorization") String token);

    @GET("playlists/{id}/deletesong")
    Call<Object> deleteSongFromPl(@Path("id") int id, @Query("idSong") int idSong, @Header("Authorization") String token);

    @POST("playlists/add")
    Call<Playlist> createPl(@Query("name") String plName, @Header("Authorization") String token);

    @GET("playlists/delete/{id}")
    Call<Object> deletePl(@Path("id") int id, @Header("Authorization") String token);
}
