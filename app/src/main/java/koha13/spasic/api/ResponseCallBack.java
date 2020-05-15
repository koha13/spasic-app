package koha13.spasic.api;

import java.util.List;

import koha13.spasic.model.Song;

public interface ResponseCallBack<T> {
    void onDataSuccess(T data);
    void onDataFail(String message);
    void onFailed(Throwable error);
}
