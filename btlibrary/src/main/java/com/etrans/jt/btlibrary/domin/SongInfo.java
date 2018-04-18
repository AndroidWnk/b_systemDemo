package com.etrans.jt.btlibrary.domin;

/**
 * 单元名称:SongInfo.java
 * Created by fuxiaolei on 2016/7/14.
 * 说明:
 * Last Change by fuxiaolei on 2016/7/14.
 */
public class SongInfo {

    private String mediaTitle;//歌曲名称
    private String mediaArtist;//歌手
    private String mediaAlbum;//专辑
    private String mediaGenre;//媒体类型
    private String mediaTrackNumber;//当前歌曲
    private String mediaTotalTracks;//歌曲总数
    private long mediaDuration;//歌曲时长

    public String getMediaTitle() {
        return mediaTitle;
    }

    public void setMediaTitle(String mediaTitle) {
        this.mediaTitle = mediaTitle;
    }

    public String getMediaArtist() {
        return mediaArtist;
    }

    public void setMediaArtist(String mediaArtist) {
        this.mediaArtist = mediaArtist;
    }

    public String getMediaAlbum() {
        return mediaAlbum;
    }

    public void setMediaAlbum(String mediaAlbum) {
        this.mediaAlbum = mediaAlbum;
    }

    public String getMediaGenre() {
        return mediaGenre;
    }

    public void setMediaGenre(String mediaGenre) {
        this.mediaGenre = mediaGenre;
    }

    public String getMediaTrackNumber() {
        return mediaTrackNumber;
    }

    public void setMediaTrackNumber(String mediaTrackNumber) {
        this.mediaTrackNumber = mediaTrackNumber;
    }

    public String getMediaTotalTracks() {
        return mediaTotalTracks;
    }

    public void setMediaTotalTracks(String mediaTotalTracks) {
        this.mediaTotalTracks = mediaTotalTracks;
    }

    public long getMediaDuration() {
        return mediaDuration;
    }

    public void setMediaDuration(long mediaDuration) {
        this.mediaDuration = mediaDuration;
    }
}
