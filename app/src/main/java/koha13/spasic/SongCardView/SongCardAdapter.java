package koha13.spasic.SongCardView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.model.Song;

public class SongCardAdapter extends RecyclerView.Adapter<SongCardAdapter.SongViewHolder> {

    List<Song> songs;

    public SongCardAdapter(List<Song> songs){
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        SongViewHolder svh = new SongViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        holder.songName.setText(songs.get(position).getName());
        holder.songArtist.setText(songs.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public static class SongViewHolder extends RecyclerView.ViewHolder{
        CardView cv;
        TextView songName;
        TextView songArtist;
        ImageView imageView;

        SongViewHolder(View itemView){
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv_song);
            songName = (TextView)itemView.findViewById(R.id.cv_song_name);
            songArtist = (TextView)itemView.findViewById(R.id.cv_song_artist);
            imageView = (ImageView)itemView.findViewById(R.id.cv_image);
        }
    }
}
