package koha13.spasic.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.entity.Song;

public class SongControlViewModel extends ViewModel {
    public static List<Song> queueSongs = new ArrayList<>();
    public static MutableLiveData<Song> currentSong = new MutableLiveData<>();
    public static MutableLiveData<Integer> loopState = new MutableLiveData<>();
    public static MutableLiveData<Boolean> randomState = new MutableLiveData<>();

    public static void updateCurrentSong(Song song){
        currentSong.setValue(song);
    }

    public static void updateLoopState(int ls){
        loopState.setValue(ls);
    }

    public static void updateRandomState(boolean rs){
        randomState.setValue(rs);
    }
}
