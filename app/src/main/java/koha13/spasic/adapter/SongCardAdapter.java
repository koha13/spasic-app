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

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.model.Song;

public class SongCardAdapter extends RecyclerView.Adapter<SongCardAdapter.SongViewHolder> {

    private List<Song> songs;
    private Context mContext;
    private boolean isPLItem = false;

    public SongCardAdapter(List<Song> songs, Context mContext) {
        this.songs = songs;
        this.mContext = mContext;
    }

    public SongCardAdapter(List<Song> songs, Context mContext, boolean isPLItem) {
        this.songs = songs;
        this.mContext = mContext;
        this.isPLItem = isPLItem;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        SongViewHolder svh = new SongViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SongViewHolder holder, int position) {
        holder.songName.setText(songs.get(position).getName());
        holder.songArtist.setText(songs.get(position).getArtist());
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
                if(isPLItem){
                    popupMenu.inflate(R.menu.option_menu_pl_item);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.btn_delete:
                                    Toast.makeText(mContext, "Delete", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.btn_add_after_curent_song:
                                    Toast.makeText(mContext, "Phat tiep theo", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.btn_add_to_pl:
                                    Toast.makeText(mContext, "Them vao pl", Toast.LENGTH_SHORT).show();
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
                }else{
                    popupMenu.inflate(R.menu.option_menu_big_cv);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.btn_add_after_curent_song:
                                    Toast.makeText(mContext, "Phat tiep theo", Toast.LENGTH_SHORT).show();
                                    break;
                                case R.id.btn_add_to_pl:
                                    Toast.makeText(mContext, "Them vao pl", Toast.LENGTH_SHORT).show();
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
                }
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
            cv = (CardView) itemView.findViewById(R.id.cv_song);
            songName = (TextView) itemView.findViewById(R.id.cv_song_name);
            songArtist = (TextView) itemView.findViewById(R.id.cv_song_artist);
            imageView = (ImageView) itemView.findViewById(R.id.cv_image);
            menu = (ImageButton) itemView.findViewById(R.id.menu_cv);
        }
    }
}
