package com.draconra.bakingapp.util.network;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class APIError {

    @SerializedName("status_code")
    @Expose
    private Integer statusCode;
    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return statusMessage;
    }
}
