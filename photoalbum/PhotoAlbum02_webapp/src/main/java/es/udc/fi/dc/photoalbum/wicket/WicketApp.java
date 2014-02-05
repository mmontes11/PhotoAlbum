package es.udc.fi.dc.photoalbum.wicket;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import es.udc.fi.dc.photoalbum.wicket.pages.auth.Albums;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.ErrorPage404;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.HottestPics;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.Image;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.Profile;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.SignOut;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.Upload;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.Search;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.SearchBig;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.search.SearchFiles;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.ShareAlbum;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharePhoto;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharedAlbums;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharedBig;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharedFiles;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.share.SharedUserAlbums;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.tag.CreateTagAlbum;
import es.udc.fi.dc.photoalbum.wicket.pages.auth.tag.TagPhoto;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.ForgotPassword;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.Login;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.Register;
import es.udc.fi.dc.photoalbum.wicket.pages.nonAuth.RegistryCompleted;

/**
 */
public class WicketApp extends WebApplication {

    public WicketApp() {
    }

    /**
     * Method getHomePage.
     * 
     * @return Class<? extends Page>
     */
    public Class<? extends Page> getHomePage() {
        return Login.class;
    }

    @Override
    protected void init() {
        super.init();
        getComponentInstantiationListeners().add(
                new SpringComponentInjector(this));
        getSecuritySettings().setAuthorizationStrategy(
                new AuthorizationStrategy());
        mountPage("albums", Albums.class);
        mountPage("error404", ErrorPage404.class);
        mountPage("image", Image.class);
        mountPage("register", Register.class);
        mountPage("registerCompleted", RegistryCompleted.class);
        mountPage("shareAlbum", ShareAlbum.class);
        mountPage("sharePhoto", SharePhoto.class);
        mountPage("sharedAlbums", SharedAlbums.class);
        mountPage("sharedBig", SharedBig.class);
        mountPage("sharedFiles", SharedFiles.class);
        mountPage("sharedUsersAlbums", SharedUserAlbums.class);
        mountPage("pics", Upload.class);
        mountPage("profile", Profile.class);
        mountPage("search", Search.class);
        mountPage("searchFiles", SearchFiles.class);
        mountPage("searchBig", SearchBig.class);
        mountPage("forgotPassword", ForgotPassword.class);
        mountPage("signOut", SignOut.class);
        mountPage("createTagAlbum", CreateTagAlbum.class);
        mountPage("tagPhoto", TagPhoto.class);
        mountPage("hottestPics", HottestPics.class);
    }

    /**
     * Method newSession.
     * 
     * @param request
     *            Request
     * @param response
     *            Response
     * @return Session
     */
    @Override
    public Session newSession(Request request, Response response) {
        return new MySession(request);
    }
}
