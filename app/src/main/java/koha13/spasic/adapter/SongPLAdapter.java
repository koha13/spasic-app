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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import koha13.spasic.AddToPlDialog;
import koha13.spasic.R;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;
import koha13.spasic.utils.GeneralDTO;

public class SongPLAdapter extends RecyclerView.Adapter<SongPLAdapter.SongViewHolder> {

    private List<Song> songs;
    private Context mContext;
    private int plId;

    public SongPLAdapter(List<Song> songs, Context mContext, int plId) {
        this.songs = songs;
        this.mContext = mContext;
        this.plId = plId;
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
        holder.time.setText(GeneralDTO.secondToMinute(song.getLength()));
        Glide.with(mContext)
                .load(song.getSongImage())
                .into(holder.imageView);
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Business code here
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.menu);
                popupMenu.inflate(R.menu.option_menu_pl_item);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.btn_delete:
                                AllPlaylistsViewModel.deleteSongFromPl(plId, song);
                                break;
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
        TextView time;

        SongViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_song);
            songName = itemView.findViewById(R.id.cv_song_name);
            songArtist = itemView.findViewById(R.id.cv_song_artist);
            imageView = itemView.findViewById(R.id.cv_image);
            menu = itemView.findViewById(R.id.menu_cv);
            time = itemView.findViewById(R.id.cv_time);
        }
    }
}
