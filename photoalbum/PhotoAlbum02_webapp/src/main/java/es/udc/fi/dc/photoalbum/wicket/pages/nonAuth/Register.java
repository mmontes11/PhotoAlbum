package es.udc.fi.dc.photoalbum.wicket.pages.nonAuth;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.EmailAddressValidator;
import org.apache.wicket.validation.validator.StringValidator;
import org.springframework.dao.DataIntegrityViolationException;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;
import es.udc.fi.dc.photoalbum.utils.Validator;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.PasswordPolicyValidator;

/**
 */
@SuppressWarnings("serial")
public class Register extends BasePage {
    @SpringBean
    private UserService userService;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private FeedbackPanel feedback;

    /**
     * Constructor for Register.
     * 
     * @param parameters
     *            PageParameters
     */
    public Register(final PageParameters parameters) {
        super(parameters);
        this.feedback = new FeedbackPanel("feedback");
        this.feedback.setOutputMarkupId(true);
        add(this.feedback);
        add(createFormRegister());
    }

    /**
     * Method createFormRegister.
     * 
     * @return Form<User>
     */
    private Form<User> createFormRegister() {
        Form<User> form = new Form<User>("form",
                new CompoundPropertyModel<User>(new User())) {
            @Override
            protected void onSubmit() {
                User user = getModelObject();
                if (Validator.isUsernameValid(user.getUsername().toLowerCase())) {
                    user.setUsername(user.getUsername().toLowerCase());
                    user.setEmail(user.getEmail().toLowerCase());
                    user.setPassword(MD5.getHash(user.getPassword()));
                    try {
                        userService.create(user);
                        setResponsePage(RegistryCompleted.class);
                    } catch (DataIntegrityViolationException e) {
                        Logger.getLogger(Register.class.getName()).log(
                                Level.WARNING, e.toString(), e);
                        error(new StringResourceModel("register.alreadyExist",
                                this, null).getString());
                    }
                } else {
                    error(new StringResourceModel("validator.errorUsername",
                            this, null).getString());
                }
            }
        };
        RequiredTextField<String> username = new RequiredTextField<String>(
                "username");
        username.setLabel(new StringResourceModel("register.usernameField",
                this, null));
        RequiredTextField<String> email = new RequiredTextField<String>("email");
        email.add(EmailAddressValidator.getInstance());
        email.setLabel(new StringResourceModel("register.emailField", this,
                null));
        PasswordTextField password = new PasswordTextField("password");
        password.add(StringValidator.minimumLength(PASSWORD_MIN_LENGTH));
        password.add(new PasswordPolicyValidator());
        password.setLabel(new StringResourceModel("register.passwordField",
                this, null));
        PasswordTextField passwordAgain = new PasswordTextField(
                "passwordAgain", Model.of(""));
        passwordAgain.setLabel(new StringResourceModel(
                "register.passwordRepeatField", this, null));
        form.add(new EqualPasswordInputValidator(password, passwordAgain));
        form.add(username);
        form.add(email);
        form.add(password);
        form.add(passwordAgain);
        form.add(new MyAjaxButton("ajax-button", form, this.feedback));
        return form;
    }
}
