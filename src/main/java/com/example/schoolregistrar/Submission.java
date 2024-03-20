package com.example.schoolregistrar;

public class Submission {
    private String name;
    private String attachments;

    public Submission(String name, String attachments) {
        this.name = name;
        this.attachments = attachments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttachments() {
        return attachments;
    }

    public void setAttachments(String attachments) {
        this.attachments = attachments;
    }
}
