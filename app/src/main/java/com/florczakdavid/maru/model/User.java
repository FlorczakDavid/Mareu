package com.florczakdavid.maru.model;

import java.util.UUID;

public class User {
    private UUID uuid;
    private String name;
    private String mail;

    public User(String name) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.mail = name+"@lamzone.com";
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
