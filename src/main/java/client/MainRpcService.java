package client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import shared.User;

import java.util.List;

/**
 * Created by Vadim on 16.05.2016.
 *
 */
@RemoteServiceRelativePath("gwtPersistService")
public interface MainRpcService extends RemoteService {

    List<User> getAllUsers();

    User saveUser(User user);

    User loginUser(String login, String password);

}
