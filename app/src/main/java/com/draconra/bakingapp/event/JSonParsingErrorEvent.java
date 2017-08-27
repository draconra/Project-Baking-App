package com.draconra.bakingapp.event;

public class JSonParsingErrorEvent {
    private String message;

    public JSonParsingErrorEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
