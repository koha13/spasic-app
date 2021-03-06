package koha13.spasic.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Objects;

import koha13.spasic.R;
import koha13.spasic.activity.csactivity.CurrentSongActivity;
import koha13.spasic.data.FetchApiImpl;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;
import lombok.SneakyThrows;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, AudioManager.OnAudioFocusChangeListener {

    public static final String ACTION_PLAY = "ACTION_PLAY";
    public static final String ACTION_PAUSE = "ACTION_PAUSE";
    public static final String ACTION_PREVIOUS = "ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "ACTION_NEXT";
    public static final String ACTION_STOP = "ACTION_STOP";
    //AudioPlayer notification ID
    private static final int NOTIFICATION_ID = 101;
    private final IBinder musicBind = new MusicBinder();
    //MediaSession
    private MediaSessionCompat mediaSession;
    private MediaControllerCompat.TransportControls transportControls;
    private MediaPlayer player;
    //Handle incoming phone calls
    private boolean ongoingCall = false;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private AudioManager audioManager;

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
                playAgain();
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

    @SneakyThrows
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();
        SongControlViewModel.isPlaying.postValue(false);
        SongControlViewModel.randomState.postValue(false);
        player = new MediaPlayer();
        initMediaSession();
        initMusicPlayer();
        registerBecomingNoisyReceiver();
        callStateListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        removeNotification();
        removeAudioFocus();
        unregisterReceiver(becomingNoisyReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIncomingActions(intent);
        requestAudioFocus();
        return super.onStartCommand(intent, flags, startId);
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
        buildNotification(PlaybackStatus.PAUSED);
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
        Song currentSongValue = SongControlViewModel.currentSong.getValue();
        playSong(Objects.requireNonNull(currentSongValue));
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
            buildNotification(PlaybackStatus.PLAYING);
        } else {
            if (SongControlViewModel.isPlaying.getValue()) {
                pauseSong();
            } else {
                int length = player.getCurrentPosition();
                player.seekTo(length);
                player.start();
                SongControlViewModel.isPlaying.postValue(true);
                buildNotification(PlaybackStatus.PLAYING);
            }

        }
    }

    public void stopSong() {
        SongControlViewModel.isPlaying.postValue(false);
        player.stop();
        player.release();
    }

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

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initMediaSession() throws RemoteException {
        // Create a new MediaSession
        mediaSession = new MediaSessionCompat(getApplicationContext(), "AudioPlayer");
        //Get MediaSessions transport controls
        transportControls = mediaSession.getController().getTransportControls();
        //set MediaSession -> ready to receive media commands
        mediaSession.setActive(true);
        //indicate that the MediaSession handles transport control commands
        // through its MediaSessionCompat.Callback.
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Attach Callback to receive MediaSession updates
        mediaSession.setCallback(new MediaSessionCompat.Callback() {
            // Implement callbacks
            @Override
            public void onPlay() {
                super.onPlay();
                playSong();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onPause() {
                super.onPause();
                pauseSong();
                buildNotification(PlaybackStatus.PAUSED);
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                playNextSong();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                playPreviousSong();
                buildNotification(PlaybackStatus.PLAYING);
            }

            @Override
            public void onStop() {
                super.onStop();
                removeNotification();
                //Stop the service
                stopSelf();
            }

            @Override
            public void onSeekTo(long position) {
                super.onSeekTo(position);
            }
        });
    }

    private void buildNotification(PlaybackStatus playbackStatus) {

        int notificationAction = android.R.drawable.ic_media_pause;//needs to be initialized
        PendingIntent play_pauseAction = null;

        //Build a new notification according to the current state of the MediaPlayer
        if (playbackStatus == PlaybackStatus.PLAYING) {
            notificationAction = R.drawable.ic_pause_white_24dp;
            //create the pause action
            play_pauseAction = playbackAction(1);
        } else if (playbackStatus == PlaybackStatus.PAUSED) {
            notificationAction = R.drawable.ic_play_circle_filled_white_24dp;
            //create the play action
            play_pauseAction = playbackAction(0);
        }

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(),
                R.drawable.listening); //replace with your own image

        // Create a new Notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setShowWhen(false)
                .setOnlyAlertOnce(true)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        // Attach our MediaSession token
                        .setMediaSession(mediaSession.getSessionToken())
                        // Show our playback controls in the compact notification view.
                        .setShowActionsInCompactView(0, 1, 2))
                // Set the Notification color
                .setColor(getResources().getColor(R.color.colorPrimary))
                // Set the large and small icons
                .setLargeIcon(largeIcon)
                .setSmallIcon(android.R.drawable.stat_sys_headset)
                // Set Notification content information
                .setContentText(SongControlViewModel.currentSong.getValue().getArtists())
                .setContentTitle(SongControlViewModel.currentSong.getValue().getName())
                .setContentInfo(SongControlViewModel.currentSong.getValue().getAlbum())
                // Add playback actions
                .addAction(R.drawable.ic_skip_previous_white_24dp, "previous", playbackAction(3))
                .addAction(notificationAction, "pause", play_pauseAction)
                .addAction(R.drawable.ic_skip_next_white_24dp, "next", playbackAction(2));

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("111", "NOTIFICATION_CHANNEL_NAME", importance);

            notificationBuilder.setChannelId("111");
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(notificationChannel);
        }
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).notify(NOTIFICATION_ID, notificationBuilder.build());
    }

    private void removeNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);
    }

    private PendingIntent playbackAction(int actionNumber) {
        Intent playbackAction = new Intent(this, MusicService.class);
        switch (actionNumber) {
            case 0:
                // Play
                playbackAction.setAction(ACTION_PLAY);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 1:
                // Pause
                playbackAction.setAction(ACTION_PAUSE);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 2:
                // Next track
                playbackAction.setAction(ACTION_NEXT);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            case 3:
                // Previous track
                playbackAction.setAction(ACTION_PREVIOUS);
                return PendingIntent.getService(this, actionNumber, playbackAction, 0);
            default:
                break;
        }
        return null;
    }

    private void handleIncomingActions(Intent playbackAction) {
        if (playbackAction == null || playbackAction.getAction() == null) return;

        String actionString = playbackAction.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {
            transportControls.play();
        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {
            transportControls.pause();
        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {
            transportControls.skipToNext();
        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {
            transportControls.skipToPrevious();
        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
            transportControls.stop();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onAudioFocusChange(int focusChange) {
        //Invoked when the audio focus of the system is updated.
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (player == null) initMusicPlayer();
                else if (!player.isPlaying()) playSong();
                player.setVolume(1.0f, 1.0f);
                break;
            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time: stop playback and release media player
                Log.d("Loss song", "True");
                if (player.isPlaying()) pauseSong();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (player.isPlaying()) pauseSong();
                break;
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (player.isPlaying()) player.setVolume(0.1f, 0.1f);
                break;
        }
    }

    private boolean requestAudioFocus() {
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            //Focus gained
            return true;
        }
        //Could not gain focus
        return false;
    }

    private boolean removeAudioFocus() {
        return AudioManager.AUDIOFOCUS_REQUEST_GRANTED ==
                audioManager.abandonAudioFocus(this);
    }

    public void seekTo(int progress) {
        player.seekTo(progress);
    }

    public int getCurrentPosition() {
        return player.getCurrentPosition();
    }

    public enum PlaybackStatus {
        PLAYING,
        PAUSED
    }

    public class MusicBinder extends Binder {
        public MusicBinder() {
        }

        public MusicService getService() {
            return MusicService.this;
        }
    }
}

