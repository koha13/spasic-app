package koha13.spasic.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
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
    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;

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
        registerBecomingNoisyReceiver();
        callStateListener();
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

    //Handle headphone remove
    //Becoming noisy
    private BroadcastReceiver becomingNoisyReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //pause audio on ACTION_AUDIO_BECOMING_NOISY
            pauseSong();
//            buildNotification(PlaybackStatus.PAUSED);
        }
    };

    private void registerBecomingNoisyReceiver() {
        //register after getting audio focus
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(becomingNoisyReceiver, intentFilter);
    }

    //Handle incoming phone calls
    private void callStateListener() {
        // Get the telephony manager
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //Starting listening for PhoneState changes
        phoneStateListener = new PhoneStateListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {
                switch (state) {
                    //if at least one call exists or the phone is ringing
                    //pause the MediaPlayer
                    case TelephonyManager.CALL_STATE_OFFHOOK:
                    case TelephonyManager.CALL_STATE_RINGING:
                        if (player != null) {
                            pauseSong();
                            ongoingCall = true;
                        }
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        // Phone idle. Start playing.
                        if (player != null) {
                            if (ongoingCall) {
                                ongoingCall = false;
                                playSong();
                            }
                        }
                        break;
                }
            }
        };
        // Register the listener with the telephony manager
        // Listen for changes to the device call state.
        telephonyManager.listen(phoneStateListener,
                PhoneStateListener.LISTEN_CALL_STATE);
    }
}

