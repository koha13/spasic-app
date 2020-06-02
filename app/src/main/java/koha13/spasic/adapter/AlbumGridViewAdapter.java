package koha13.spasic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.entity.Album;
import koha13.spasic.entity.Artist;

public class AlbumGridViewAdapter extends BaseAdapter {

    List<Album> albums;
    Context mContext;
    int num = -1;

    public AlbumGridViewAdapter(List<Album> albums, Context mContext) {
        this.mContext = mContext;
        this.albums = albums;
    }

    public void addAlbums(List<Album> albums){
        if(albums==null) albums = new ArrayList<>();
        this.albums.addAll(albums);
        notifyDataSetChanged();
    }

    public void reset(){
        this.albums = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (num > -1) return num;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.album_grid_item, null);
        ImageView image = convertView.findViewById(R.id.grid_item_image);
        TextView albumName = convertView.findViewById(R.id.grid_item_name);
        TextView artistName = convertView.findViewById(R.id.grid_item_artist);

        Picasso.get().load(albums.get(position).getImageLink()).into(image);
        albumName.setText(albums.get(position).getName());
        artistName.setText(albums.get(position).getArtist());

        return convertView;
    }
}
