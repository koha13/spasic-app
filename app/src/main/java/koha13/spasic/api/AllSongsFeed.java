package koha13.spasic.api;

import java.io.IOException;
import java.util.List;

import koha13.spasic.model.Song;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllSongsFeed {

    public static void getAllSongs(final ResponseCallBack<List<Song>> callBack){
        SpasicApi mAPIService= RetrofitClient.getAPIService();
        mAPIService.getAllSongs("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIn0.YfClARxxRTXTnXeVO-0cqQ81V2gjllSbG5IrNmUvN9s").enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if(response.isSuccessful()){
                    if (callBack != null)
                        callBack.onDataSuccess(response.body());
                }
                else{
                    if(callBack != null){
                        try {
                            callBack.onDataFail(response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                t.printStackTrace();
                callBack.onFailed(t);
            }
        });
    }
}
