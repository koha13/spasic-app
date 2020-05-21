package koha13.spasic.FragmentCurrentSong;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import koha13.spasic.R;
import koha13.spasic.adapter.QueueSongAdapter;
import koha13.spasic.data.AllSongsViewModel;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class QueueFragment extends Fragment {

    private RecyclerView.LayoutManager layoutManager;
    private AllSongsViewModel allSongsViewModel;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_queue, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerQueue);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        allSongsViewModel = ViewModelProviders.of(getActivity()).get(AllSongsViewModel.class);
        final Observer<List<Song>> observer = new Observer<List<Song>>() {
            @Override
            public void onChanged(List<Song> songs) {
                SongControlViewModel.queueSongs = songs;
                QueueSongAdapter songCardAdapter = new QueueSongAdapter(getActivity());
                ItemTouchHelper.Callback callback = new ItemTouchHelperCallBack(songCardAdapter);
                ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
                touchHelper.attachToRecyclerView(recyclerView);
                recyclerView.setAdapter(songCardAdapter);
            }
        };
        allSongsViewModel.getAllSongs().observe(getActivity(), observer);

//        List<Song> songs = new ArrayList<>();
//        songs.add(new Song("Test1", "Artist", 123));
//        songs.add(new Song("Test2", "Artist", 123));
//        songs.add(new Song("Test3", "Artist", 123));
//        QueueSongAdapter songCardAdapter = new QueueSongAdapter(songs, getActivity());
//        ItemTouchHelper.Callback callback = new ItemTouchHelperCallBack(songCardAdapter);
//        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
//        touchHelper.attachToRecyclerView(recyclerView);
//        recyclerView.setAdapter(songCardAdapter);
        return rootView;
    }

    public interface ItemTouchHelperAdapter{
        boolean onItemMove(int fromPosition, int toPosition);
        void onItemDismiss(int position);
    }

    public class ItemTouchHelperCallBack extends ItemTouchHelper.Callback{

        private final ItemTouchHelperAdapter mAdapter;

        public ItemTouchHelperCallBack(ItemTouchHelperAdapter mAdapter) {
            this.mAdapter = mAdapter;
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
            return makeMovementFlags(dragFlags, swipeFlags);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            mAdapter.onItemMove(viewHolder.getAdapterPosition(),
                    target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            mAdapter.onItemDismiss(viewHolder.getAdapterPosition());
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return true;
        }
    }
}
