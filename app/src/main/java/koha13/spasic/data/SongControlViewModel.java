package koha13.spasic.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import koha13.spasic.entity.Song;

public class SongControlViewModel extends ViewModel {
    public static List<Song> queueSongs = new ArrayList<>();
    public static List<Song> savedQueueSongs = new ArrayList<>();
    public static MutableLiveData<Song> currentSong = new MutableLiveData<>();
    public static Integer loopState = 0;
    public static Boolean randomState = false;

    public static void updateCurrentSong(Song song) {
        currentSong.setValue(song);
    }

    public static MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();


    public static boolean getPrevious() {
        if (isQueueEmpty()) return false;
        if (queueSongs.get(0).getId() == currentSong.getValue().getId()) {
            updateCurrentSong(queueSongs.get(queueSongs.size() - 1));
            return true;
        }
        for (int index = 1; index < queueSongs.size(); index++) {
            Song prev = queueSongs.get(index - 1);
            if (prev.getId() == currentSong.getValue().getId()) {
                updateCurrentSong(prev);
                return true;
            }
        }
        return false;
    }

    private static boolean isQueueEmpty() {
        return queueSongs == null || queueSongs.size() == 0;
    }

    public static boolean getNext() {
        if (isQueueEmpty()) return false;
        if (queueSongs.get(queueSongs.size() - 1).getId() == currentSong.getValue().getId()) {
            updateCurrentSong(queueSongs.get(0));
            return true;
        }
        for (int index = 1; index < queueSongs.size(); index++) {
            Song next = queueSongs.get(index + 1);
            if (next.getId() == currentSong.getValue().getId()) {
                updateCurrentSong(next);
                return true;
            }
        }
        return false;
    }

    public static void shuffleQueue() {
        Collections.shuffle(SongControlViewModel.queueSongs);
        backupQueue();
    }

    private static void backupQueue() {
        savedQueueSongs = queueSongs;
    }
}
