package es.udc.fi.dc.photoalbum.utils;

import java.util.Locale;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * Utility for sending mails
 * 
 * @author usuario
 * @version $Revision: 1.0 $
 */
public class Mail {

    private static final String HOST_NAME = "s246.sam-solutions.net";
    private static final int SMTP_PORT = 25;
    private static final String EMAIL = "";
    private static final String LOGIN = "ssav";
    private static final String PASSWORD = "";
    private static final String SUBJECT_REG_EN = "Registration at \"Photo Albums\"";
    private static final String SUBJECT_PASS_EN = "Password recovery";
    private static final String MESSAGE_REG_EN = "Dear User, thanks for registration. Enjoy!";
    private static final String MESSAGE_PASS_EN = "Dear User, you can login with new password and change"
            + " it in Profile. New password is: ";
    // private static final String SUBJECT_REG_RU =
    // "����������� �� \"Photo Albums\"";
    // private static final String SUBJECT_PASS_RU = "�������������� ������";
    // private static final String MESSAGE_REG_RU = "������� �� �����������";
    // private static final String MESSAGE_PASS_RU =
    // "������ �� ������ ����� � ����� ������� � "
    // + "�������� ��� � ���� �������. ����� ������: ";
    private Email email = new SimpleEmail();

    /**
     * @param emailTo
     *            recepient's email
     * 
     * @throws EmailException
     */
    public Mail(String emailTo) throws EmailException {
        this.email.setHostName(HOST_NAME);
        this.email.setSmtpPort(SMTP_PORT);
        this.email.setFrom(EMAIL);
        this.email.setAuthenticator(new DefaultAuthenticator(LOGIN, PASSWORD));
        this.email.addTo(emailTo);
    }

    /**
     * sends mail on registration
     * 
     * 
     * @param locale
     *            Locale
     * @throws EmailException
     */
    public void sendRegister(Locale locale) throws EmailException {
        this.email.setSubject(SUBJECT_REG_EN);
        this.email.setMsg(MESSAGE_REG_EN);
        this.email.send();
    }

    /**
     * sends mail on password forget
     * 
     * 
     * @param password
     *            String
     * @param locale
     *            Locale
     * @throws EmailException
     */
    public void sendPass(String password, Locale locale) throws EmailException {
        this.email.setSubject(SUBJECT_PASS_EN);
        this.email.setMsg(MESSAGE_PASS_EN + password);
        this.email.send();
    }
}