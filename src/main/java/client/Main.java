package client;

import client.login.LoginViewImpl;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import shared.User;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Main implements EntryPoint {
    private static Logger logger = Logger.getLogger("MyLogger");
    private UserMessages messages = GWT.create(UserMessages.class);
    private static String currentLocal = com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().getLocaleName();
    private MainRpcServiceAsync rpcService = GWT.create(MainRpcService.class);

    /**
     * Entry point method.
     */
    public void onModuleLoad() {
        final LoginViewImpl loginView = new LoginViewImpl();
        logging("onModuleLoad() - success");
        logging("Current locale: " + currentLocal);
        renderingLoginPage(loginView);

        //noinspection Convert2Lambda
        loginView.getButtonSubmit().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                final String login = loginView.getLoginBox().getValue();
                final String password = loginView.getPasswordBox().getValue();

                //send login and password to server
                rpcService.loginUser(login, password, new AsyncCallback<User>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        showError(loginView, "Service not available");
                        logging("Fail: onFailure() in rpcService.loginUser()");
                    }

                    @Override
                    public void onSuccess(User user) {
                        if (user.getLogin() != null) {
                            renderingHomePage(user, loginView);
                            logging("Login success");
                        } else {
                            showError(loginView, messages.loginFailed());
                            logging("Login failed");
                        }
                    }
                });
            }
        });

        RootPanel.get().add(loginView);
    }

    private void showError(LoginViewImpl loginView, String text) {
        loginView.getLabel2().setText(text);
    }

    private void logging(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    private void renderingLoginPage(LoginViewImpl loginView) {
        loginView.getLoginLabel().setText(messages.login());
        loginView.getPasswordLabel().setText(messages.password());
        loginView.getButtonSubmit().setText(messages.submit());
        loginView.getLogoutLink().setVisible(false);

        showLogoAndCopyright(loginView);
    }

    private void showLogoAndCopyright(LoginViewImpl loginView) {
        loginView.getImageLogo().setUrl("imageLogo.jpg");
        loginView.getLogoLabel().setText("GWT Productivity for developers, performance for users");
        loginView.getLabelFooter().setText("© 2016 Company Name");
    }

    private void renderingHomePage(User user, LoginViewImpl loginView) {
        hideLoginFields(loginView);

        if (currentLocal.contains("default")) {
            loginView.getLogoutLink().setHref("Main.html");
        } else {
            loginView.getLogoutLink().setHref("Main.html?locale=" + currentLocal);
        }
        loginView.getLogoutLink().setText(messages.logout());
        loginView.getLogoutLink().setVisible(true);
        loginView.getLabel1().setText(getGreetingDependingOnTimeOfDay() + ", " + user.getName() + ".");
    }

    private void hideLoginFields(LoginViewImpl loginView) {
        loginView.getLoginLabel().setVisible(false);
        loginView.getLabel2().setText("");
        loginView.getLabel2().setVisible(false);
        loginView.getPasswordLabel().setVisible(false);
        loginView.getLoginBox().setVisible(false);
        loginView.getPasswordBox().setVisible(false);
        loginView.getButtonSubmit().setVisible(false);
    }

    @SuppressWarnings("deprecation")
    private String getGreetingDependingOnTimeOfDay() {
        Date date = new Date();
        DateTimeFormat dtf = DateTimeFormat.getFormat("HH:mm");
        String time = dtf.format(date, TimeZone.createTimeZone(date.getTimezoneOffset()));
        logging("Local client time: " + time);
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        int timeInMinutes = hour * 60 + minute;

        int morning = 6 * 60;
        int day = 11 * 60;
        int evening = 16 * 60;
        int night = 21 * 60;

        if ((timeInMinutes > evening) && (timeInMinutes < night)) {
            return messages.goodEvening();
        } else if ((timeInMinutes > night) || (timeInMinutes < morning)) {
            return messages.goodNight();
        } else if ((timeInMinutes > morning) && (timeInMinutes < day)) {
            return messages.goodMorning();
        } else return messages.goodDay();
    }

    //Method for testing
    public String getGreeting(String greeting) {
        return greeting + ", World!";
    }
}
