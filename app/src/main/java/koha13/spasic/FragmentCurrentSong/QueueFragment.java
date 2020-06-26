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

import koha13.spasic.R;
import koha13.spasic.adapter.QueueSongAdapter;
import koha13.spasic.data.SongControlViewModel;
import koha13.spasic.entity.Song;

public class QueueFragment extends Fragment {

    SongControlViewModel songControlViewModel;
    QueueSongAdapter songCardAdapter;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_queue, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerQueue);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        songCardAdapter = new QueueSongAdapter(getActivity());
        ItemTouchHelper.Callback callback = new ItemTouchHelperCallBack(songCardAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        songCardAdapter.setTouchHelper(touchHelper);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(songCardAdapter);
        songControlViewModel = ViewModelProviders.of(getActivity()).get(SongControlViewModel.class);
        SongControlViewModel.currentSong.observe(getActivity(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                songCardAdapter.notifyDataSetChanged();
                int checkStart = layoutManager.findFirstVisibleItemPosition();
                int checkEnd = layoutManager.findLastVisibleItemPosition();
                int index = 0;
                for (index = 0; index < SongControlViewModel.queueSongs.size(); index++) {
                    if (SongControlViewModel.queueSongs.get(index).getId() == SongControlViewModel.currentSong.getValue().getId()) {
                        break;
                    }
                }
                if (checkStart < index || checkEnd > index) {
                    layoutManager.scrollToPosition(index);
                }
            }
        });
        SongControlViewModel.randomState.observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                songCardAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }

    public interface ItemTouchHelperAdapter {
        boolean onItemMove(int fromPosition, int toPosition);

        void onItemDismiss(int position);
    }

    public class ItemTouchHelperCallBack extends ItemTouchHelper.Callback {

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
