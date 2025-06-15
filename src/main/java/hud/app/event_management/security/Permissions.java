package hud.app.event_management.security;

import lombok.Getter;

@Getter
public enum Permissions {

    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_CREATE("admin:create"),
    ADMIN_DELETE("admin:delete"),

    ;

    private final String permission;

    Permissions(String permission) {
        this.permission = permission;
    }
}
