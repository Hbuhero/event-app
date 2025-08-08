package hud.app.event_management.model;

import lombok.Getter;

public enum EmailType {
    ACCOUNT_ACTIVATION("Activate Your Account"),
    PASSWORD_RESET("Password Reset Request"),
    CATEGORY_SUBSCRIPTION("New Events Added"),
    EVENT_REMAINDER("Upcoming Event - Don't Miss Out!");

    @Getter
    private final String subject;


    EmailType(String subject) {
        this.subject = subject;
    }
}


