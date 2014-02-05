package es.udc.fi.dc.photoalbum.wicket.pages.nonAuth;

import static es.udc.fi.dc.photoalbum.wicket.CookiesConstants.COOKIE_EMAIL;
import static es.udc.fi.dc.photoalbum.wicket.CookiesConstants.COOKIE_MAX_AGE;
import static es.udc.fi.dc.photoalbum.wicket.CookiesConstants.COOKIE_PASSWORD;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.cookies.CookieUtils;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;
import es.udc.fi.dc.photoalbum.utils.Validator;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.Albums;

/**
 */
@SuppressWarnings("serial")
public class Login extends BasePage {
    @SpringBean
    private UserService userService;
    private FeedbackPanel feedback;

    /**
     * Constructor for Login.
     * 
     * @param parameters
     *            PageParameters
     */
    public Login(final PageParameters parameters) {
        super(parameters);
        this.feedback = new FeedbackPanel("feedback");
        this.feedback.setOutputMarkupId(true);
        add(this.feedback);
        add(createFormLogin());
    }

    /**
     * Method createFormLogin.
     * 
     * @return Form<User>
     */
    private Form<User> createFormLogin() {
        final CheckBox chk = new CheckBox("bool", Model.of(Boolean.FALSE));
        Form<User> form = new Form<User>("form",
                new CompoundPropertyModel<User>(new User())) {
            @Override
            protected void onSubmit() {
                User user = getModelObject();
                MySession session = (MySession) getSession();
                String email = user.getEmail().toLowerCase();
                if (Validator.isUsernameValid(email)) {
                    user.setUsername(email);
                    User u = userService.getByUsername(user);
                    if (u != null) {
                        email = u.getEmail();
                    }
                }
                User userDB = userService.getUser(email, user.getPassword());
                if (!(userDB == null)) {
                    if (chk.getModelObject()) {
                        CookieUtils cu = new CookieUtils();
                        cu.getSettings().setMaxAge(COOKIE_MAX_AGE);
                        cu.save(COOKIE_EMAIL, user.getEmail());
                        cu.save(COOKIE_PASSWORD,
                                MD5.getHash(user.getPassword()));
                    } else {
                        session.setuId(userDB.getId());
                    }
                    setResponsePage(Albums.class);
                } else {
                    error(new StringResourceModel("login.noSuchUser", this,
                            null).getString());
                }
            }
        };
        RequiredTextField<String> email = new RequiredTextField<String>("email");
        email.setLabel(new StringResourceModel("login.emailOrUsername", this,
                null));
        PasswordTextField password = new PasswordTextField("password");
        password.setLabel(new StringResourceModel("login.passwordField", this,
                null));
        form.add(email);
        form.add(password);
        form.add(new MyAjaxButton("ajax-button", form, feedback));
        form.add(chk);
        return form;
    }

}
