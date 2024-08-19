package com.app.noteimportapplication.models;

import java.time.LocalDateTime;

public class Note {
    private String guid;
    private String comments;
    private LocalDateTime datetime;
    private LocalDateTime modifiedDateTime;
    private String clientGuid;
    private String loggedUser;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public LocalDateTime getModifiedDateTime() {
        return modifiedDateTime;
    }

    public void setModifiedDateTime(LocalDateTime modifiedDateTime) {
        this.modifiedDateTime = modifiedDateTime;
    }

    public String getClientGuid() {
        return clientGuid;
    }

    public void setClientGuid(String clientGuid) {
        this.clientGuid = clientGuid;
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }

    @Override
    public String toString() {
        return "Note{" +
                "guid='" + guid + '\'' +
                ", comments='" + comments + '\'' +
                ", dateTime=" + datetime +
                ", modifiedDateTime=" + modifiedDateTime +
                ", clientGuid='" + clientGuid + '\'' +
                ", loggedUser='" + loggedUser + '\'' +
                '}';
    }
}

