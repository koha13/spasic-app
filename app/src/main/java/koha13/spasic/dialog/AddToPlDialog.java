package koha13.spasic.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import koha13.spasic.R;
import koha13.spasic.data.AllPlaylistsViewModel;
import koha13.spasic.entity.Playlist;
import koha13.spasic.entity.Song;

public class AddToPlDialog {
    private Song song;
    private Context mContext;
    private List<Playlist> pls;
    private View view;

    public AddToPlDialog(Song song, Context mContext) {
        this.song = song;
        this.mContext = mContext;
        pls = AllPlaylistsViewModel.getAllPlaylists().getValue();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AlertDialog getDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        List<String> plsName = new ArrayList<>();
        for (Playlist pl : pls) {
            plsName.add(pl.getName());
        }
        return dialogBuilder.setTitle("Thêm bài playlist:").setMultiChoiceItems(plsName.toArray(new
                        CharSequence[plsName.size()]), checkSub(),
                new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            AllPlaylistsViewModel.addSongToPl(pls.get(which).getId(), song);
                        } else {
                            AllPlaylistsViewModel.deleteSongFromPl(pls.get(which).getId(), song);
                        }
                    }
                }).setNegativeButton("Xong", null).setPositiveButton("Thêm vào playlist mới",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        view = View.inflate(mContext, R.layout.create_pl_dialog, null);
                        AlertDialog.Builder createDialogBuilder = new AlertDialog.Builder(dialogBuilder.getContext());
                        createDialogBuilder.setTitle("Thêm vào playlist mới:").setView(view)
                                .setNegativeButton("Hủy", null)
                                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        EditText newPlName = view.findViewById(R.id.create_pl_ed);
                                        AllPlaylistsViewModel.createAndAddSongToPl(newPlName.getText().toString(), song);
                                    }
                                }).show();
                    }
                }).create();
    }

    private boolean[] checkSub() {
        boolean[] rs = new boolean[pls.size()];
        Arrays.fill(rs, false);
        for (int i = 0; i < pls.size(); i++) {
            for (Song s : pls.get(i).getSongs()) {
                if (s.getId() == song.getId()) {
                    rs[i] = true;
                    break;
                }
            }
        }
        return rs;
    }

}
