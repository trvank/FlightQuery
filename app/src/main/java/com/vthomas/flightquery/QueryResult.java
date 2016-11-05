package com.vthomas.flightquery;

/**
 * Created by Thomas on 11/5/2016.
 */
public class QueryResult {
    String speech_string = "";
    String text_string = "";

    public String get_speech_string() {
        return speech_string;
    }

    public String get_text_string() {
        return text_string;
    }

    public void set_speech_string(String speech_string) {
        this.speech_string = speech_string;
    }

    public void set_text_string(String text_string) {
        this.text_string = text_string;
    }
}
