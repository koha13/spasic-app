package koha13.spasic.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Playlist {
    private String name;
    List<Song> songs;

    public int getTotalTime(){
        int rs = 0;
        for(int i=0;i<songs.size();i++){
            rs+=songs.get(i).getLength();
        }
        return rs;
    }
}
