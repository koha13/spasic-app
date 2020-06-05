package koha13.spasic.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.activity.AlbumDetailActivity;
import koha13.spasic.activity.ArtistDetailActivity;
import koha13.spasic.entity.Artist;

public class ArtistGridViewAdapter extends BaseAdapter {

    List<Artist> artists;
    Context mContext;

    public ArtistGridViewAdapter(List<Artist> artists, Context mContext) {
        this.mContext = mContext;
        this.artists = artists;
    }

    public void addArtist(List<Artist> artists) {
        if (artists == null) this.artists = new ArrayList<>();
        this.artists.addAll(artists);
        notifyDataSetChanged();
    }

    public void reset() {
        this.artists = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return artists.size();
    }

    @Override
    public Object getItem(int position) {
        return artists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.artist_grid_item, null);
        ImageView image = convertView.findViewById(R.id.grid_item_image);
        TextView artistName = convertView.findViewById(R.id.grid_item_name);
        RelativeLayout item = convertView.findViewById(R.id.artist_item);

        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ArtistDetailActivity.class);
                intent.putExtra("artistName", artists.get(position).getName());
                mContext.startActivity(intent);
            }
        });
        Picasso.get().load(artists.get(position).getImageLink()).into(image);
        artistName.setText(artists.get(position).getName());

        return convertView;
    }
}
