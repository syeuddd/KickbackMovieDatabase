package com.leafnext.kickbackmoviedatabase.model;

import android.os.Parcel;


public class MovieInfo implements android.os.Parcelable {

    private String originalTitle;
    private String posterImage;
    private String movieLength;
    private String overview;
    private String releaseDate;
    private String thumbnailImage;
    private String voteAverage;
    private String trailer1;
    private String trailer2;
    private String trailer3;

    public String getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(String movieLength) {
        this.movieLength = movieLength;
    }

    public String getTrailer1() {
        return trailer1;
    }

    public void setTrailer1(String trailer1) {
        this.trailer1 = trailer1;
    }

    public String getTrailer2() {
        return trailer2;
    }

    public void setTrailer2(String trailer2) {
        this.trailer2 = trailer2;
    }

    public String getTrailer3() {
        return trailer3;
    }

    public void setTrailer3(String trailer3) {
        this.trailer3 = trailer3;
    }

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

    public String getPosterImage() {
        return posterImage;
    }

    public void setPosterImage(String posterImage) {
        this.posterImage = posterImage;
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
        dest.writeString(this.posterImage);
        dest.writeString(this.movieLength);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.thumbnailImage);
        dest.writeString(this.voteAverage);
        dest.writeString(this.trailer1);
        dest.writeString(this.trailer2);
        dest.writeString(this.trailer3);
    }

    protected MovieInfo(Parcel in) {
        this.originalTitle = in.readString();
        this.posterImage = in.readString();
        this.movieLength = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.thumbnailImage = in.readString();
        this.voteAverage = in.readString();
        this.trailer1 = in.readString();
        this.trailer2 = in.readString();
        this.trailer3 = in.readString();
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
