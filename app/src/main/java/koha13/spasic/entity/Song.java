package koha13.spasic.entity;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Song {
    private String name;
    private String artists;
    private int length;
    private int id;
    private String album;
    private String songImage;
    private String lyric;
    private boolean like;
    private String link;

    public Song(String name, String artists, int length) {
        this.name = name;
        this.artists = artists;
        this.length = length;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id == song.id &&
                Objects.equals(link, song.link);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(id, link);
    }
}
