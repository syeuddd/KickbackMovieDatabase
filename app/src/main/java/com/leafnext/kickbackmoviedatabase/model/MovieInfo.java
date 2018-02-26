package com.leafnext.kickbackmoviedatabase.model;

import android.os.Parcel;

import java.util.ArrayList;


public class MovieInfo implements android.os.Parcelable {

    private String movieId;
    private String originalTitle;
    private String posterImage;
    private String movieLength;
    private String overview;
    private String releaseDate;
    private String thumbnailImage;
    private String voteAverage;
    private ArrayList<String> trailers;
    private ArrayList<String> reviews;

    public ArrayList<String> getTrailers() {
        return trailers;
    }

    public void setTrailers(ArrayList<String> trailers) {
        this.trailers = trailers;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getMovieLength() {
        return movieLength;
    }

    public void setMovieLength(String movieLength) {
        this.movieLength = movieLength;
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
        dest.writeString(this.movieId);
        dest.writeString(this.originalTitle);
        dest.writeString(this.posterImage);
        dest.writeString(this.movieLength);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.thumbnailImage);
        dest.writeString(this.voteAverage);
        dest.writeStringList(this.trailers);
        dest.writeStringList(this.reviews);
    }

    protected MovieInfo(Parcel in) {
        this.movieId = in.readString();
        this.originalTitle = in.readString();
        this.posterImage = in.readString();
        this.movieLength = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.thumbnailImage = in.readString();
        this.voteAverage = in.readString();
        this.trailers = in.createStringArrayList();
        this.reviews = in.createStringArrayList();
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
