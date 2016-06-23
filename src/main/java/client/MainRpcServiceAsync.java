package client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.User;

public interface MainRpcServiceAsync {

    void loginUser(String login, String password, AsyncCallback<User> async);

    void logOut(String login, AsyncCallback<Void> async);

    void getGreeting(String greeting, AsyncCallback<String> async);
}
