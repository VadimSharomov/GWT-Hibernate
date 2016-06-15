package client.login;

import com.google.gwt.user.client.ui.*;

interface LoginView {

    Label getLabel1();

    Label getLabel2();

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
