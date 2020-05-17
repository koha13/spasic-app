package koha13.spasic.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import koha13.spasic.model.Song;

public class DataViewModel extends ViewModel {
    private MutableLiveData<List<Song>> allSongs;

    public MutableLiveData<List<Song>> getAllSongs(){
        if(allSongs == null){
            allSongs = new MutableLiveData<>();
        }
        return allSongs;
    }

    public void updateAllSongs(List<Song> songs){
        allSongs.setValue(songs);
    }
}
