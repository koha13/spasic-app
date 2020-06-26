package koha13.spasic.data;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import koha13.spasic.activity.MainActivity;
import koha13.spasic.entity.Song;

public class SongControlViewModel extends ViewModel {
    public static List<Song> queueSongs = new ArrayList<>();
    public static List<Song> savedQueueSongs = new ArrayList<>();
    public static MutableLiveData<Song> currentSong = new MutableLiveData<>();
    public static Integer loopState = 0;
    public static MutableLiveData<Boolean> randomState = new MutableLiveData<>();
    public static MutableLiveData<Boolean> isPlaying = new MutableLiveData<>();

    public static void updateCurrentSong(Song song) {
        currentSong.setValue(song);
    }

    public static Song getPreviousSong() {
        if (isQueueEmpty() || queueSongs.size() == 1) return null;
        if (queueSongs.get(0).getId() == currentSong.getValue().getId()) {
            return queueSongs.get(queueSongs.size() - 1);
        }
        for (int index = 1; index < queueSongs.size(); index++) {
            if (queueSongs.get(index).getId() == currentSong.getValue().getId()) {
                return queueSongs.get(index - 1);
            }
        }
        return null;
    }

    private static boolean isQueueEmpty() {
        return queueSongs == null || queueSongs.size() == 0;
    }

    public static Song getNextSong() {
        if (isQueueEmpty() || queueSongs.size() == 1) return null;
        if (queueSongs.get(queueSongs.size() - 1).getId() == currentSong.getValue().getId()) {
            return queueSongs.get(0);
        }
        for (int index = 0; index < queueSongs.size() - 1; index++) {
            if (queueSongs.get(index).getId() == currentSong.getValue().getId()) {
                return queueSongs.get(index + 1);
            }
        }
        return null;
    }

    public static void shuffleQueue() {
        saveBackupQueue();
        Collections.shuffle(queueSongs);
        int index = queueSongs.indexOf(currentSong.getValue());
        if (index != -1) {
            queueSongs.remove(index);
            queueSongs.add(0, currentSong.getValue());
        }
        randomState.setValue(true);
    }

    public static void unShuffle() {
        randomState.setValue(false);
        queueSongs = savedQueueSongs;
    }

    private static void saveBackupQueue() {
        savedQueueSongs = new ArrayList<>();
        savedQueueSongs.addAll(queueSongs);
        System.out.println(savedQueueSongs == queueSongs);
    }

    public static boolean addSongToQueue(Song song) {
        int index = queueSongs.indexOf(song);
        if (index != -1) {
            queueSongs.remove(song);
        }
        queueSongs.add(song);
        addSongToSavedQueue(song);
        return true;
    }

    private static void addSongToSavedQueue(Song song) {
        if (randomState.getValue()) {
            int index = savedQueueSongs.indexOf(song);
            if (index == -1) {
                savedQueueSongs.add(song);
            }
        }
    }

    public static boolean addSongToQueueNoUpdatePos(Song song) {
        int index = queueSongs.indexOf(song);
        if (index == -1) {
            queueSongs.add(song);
        }
        addSongToSavedQueue(song);
        return true;
    }

    public static void addSongAfterCurrentSong(Song song) {
        int index = queueSongs.indexOf(currentSong);
        if (index != -1) {
            queueSongs.remove(song);
            queueSongs.add(index + 1, song);
        } else {
            queueSongs.add(song);
            addSongToSavedQueue(song);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void playList(List<Song> songs) {
        if (songs == null || songs.size() == 0) return;
        queueSongs = new ArrayList<>();
        queueSongs.addAll(songs);
        randomState.setValue(false);
        MainActivity.musicService.playSong(queueSongs.get(0));
    }
}
