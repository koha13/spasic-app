package koha13.spasic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.entity.Artist;

public class ArtistGridViewAdapter extends BaseAdapter {

    List<Artist> artists;
    Context mContext;
    int num = -1;

    public ArtistGridViewAdapter(List<Artist> artists, Context mContext) {
        this.mContext = mContext;
        this.artists = artists;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int getCount() {
        if (num > -1) return num;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.artist_grid_item, null);
        ImageView image = convertView.findViewById(R.id.grid_item_image);
        TextView artistName = convertView.findViewById(R.id.grid_item_name);

        Picasso.get().load(artists.get(position).getImageLink()).into(image);
        artistName.setText(artists.get(position).getName());

        return convertView;
    }
}
