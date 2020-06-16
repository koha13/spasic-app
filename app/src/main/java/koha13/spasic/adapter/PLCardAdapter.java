package koha13.spasic.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.activity.PLActivity;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;
import koha13.spasic.utils.GeneralDTO;

public class PLCardAdapter extends RecyclerView.Adapter<PLCardAdapter.PLViewHolder> {

    List<Playlist> pls;
    Context mContext;

    public PLCardAdapter(List<Playlist> pls, Context mContext) {
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
    public void onBindViewHolder(@NonNull PLCardAdapter.PLViewHolder holder, final int position) {
        holder.plName.setText(pls.get(position).getName());
        holder.totalSong.setText(pls.get(position).getSongs().size() + " songs");
        holder.totalTime.setText(GeneralDTO.secondToMinute(pls.get(position).getTotalTime()));
        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, PLActivity.class);
                intent.putExtra("plID", pls.get(position).getId());
                mContext.startActivity(intent);
            }
        });
        holder.play.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                SongControlViewModel.playList(pls.get(position).getSongs());
            }
        });
    }

    @Override
    public int getItemCount() {
        return pls.size();
    }

    public static class PLViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView plName;
        TextView totalSong;
        TextView totalTime;
        ImageButton play;

        PLViewHolder(View itemView) {
            super(itemView);
            cv = itemView.findViewById(R.id.pl_cv);
            plName = itemView.findViewById(R.id.pl_cv_name);
            totalSong = itemView.findViewById(R.id.pl_cv_info);
            totalTime = itemView.findViewById(R.id.pl_cv_time);
            play = itemView.findViewById(R.id.play_pl_cv);
        }
    }
}
