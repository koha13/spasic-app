package koha13.spasic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.model.Playlist;
import koha13.spasic.utils.GeneralDTO;

public class PLCardAdapter extends RecyclerView.Adapter<PLCardAdapter.PLViewHolder> {

    List<Playlist> pls;
    Context mContext;

    public PLCardAdapter(List<Playlist> pls, Context mContext){
        this.pls = pls;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PLCardAdapter.PLViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pl_card_view, parent, false);
        PLCardAdapter.PLViewHolder svh = new PLCardAdapter.PLViewHolder(v);
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull PLCardAdapter.PLViewHolder holder, int position) {
        holder.plName.setText(pls.get(position).getName());
        holder.totalSong.setText(String.valueOf(pls.get(position).getSongs().size()) + " songs");
        holder.totalTime.setText(GeneralDTO.secondToMinute(pls.get(position).getTotalTime()));
        holder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Business code here
                Toast.makeText(mContext, "Play pl", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return pls.size();
    }

    public static class PLViewHolder extends RecyclerView.ViewHolder{
        TextView plName;
        TextView totalSong;
        TextView totalTime;
        ImageButton play;

        PLViewHolder(View itemView){
            super(itemView);
            plName = (TextView)itemView.findViewById(R.id.pl_cv_name);
            totalSong = (TextView)itemView.findViewById(R.id.pl_cv_info);
            totalTime = (TextView)itemView.findViewById(R.id.pl_cv_time);
            play = (ImageButton)itemView.findViewById(R.id.play_pl_cv);
        }
    }
}
