package client.login;


import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;

/**
 * Created by Vadim on 16.05.2016.
 *
 */
public interface LoginResources extends ClientBundle  {

    public interface MyCss extends CssResource {
        String blackText();

        String blackTextCenter();

        String redText();

        String loginButton();

        String box();

        String background();

        String footer();
    }

    @Source("Login.css")
    MyCss style();

}
