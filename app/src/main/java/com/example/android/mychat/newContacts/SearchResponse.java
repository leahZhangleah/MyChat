package com.example.android.mychat.newContacts;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.android.mychat.User;

import java.util.List;

public class SearchResponse {
    public final Status status;
    @Nullable
    public final List<User> data;
    @Nullable
    public final Throwable error;

    public SearchResponse(Status status, List<User> data, Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static SearchResponse loading(){
        return new SearchResponse(Status.LOADING,null,null);
    }
    public static SearchResponse success(@NonNull List<User> users){
        return new SearchResponse(Status.SUCCESS,users,null);
    }

    public static SearchResponse error(@NonNull Throwable error){
        return new SearchResponse(Status.ERROR,null,error);
    }
}
