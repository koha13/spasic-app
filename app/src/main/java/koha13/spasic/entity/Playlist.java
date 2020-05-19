package koha13.spasic.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    private int id;
    private List<Song> songs;
    private String name;
    private String avatar;

    public int getTotalTime() {
        int rs = 0;
        for (int i = 0; i < songs.size(); i++) {
            rs += songs.get(i).getLength();
        }
        return rs;
    }
}
