package com.wpi.cs4518.werideshare;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.firebase.database.ServerValue;

import java.util.Map;

/**
 * Created by mrampiah on 11/22/16.
 */
/*
 * An instance of LogEntry represents a user event log, such as signin/out and switching a channel.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LogEntry {
    private String tag;
    private String log;
    private Long time;

    public LogEntry() {}

    public LogEntry(String tag, String log) {
        this.tag = tag;
        this.log = log;
    }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }
    public String getLog() { return log; }
    public void setLog(String log) { this.log = log; }

    public Map<String, String> getTime() { return ServerValue.TIMESTAMP; }
    public void setTime(Long time) { this.time = time; }

    @JsonIgnore
    public Long getTimeLong() { return time; }
}
