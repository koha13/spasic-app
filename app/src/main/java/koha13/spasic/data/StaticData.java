package koha13.spasic.data;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.model.Playlist;
import koha13.spasic.model.Song;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class StaticData {
    public static List<Song> songs = new ArrayList<>();
    public static List<Playlist> playlists = new ArrayList<>();

    public static void updateAllSongs(List<Song> data){
        songs = data;
    }
}
