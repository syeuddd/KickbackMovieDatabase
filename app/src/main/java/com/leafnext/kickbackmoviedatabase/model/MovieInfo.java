package com.leafnext.kickbackmoviedatabase.model;

import android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by syedehteshamuddin on 2018-02-07.
 */

public class MovieInfo {

    private String originalTitle;
    private String poster;
    private String overview;
    private String releaseDate;
    private String thumbnailImage;


    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(String thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }



}
