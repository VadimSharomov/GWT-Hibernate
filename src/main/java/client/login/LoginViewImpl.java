package client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

/**
 * Created by Vadim on 16.05.2016.
 *
 */
public class LoginViewImpl extends Composite implements LoginView {
    private static LoginUiBinder uiBinder = GWT.create(LoginUiBinder.class);

    @UiTemplate("LoginViewImpl.ui.xml")
    interface LoginUiBinder extends UiBinder<Widget, LoginViewImpl> {
    }

    @UiField(provided = true)
    final LoginResources res;

    public LoginViewImpl() {
        this.res = GWT.create(LoginResources.class);
        res.style().ensureInjected();
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiField
    TextBox loginBox;
    @UiField
    TextBox passwordBox;
    @UiField
    Label loginLabel;
    @UiField
    Label passwordLabel;
    @UiField
    Anchor logoutLink;
    @UiField
    Label completionLabel1;
    @UiField
    Label completionLabel2;
    @UiField
    Button buttonSubmit;
    @UiField
    Image imageLogo;
    @UiField
    Label labelFooter;
    @UiField
    Label logoLabel;

    @UiHandler("buttonSubmit")
    void doClickSubmit(ClickEvent event) {

    }

    @UiHandler("loginBox")
    void handleLoginChange(ValueChangeEvent<String> event) {

    }

    @UiHandler("passwordBox")
    void handlePasswordChange(ValueChangeEvent<String> event) {

    }

    @Override
    public Label getCompletionLabel1() {
        return completionLabel1;
    }

    @Override
    public Label getCompletionLabel2() {
        return completionLabel2;
    }

    @Override
    public Image getImageLogo() {
        return imageLogo;
    }

    @Override
    public Label getLabelFooter() {
        return labelFooter;
    }

    @Override
    public Button getButtonSubmit() {
        return buttonSubmit;
    }

    @Override
    public TextBox getLoginBox() {
        return loginBox;
    }

    @Override
    public TextBox getPasswordBox() {
        return passwordBox;
    }

    @Override
    public Label getLoginLabel() {
        return loginLabel;
    }

    @Override
    public Label getPasswordLabel() {
        return passwordLabel;
    }

    @Override
    public Anchor getLogoutLink() {
        return logoutLink;
    }

    @Override
    public Label getLogoLabel() {
        return logoLabel;
    }
}
