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

import koha13.spasic.data.FetchApiImpl;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener {
    private final IBinder musicBind = new MusicBinder();
    private MediaPlayer player;

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
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (SongControlViewModel.loopState == 1) {
                    playAgain();
                } else if (SongControlViewModel.loopState == 0) {
                    if (SongControlViewModel.currentSong.getValue().getId() !=
                            SongControlViewModel.queueSongs.get(SongControlViewModel.queueSongs.size() - 1).getId()) {
                        if (!playNextSong()) {
                            SongControlViewModel.isPlaying.postValue(false);
                        }
                    } else {
                        SongControlViewModel.isPlaying.postValue(false);
                    }
                } else {
                    if (!playNextSong()) {
                        SongControlViewModel.isPlaying.postValue(false);
                    }
                }
            }
        });
    }

    public void pauseSong() {
        SongControlViewModel.isPlaying.postValue(false);
        player.pause();
    }

    private void playAgain() {
        FetchApiImpl.upPlay(SongControlViewModel.currentSong.getValue().getId());
        player.seekTo(0);
        player.start();
        SongControlViewModel.isPlaying.postValue(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean playNextSong() {
        Song nextSong = SongControlViewModel.getNextSong();
        if (nextSong != null) {
            playSong(nextSong);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public boolean playPreviousSong() {
        Song prevSong = SongControlViewModel.getPreviousSong();
        if (prevSong != null) {
            playSong(prevSong);
            return true;
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void playSong() {
        playSong(Objects.requireNonNull(SongControlViewModel.currentSong.getValue()));
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void playSong(Song song) {
        if (!song.equals(SongControlViewModel.currentSong.getValue())) {
            FetchApiImpl.upPlay(song.getId());
            player.reset();
            Uri trackUri = Uri.parse(song.getLink());
            try {
                player.setDataSource(getApplicationContext(), trackUri);
                player.prepareAsync();
            } catch (Exception e) {
                Log.e("MUSIC SERVICE", "Error setting data source", e);
            }
            SongControlViewModel.updateCurrentSong(song);
            SongControlViewModel.isPlaying.postValue(true);
            SongControlViewModel.addSongToQueueNoUpdatePos(song);
        } else {
            if (SongControlViewModel.isPlaying.getValue()) {
                pauseSong();
            } else {
                int length = player.getCurrentPosition();
                player.seekTo(length);
                player.start();
                SongControlViewModel.isPlaying.postValue(true);
            }

        }
    }

    public void stopSong() {
        SongControlViewModel.isPlaying.postValue(false);
        player.stop();
    }

    public class MusicBinder extends Binder {
        public MusicBinder() {
        }

        public MusicService getService() {
            return MusicService.this;
        }
    }


}

