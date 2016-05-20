package client.login;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
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
    Image imageFooter;
    @UiField
    Label labelFooter;

    private Boolean tooShort = false;

    @UiHandler("buttonSubmit")
    void doClickSubmit(ClickEvent event) {
        if (tooShort) {
//            completionLabel2.setText("Login or Password is too short!");
        }
    }

    @UiHandler("loginBox")
    void handleLoginChange(ValueChangeEvent<String> event) {
        if (event.getValue().length() < 3) {
//            completionLabel2.setText("Login too short (Size must be > 3)");
            tooShort = true;
        } else {
            tooShort = false;
            completionLabel2.setText("");
        }
    }

    @UiHandler("loginBox")
    void handleLoginKeyboardKey(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
        }
    }

    @UiHandler("passwordBox")
    void handlePasswordKeyboardKey(KeyDownEvent event) {
        if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
        }
    }

    @UiHandler("passwordBox")
    void handlePasswordChange(ValueChangeEvent<String> event) {
        if (event.getValue().length() < 3) {
            tooShort = true;
//            completionLabel2.setText("Password too short (Size must be > 3)");
        } else {
            tooShort = false;
            completionLabel2.setText("");
        }
    }

    @Override
    public HasText getUserInputBox() {
        return loginBox;
    }

    @Override
    public HasText getPassInputBox() {
        return passwordBox;
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
}
