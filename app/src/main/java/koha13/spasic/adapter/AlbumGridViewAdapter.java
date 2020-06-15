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

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.activity.AlbumDetailActivity;
import koha13.spasic.entity.Album;

public class AlbumGridViewAdapter extends BaseAdapter {

    List<Album> albums;
    Context mContext;

    public AlbumGridViewAdapter(List<Album> albums, Context mContext) {
        this.mContext = mContext;
        this.albums = albums;
    }

    public void addAlbums(List<Album> albums) {
        if (albums == null) albums = new ArrayList<>();
        this.albums.addAll(albums);
        notifyDataSetChanged();
    }

    public void reset() {
        this.albums = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Object getItem(int position) {
        return albums.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.album_grid_item, null);
        ImageView image = convertView.findViewById(R.id.grid_item_image);
        TextView albumName = convertView.findViewById(R.id.grid_item_name);
        TextView artistName = convertView.findViewById(R.id.grid_item_artist);
        RelativeLayout item = convertView.findViewById(R.id.album_item);

        Glide.with(mContext)
                .load(albums.get(position).getImageLink())
                .into(image);
        albumName.setText(albums.get(position).getName());
        artistName.setText(albums.get(position).getArtist());
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, AlbumDetailActivity.class);
                intent.putExtra("albumName", albums.get(position).getName());
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
}
