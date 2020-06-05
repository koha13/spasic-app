package koha13.spasic.adapter;

import android.content.Context;
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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import koha13.spasic.AddToPlDialog;
import koha13.spasic.R;
import koha13.spasic.entity.Song;
import koha13.spasic.utils.GeneralDTO;

import static koha13.spasic.activity.MainActivity.musicService;


public class BigCVAdapter extends RecyclerView.Adapter<BigCVAdapter.SongViewHolder> {

    private List<Song> songs;
    private Context mContext;

    public BigCVAdapter(List<Song> songs, Context context) {
        this.songs = songs;
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
        final Song currentSong = songs.get(position);
        holder.songName.setText(currentSong.getName());
        holder.songArtist.setText(currentSong.getArtists());
        Picasso.get().load(currentSong.getSongImage()).into(holder.imageView);
        holder.time.setText(GeneralDTO.secondToMinute(currentSong.getLength()));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "click cv", Toast.LENGTH_SHORT).show();
//                Uri uri = Uri.parse(currentSong.getLink());
//                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                intent.setDataAndType(uri, "audio/*");
                System.out.println("Music service: "+ musicService);
                System.out.println("Possition:"+position);
                musicService.setSongPos(position);
                musicService.playSong();
//                mContext.startActivity(intent);
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
                                break;
                            case R.id.btn_add_to_pl:
                                new AddToPlDialog(songs.get(position),mContext).getDialog().show();
                                break;
                            case R.id.btn_add_to_queue:
                                Toast.makeText(mContext, "Them vao ds", Toast.LENGTH_SHORT).show();
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

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView songName;
        TextView songArtist;
        ImageView imageView;
        ImageButton menu;
        TextView time;

        SongViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.cv_big_song);
            songName = itemView.findViewById(R.id.cv_big_song_name);
            songArtist = itemView.findViewById(R.id.cv_big_song_artist);
            imageView = itemView.findViewById(R.id.cv_big_image);
            menu = itemView.findViewById(R.id.menu_cv_big);
            time = itemView.findViewById(R.id.cv_big_time);
        }
    }
}
