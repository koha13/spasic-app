package koha13.spasic.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.Objects;

import koha13.spasic.activity.MainActivity;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private MediaPlayer player;
    private final IBinder musicBind = new MusicBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        player = new MediaPlayer();
        initMusicPlayer();
    }

    public void initMusicPlayer() {
        //set player properties
        player.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }

    public void pauseSong() {
        SongControlViewModel.isPlaying.postValue(false);
        player.pause();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void nextSong() {
        if (SongControlViewModel.getNext()){
            MainActivity.musicService.playSong();
        }
    }


    public class MusicBinder extends Binder {
        public MusicBinder() {
        }

        public MusicService getService() {
            return MusicService.this;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void playSong() {
        playSong(Objects.requireNonNull(SongControlViewModel.currentSong.getValue()));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void playSong(Song song) {
        if (!song.equals(SongControlViewModel.currentSong.getValue())) {
            player.reset();
            Uri trackUri = Uri.parse(song.getLink());
            try {
                player.setDataSource(getApplicationContext(), trackUri);
                player.prepareAsync();
            } catch (Exception e) {
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }
            SongControlViewModel.currentSong.postValue(song);
        } else {
            int length = player.getCurrentPosition();
            player.seekTo(length);
            player.start();
        }
        SongControlViewModel.isPlaying.postValue(true);
    }

    public void stopSong() {
        SongControlViewModel.isPlaying.postValue(false);
        player.stop();
    }


}

