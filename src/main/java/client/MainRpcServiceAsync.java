package client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import shared.User;

import java.util.List;

public interface MainRpcServiceAsync {

    void getAllUsers(AsyncCallback<List<User>> async);

    void saveUser(User user, AsyncCallback<User> async);

    void loginUser(String login, String password, AsyncCallback<User> async);
}
