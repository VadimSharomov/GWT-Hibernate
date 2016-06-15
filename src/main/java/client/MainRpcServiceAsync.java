package client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.User;

import java.util.List;

public interface MainRpcServiceAsync {

    void getAllUsers(AsyncCallback<List<User>> async);

    void saveUser(User user, AsyncCallback<Void> async);

    void loginUser(String login, String password, AsyncCallback<User> async);

    void getGreeting(String greeting, AsyncCallback<String> async);
}
