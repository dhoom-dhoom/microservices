package io.java.rating.models;

import java.util.Arrays;
import java.util.List;

public class UserRating {
    public List<Rating> userRating;

    public List<Rating> getUserRating(){
        return userRating;
    }
    public void setUserRating(List<Rating> userRating){
        this.userRating = userRating;
    }
}