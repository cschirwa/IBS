package com.kt.ibs.validations;

import java.io.Serializable;

public class Notifications implements Serializable {

    private String level;
    private String message;
    private String field;
    private int code;

    public Notifications() {
    }

    public Notifications(String level, String message, String field, int code) {
        this.setField(field);
        this.setLevel(level);
        this.setMessage(message);
        this.setCode(code);
    }

    public String getLevel() {
        return this.level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getField() {
        return this.field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String toString() {
        return "Notifications{level='" + this.level + '\'' + ", message='" + this.message + '\'' + ", field='" + this.field + '\'' + ", code=" + this.code + '}';
    }
}
