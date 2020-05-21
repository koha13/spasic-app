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

import java.util.Collections;

import koha13.spasic.AddToPlDialog;
import koha13.spasic.FragmentCurrentSong.QueueFragment;
import koha13.spasic.R;
import koha13.spasic.data.SongControlViewModel;

public class QueueSongAdapter extends RecyclerView.Adapter<QueueSongAdapter.SongViewHolder> implements QueueFragment.ItemTouchHelperAdapter {

    private Context mContext;

    public QueueSongAdapter(Context mContext) {
        this.mContext = mContext;
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
        holder.songName.setText(SongControlViewModel.queueSongs.get(position).getName());
        holder.songArtist.setText(SongControlViewModel.queueSongs.get(position).getArtists());
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

                popupMenu.inflate(R.menu.option_menu_queue);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.btn_add_after_curent_song:
                                Toast.makeText(mContext, "Phat tiep theo", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btn_add_to_pl:
                                new AddToPlDialog(SongControlViewModel.queueSongs.get(position),mContext).getDialog().show();
                                break;
                            case R.id.btn_go_artist:
                                Toast.makeText(mContext, "Chuyen den ca si", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.btn_go_album:
                                Toast.makeText(mContext, "Chuyen den album", Toast.LENGTH_SHORT).show();
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
        return SongControlViewModel.queueSongs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(SongControlViewModel.queueSongs, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(SongControlViewModel.queueSongs, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        SongControlViewModel.queueSongs.remove(position);
        notifyItemRemoved(position);
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
