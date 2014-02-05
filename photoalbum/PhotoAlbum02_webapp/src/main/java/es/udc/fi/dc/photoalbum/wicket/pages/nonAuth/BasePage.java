package es.udc.fi.dc.photoalbum.wicket.pages.nonAuth;

import java.util.Locale;

import org.apache.wicket.Session;
import org.apache.wicket.devutils.debugbar.DebugBar;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.spring.UserService;
import es.udc.fi.dc.photoalbum.wicket.MySession;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.SignOut;

/**
 */
@SuppressWarnings("serial")
public class BasePage extends WebPage {
    @SpringBean
    private UserService userService;

    /**
     * Constructor for BasePage.
     * 
     * @param parameters
     *            PageParameters
     */
    public BasePage(final PageParameters parameters) {
        super(parameters);
        add(new DebugBar("debug"));
        add(homePageLink("home").add(new Label("header", "Photo Album")));
        add(new Link<Void>("goEnglish") {
            public void onClick() {
                getSession().setLocale(Locale.US);
            }
        });
        add(new Link<Void>("goSpanish") {
            public void onClick() {
                getSession().setLocale(new Locale("es", "ES"));
            }
        });
        add(new BookmarkablePageLink<Void>("signout", SignOut.class, null) {
            @Override
            public boolean isVisible() {
                return ((MySession) Session.get()).isAuthenticated();
            }
        });
        if (((MySession) Session.get()).isAuthenticated()) {
            add(new Label("fullname", userService.getById(
                    ((MySession) Session.get()).getuId()).getUsername()));
        } else {
            add(new Label("fullname"));
        }
    }
}
