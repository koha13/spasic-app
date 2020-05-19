package koha13.spasic.api;

public interface ResponseCallback<T> {
    void onDataSuccess(T data);
    void onDataFail(String message);
    void onFailed(Throwable error);
}
