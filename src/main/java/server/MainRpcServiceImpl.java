package server;

import client.MainRpcService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import shared.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vadim on 16.05.2016.
 *
 */
public class MainRpcServiceImpl extends RemoteServiceServlet implements MainRpcService {

    private SessionFactory sessionFactory;
    private Session session;
    private List usersList = new ArrayList<>();

    public MainRpcServiceImpl() {
        sessionFactory = HibernateUtil.getSessionFactory();
        session = sessionFactory.openSession();
    }

    @Override
    public List<User> getAllUsers() {
        Query query = session.createQuery("from User");
        usersList = query.list();
        return usersList;
    }

    @Override
    public User loginUser(String login, String password) {
        Query query = session.createQuery("from User");
        usersList = query.list();
        for (Object us : usersList) {
            if (login.equals(((User) us).getLogin())) {
                return (User) us;
            }
        }
        return null;
    }

    @Override
    public User saveUser(User user) {
        session.save(user);
        return null;
    }

}
