package com.leafnext.kickbackmoviedatabase.model;

import android.os.Parcel;


public class MovieInfo implements android.os.Parcelable {

    private String originalTitle;
    private String poster;
    private String overview;
    private String releaseDate;
    private String thumbnailImage;
    private String voteAverage;

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

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


    public MovieInfo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.originalTitle);
        dest.writeString(this.poster);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.thumbnailImage);
        dest.writeString(this.voteAverage);
    }

    private MovieInfo(Parcel in) {
        this.originalTitle = in.readString();
        this.poster = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.thumbnailImage = in.readString();
        this.voteAverage = in.readString();
    }

    public static final Creator<MovieInfo> CREATOR = new Creator<MovieInfo>() {
        @Override
        public MovieInfo createFromParcel(Parcel source) {
            return new MovieInfo(source);
        }

        @Override
        public MovieInfo[] newArray(int size) {
            return new MovieInfo[size];
        }
    };
}
