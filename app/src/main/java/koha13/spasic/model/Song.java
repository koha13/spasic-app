package koha13.spasic.model;

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

    public Song(String name, String artists, int length) {
        this.name = name;
        this.artists = artists;
        this.length = length;
    }
}
