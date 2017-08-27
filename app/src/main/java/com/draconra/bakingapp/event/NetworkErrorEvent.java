package com.draconra.bakingapp.event;

public class NetworkErrorEvent {
    private String message;

    public NetworkErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

