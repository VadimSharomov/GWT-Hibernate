package client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import shared.User;

@RemoteServiceRelativePath("MainRpcService")
public interface MainRpcService extends RemoteService {

    User loginUser(String login, String password);

    void logOut(String login);

    String getGreeting(String greeting);

}
