package hud.app.event_management.utils.userExtractor;


import hud.app.event_management.model.UserAccount;

public interface LoggedUser {

    UserInfo getInfo();

    UserAccount getUser();
}

