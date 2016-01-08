package jemboy.buddynavigator.Main;

public interface OnTaskCompleted {
    void onUploadIDCompleted(String response);
    void onDownloadIDCompleted(String response);
    void onDeleteIDCompleted(String response);
    void onUploadCoordinatesCompleted(String response);
    void onDownloadCoordinatesCompleted(String response, String latitude, String longitude);
}
