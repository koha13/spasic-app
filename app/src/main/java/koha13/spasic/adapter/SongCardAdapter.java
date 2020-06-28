package koha13.spasic.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.AddToPlDialog;
import koha13.spasic.R;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

import static koha13.spasic.activity.MainActivity.musicService;

public class SongCardAdapter extends RecyclerView.Adapter<SongCardAdapter.SongViewHolder> {

    private List<Song> songs;
    private Context mContext;

    public SongCardAdapter(List<Song> songs, Context mContext) {
        this.songs = songs;
        this.mContext = mContext;
    }

    public void addSong(List<Song> songs) {
        if (songs == null) songs = new ArrayList<>();
        this.songs.addAll(songs);
        notifyDataSetChanged();
    }

    public void reset() {
        songs = new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        SongViewHolder svh = new SongViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SongViewHolder holder, final int position) {
        final Song song = songs.get(position);
        holder.songName.setText(song.getName());
        holder.songArtist.setText(song.getArtists());
        Glide.with(mContext)
                .load(song.getSongImage())
                .into(holder.imageView);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (SongControlViewModel.currentSong.getValue() != null && SongControlViewModel.currentSong.getValue().getId() == song.getId())
                    return;
                musicService.playSong(song);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.menu);

                popupMenu.inflate(R.menu.option_menu_big_cv);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.btn_add_after_curent_song:
                                SongControlViewModel.addSongAfterCurrentSong(song);
                                break;
                            case R.id.btn_add_to_pl:
                                new AddToPlDialog(song, mContext).getDialog().show();
                                break;
                            case R.id.btn_add_to_queue:
                                SongControlViewModel.addSongToQueue(song);
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });

                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class SongViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView songName;
        TextView songArtist;
        ImageView imageView;
        ImageButton menu;

        SongViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_song);
            songName = itemView.findViewById(R.id.cv_song_name);
            songArtist = itemView.findViewById(R.id.cv_song_artist);
            imageView = itemView.findViewById(R.id.cv_image);
            menu = itemView.findViewById(R.id.menu_cv);
        }
    }
}
