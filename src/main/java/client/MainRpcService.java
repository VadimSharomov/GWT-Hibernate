package client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import shared.User;
import java.util.List;

@RemoteServiceRelativePath("MainRpcService")
public interface MainRpcService extends RemoteService {

    List<User> getAllUsers();

    void saveUser(User user);

    User loginUser(String login, String password);

    String getGreeting(String greeting);

}
