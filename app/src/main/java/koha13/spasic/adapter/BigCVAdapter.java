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

import com.squareup.picasso.Picasso;

import koha13.spasic.AddToPlDialog;
import koha13.spasic.R;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;
import koha13.spasic.utils.GeneralDTO;

import static koha13.spasic.activity.MainActivity.musicService;


public class BigCVAdapter extends RecyclerView.Adapter<BigCVAdapter.SongViewHolder> {

    private Context mContext;

    public BigCVAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_card_view, parent, false);
        BigCVAdapter.SongViewHolder svh = new BigCVAdapter.SongViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SongViewHolder holder, final int position) {
        final Song song = AllSongsViewModel.getAllSongs().get(position);
        holder.songName.setText(song.getName());
        holder.songArtist.setText(song.getArtists());
        Picasso.get().load(song.getSongImage()).into(holder.imageView);
        holder.time.setText(GeneralDTO.secondToMinute(song.getLength()));
        if (SongControlViewModel.currentSong.getValue() != null && song.getId() == SongControlViewModel.currentSong.getValue().getId()) {
            holder.bgCS.setVisibility(View.VISIBLE);
            if (SongControlViewModel.isPlaying.getValue()) {
                holder.iconCS.setImageResource(R.drawable.ic_pause_circle_filled_orange_40dp);
            } else {
                holder.iconCS.setImageResource(R.drawable.ic_play_circle_filled_orange_40dp);
            }
            holder.iconCS.setVisibility(View.VISIBLE);
        }else {
            holder.bgCS.setVisibility(View.GONE);
            holder.iconCS.setVisibility(View.GONE);
        }
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                System.out.println("Music service: " + musicService);
                Toast.makeText(mContext, "Click cv", Toast.LENGTH_SHORT).show();
                musicService.playSong(song);
            }
        });
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(mContext, holder.menu);
                popupMenu.inflate(R.menu.option_menu_big_cv);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.btn_add_after_curent_song:
                                Toast.makeText(mContext, "Phat tiep theo", Toast.LENGTH_SHORT).show();
                                SongControlViewModel.addSongAfterCurrentSong(song);
                                break;
                            case R.id.btn_add_to_pl:
                                new AddToPlDialog(AllSongsViewModel.getAllSongs().get(position), mContext).getDialog().show();
                                break;
                            case R.id.btn_add_to_queue:
                                SongControlViewModel.addSongToQueue(song);
                                Toast.makeText(mContext, "Đã thêm", Toast.LENGTH_SHORT).show();
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
        return AllSongsViewModel.getAllSongs().size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView songName;
        TextView songArtist;
        ImageView imageView;
        ImageButton menu;
        TextView time;
        ImageView bgCS;
        ImageView iconCS;

        SongViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_big_song);
            songName = itemView.findViewById(R.id.cv_big_song_name);
            songArtist = itemView.findViewById(R.id.cv_big_song_artist);
            imageView = itemView.findViewById(R.id.cv_big_image);
            menu = itemView.findViewById(R.id.menu_cv_big);
            time = itemView.findViewById(R.id.cv_big_time);
            bgCS = itemView.findViewById(R.id.bg_cs);
            iconCS = itemView.findViewById(R.id.icon_cs);
        }
    }
}
