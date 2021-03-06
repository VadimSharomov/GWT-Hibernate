package client;

import client.login.LoginViewImpl;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.TimeZone;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;
import org.slf4j.Logger;
import shared.User;

import java.util.Date;

import static org.slf4j.LoggerFactory.getLogger;



public class Main implements EntryPoint {
    private final static Logger logger = getLogger(Main.class);
    private UserMessages messages = GWT.create(UserMessages.class);
    private static String currentLocal = com.google.gwt.i18n.client.LocaleInfo.getCurrentLocale().getLocaleName();
    private MainRpcServiceAsync rpcService = GWT.create(MainRpcService.class);
    private User currentUser;

    /**
     * Entry point method.
     */
    public void onModuleLoad() {
        final LoginViewImpl loginView = new LoginViewImpl();
        logger.info("Client: onModuleLoad - success");
        logger.info("Client: current locale: " + currentLocal);
        renderingLoginPage(loginView);

        //noinspection Convert2Lambda
        KeyDownHandler keyDownHandler = new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                    keyDownAndClickHandler(loginView);
                }
            }
        };

        loginView.getLoginBox().addKeyDownHandler(keyDownHandler);
        loginView.getPasswordBox().addKeyDownHandler(keyDownHandler);

        //noinspection Convert2Lambda
        loginView.getButtonSubmit().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                keyDownAndClickHandler(loginView);
            }
        });

        RootPanel.get().add(loginView);
    }

    private void keyDownAndClickHandler(final LoginViewImpl loginView) {
        final String login = loginView.getLoginBox().getValue();
        final String password = loginView.getPasswordBox().getValue();

        //send login and password to server
        rpcService.loginUser(login, password, new AsyncCallback<User>() {
            @Override
            public void onFailure(Throwable caught) {
                showError(loginView, "Service not available");
                logger.info("Client: onFailure in rpcService.loginUser");
            }

            @Override
            public void onSuccess(User user) {
                if (user.getLogin() != null) {
                    renderingHomePage(user, loginView);
                    logger.info("Client: User login success: " + user);
                    currentUser = user;
                } else {
                    showError(loginView, messages.loginFailed());
                    logger.info("Client: User login failed: " + login);
                }
            }
        });
    }

    private void showError(LoginViewImpl loginView, String text) {
        loginView.getLabel2().setText(text);
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

        //noinspection Convert2Lambda
        loginView.getLogoutLink().addClickHandler(new ClickHandler() {
            @SuppressWarnings("unchecked")
            @Override
            public void onClick(ClickEvent event) {
                if (currentUser != null) {
                    rpcService.logOut(currentUser.getLogin(), new AsyncCallback() {

                        @Override
                        public void onFailure(Throwable caught) {
                            logger.error("Client: User logout failed: " + currentUser.getLogin() + ", sessionId:" + currentUser.getSessionId());

                        }

                        @Override
                        public void onSuccess(Object result) {
                            logger.info("Client: User logout success: " + currentUser.getLogin() + ", sessionId:" + currentUser.getSessionId());
                            currentUser = null;
                        }
                    });
                }
            }
        });

        String localTime = getUserLocalTime();
        loginView.getLabel1().setText(getGreetingDependingOnTimeOfDay(localTime) + ", " + user.getName() + ".");
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
    private String getUserLocalTime() {
        Date date = new Date();
        DateTimeFormat dtf = DateTimeFormat.getFormat("HH:mm");
        String localTime = dtf.format(date, TimeZone.createTimeZone(date.getTimezoneOffset()));
        logger.info("Client: Local client time: " + localTime);
        return localTime;
    }

    public String getGreetingDependingOnTimeOfDay(String localTime) {
        int hour = Integer.parseInt(localTime.split(":")[0]);
        int minute = Integer.parseInt(localTime.split(":")[1]);
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
}
