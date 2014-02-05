package es.udc.fi.dc.photoalbum.wicket.pages.auth;

import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.Search;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharedUserAlbums;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.BasePage;

/**
 */
@SuppressWarnings("serial")
public class BasePageAuth extends BasePage {

    /**
     * Constructor for BasePageAuth.
     * 
     * @param parameters
     *            PageParameters
     */
    public BasePageAuth(final PageParameters parameters) {
        super(parameters);
        add(new BookmarkablePageLink<Void>("hottestPics", HottestPics.class));
        add(new BookmarkablePageLink<Void>("albums", Albums.class));
        add(new BookmarkablePageLink<Void>("sharedUserAlbums",
                SharedUserAlbums.class));
        add(new BookmarkablePageLink<Void>("profile", Profile.class));
        add(new BookmarkablePageLink<Void>("search", Search.class));
    }
}
