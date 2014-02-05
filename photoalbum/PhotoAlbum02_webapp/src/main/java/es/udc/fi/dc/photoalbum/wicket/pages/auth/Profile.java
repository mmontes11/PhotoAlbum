package es.udc.fi.dc.photoalbum.wicket.pages.auth;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.validation.validator.StringValidator;

import es.udc.fi.dc.photoalbum.hibernate.User;
import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.utils.MD5;
import es.udc.fi.dc.photoalbum.wicket.MyAjaxButton;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.PasswordPolicyValidator;

/**
 */
@SuppressWarnings("serial")
public class Profile extends BasePageAuth {
    @SpringBean
    private UserService userService;
    private String newPassword;
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 200;

    /**
     * Method getNewPassword.
     * 
     * @return String
     */
    public String getNewPassword() {
        return this.newPassword;
    }

    /**
     * Method setNewPassword.
     * 
     * @param newPassword
     *            String
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    /**
     * Constructor for Profile.
     * 
     * @param parameters
     *            PageParameters
     */
    public Profile(PageParameters parameters) {
        super(parameters);
        add(new ChangePassForm<Void>("form", new CompoundPropertyModel<User>(
                new User())));
        add(new DeleteForm<Void>("deleteForm", new CompoundPropertyModel<User>(
                new User())));
    }

    /**
	 */
    private class ChangePassForm<T> extends Form<User> {
        /**
         * Constructor for ChangePassForm.
         * 
         * @param id
         *            String
         * @param cpm
         *            CompoundPropertyModel<User>
         */
        public ChangePassForm(String id, CompoundPropertyModel<User> cpm) {
            super(id, cpm);
            PasswordTextField password = new PasswordTextField("password");
            password.setLabel(new StringResourceModel("profile.passwordField",
                    this, null));
            PasswordTextField newPassword = new PasswordTextField(
                    "newPassword", new PropertyModel<String>(Profile.this,
                            "newPassword"));
            newPassword.add(StringValidator.minimumLength(PASSWORD_MIN_LENGTH));
            newPassword.add(new PasswordPolicyValidator());
            newPassword.setLabel(new StringResourceModel(
                    "profile.newPasswordField", this, null));
            add(password);
            add(newPassword);
            FeedbackPanel feedback = new FeedbackPanel("feedback");
            feedback.setOutputMarkupId(true);
            add(feedback);
            add(new MyAjaxButton("ajax-button", this, feedback));
        }

        @Override
        protected void onSubmit() {
            User user = getModelObject();
            user.setEmail(userService.getById(
                    ((MySession) Session.get()).getuId()).getEmail());
            User userDB = userService.getUser(user.getEmail(),
                    user.getPassword());
            if (!(userDB == null)) {
                userDB.setPassword(MD5.getHash(newPassword));
                userService.update(userDB);
                info(new StringResourceModel("profile.passwordChanged", this,
                        null).getString());
            } else {
                error(new StringResourceModel("profile.wrongPassword", this,
                        null).getString());
            }
        }
    }

    /**
	 */
    private class DeleteForm<T> extends Form<User> {
        /**
         * Constructor for DeleteForm.
         * 
         * @param id
         *            String
         * @param cpm
         *            CompoundPropertyModel<User>
         */
        public DeleteForm(String id, CompoundPropertyModel<User> cpm) {
            super(id, cpm);
            final ModalWindow modal = new ModalWindow("modal");
            modal.setPageCreator(new ModalWindow.PageCreator() {
                public Page createPage() {
                    return new ModalDelete(modal);
                }
            });
            modal.setResizable(false);
            modal.setInitialWidth(WINDOW_WIDTH);
            modal.setInitialHeight(WINDOW_HEIGHT);
            modal.setWindowClosedCallback(new ModalWindow.WindowClosedCallback() {
                public void onClose(AjaxRequestTarget target) {
                    setResponsePage(Profile.class);
                }
            });
            add(modal);
            AjaxButton delete = new AjaxButton("delete") {
                @Override
                protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                    modal.show(target);
                }

                @Override
                protected void onError(AjaxRequestTarget target, Form<?> form) {
                }
            };
            add(delete);
        }
    }

}
