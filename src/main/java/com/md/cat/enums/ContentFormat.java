package com.md.cat.enums;

public enum ContentFormat {
    Tag("Tag"),
    Text("Text"),
    Custom("Custom");
    private final String tag;

    private ContentFormat(String tag) {
        this.tag = tag;
    }

}
