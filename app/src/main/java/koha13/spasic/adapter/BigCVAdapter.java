package koha13.spasic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.model.Song;

public class BigCVAdapter extends RecyclerView.Adapter<BigCVAdapter.SongViewHolder>{

    private List<Song> songs;

    public BigCVAdapter(List<Song> songs){
        this.songs = songs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.big_card_view, parent, false);
        BigCVAdapter.SongViewHolder svh = new BigCVAdapter.SongViewHolder(v);
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

    public static class SongViewHolder extends RecyclerView.ViewHolder{
        TextView songName;
        TextView songArtist;
        ImageView imageView;

        SongViewHolder(View itemView){
            super(itemView);
            songName = (TextView)itemView.findViewById(R.id.cv_big_song_name);
            songArtist = (TextView)itemView.findViewById(R.id.cv_big_song_artist);
            imageView = (ImageView)itemView.findViewById(R.id.cv_big_image);
        }
    }
}
