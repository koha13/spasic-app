package koha13.spasic.api;

import java.util.List;

import koha13.spasic.entity.Album;
import koha13.spasic.entity.Artist;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;
import koha13.spasic.entity.User;
import koha13.spasic.model.ChangePassRequest;
import koha13.spasic.model.LoginRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SpasicApi {
    @GET("allsongs")
    Call<List<Song>> getAllSongs(@Query("page") int page, @Query("size") int size, @Header("Authorization") String token);

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

    @GET("searchsong")
    Call<List<Song>> searchSong(@Query("key") String key, @Query("page") int page, @Query("size") int size, @Header("Authorization") String token);

    @GET("searchalbum")
    Call<List<Album>> searchAlbum(@Query("key") String key, @Query("page") int page, @Query("size") int size, @Header("Authorization") String token);

    @GET("searchartist")
    Call<List<Artist>> searchArtist(@Query("key") String key, @Query("page") int page, @Query("size") int size, @Header("Authorization") String token);

    @GET("artist")
    Call<List<Song>> getSongByArtist(@Query("key") String key, @Header("Authorization") String token);

    @GET("album")
    Call<List<Song>> getSongByAlbum(@Query("key") String key, @Header("Authorization") String token);

    @POST("upPlay/{songId}")
    Call<Object> upPlay(@Path("songId") int songId, @Header("Authorization") String token);

    @GET("rank")
    Call<List<Song>> getRank(@Header("Authorization") String token);

    @POST("auth/changepass")
    Call<User> changePass(@Body ChangePassRequest changePassRequest, @Header("Authorization") String token);
}
