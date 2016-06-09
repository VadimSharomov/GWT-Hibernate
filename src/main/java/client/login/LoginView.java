package client.login;

import com.google.gwt.user.client.ui.*;

/**
 * Created by Vadim on 16.05.2016.
 *
 */
public interface LoginView {

    Label getCompletionLabel1();

    Label getCompletionLabel2();

    Image getImageLogo();

    Label getLabelFooter();

    Button getButtonSubmit();

    TextBox getLoginBox();

    TextBox getPasswordBox();

    Label getLoginLabel();

    Label getPasswordLabel();

    Anchor getLogoutLink();

    Label getLogoLabel();
}
