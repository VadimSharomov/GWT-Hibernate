package client;

import com.google.gwt.i18n.client.Messages;

/**
 * Created by Vadim on 19.05.2016.
 *
 */
public interface UserMessages extends Messages{

    @Messages.DefaultMessage("Login")
    String login();

    @Messages.DefaultMessage("Logout")
    String logout();

    @Messages.DefaultMessage("Password")
    String password();

    @Messages.DefaultMessage("Login or password failed")
    String loginFailed();

    @Messages.DefaultMessage("Submit")
    String submit();

    @Messages.DefaultMessage("Good evening")
    String goodEvening();

    @Messages.DefaultMessage("Good night")
    String goodNight();

    @Messages.DefaultMessage("Good morning")
    String goodMorning();

    @Messages.DefaultMessage("Good day")
    String goodDay();
}
