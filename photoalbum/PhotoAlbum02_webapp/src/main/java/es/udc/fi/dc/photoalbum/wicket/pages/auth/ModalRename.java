package es.udc.fi.dc.photoalbum.wicket.pages.auth;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.Album;
import es.udc.fi.dc.photoalbum.spring.AlbumService;

/**
 */
@SuppressWarnings("serial")
public class ModalRename extends WebPage {

    private String result;
    @SpringBean
    private AlbumService albumService;
    private ModalWindow window;
    private FeedbackPanel feedback;
    private Album album;

    /**
     * Method getResult.
     * 
     * @return String
     */
    @SuppressWarnings("unused")
    private String getResult() {
        return this.result;
    }

    /**
     * Method setResult.
     * 
     * @param result
     *            String
     */
    @SuppressWarnings("unused")
    private void setResult(String result) {
        this.result = result;
    }

    /**
     * Constructor for ModalRename.
     * 
     * @param album
     *            Album
     * @param window
     *            ModalWindow
     */
    public ModalRename(final Album album, final ModalWindow window) {
        this.result = album.getName();
        this.window = window;
        this.album = album;
        Form<Void> form = new Form<Void>("form");
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        this.feedback = feedback;
        RequiredTextField<String> name = new RequiredTextField<String>("name",
                new PropertyModel<String>(this, "result"));
        name.setLabel(new StringResourceModel("modalRename.albumName", this,
                null));
        form.add(name);
        form.add(createButtonOk());
        form.add(createButtonCancel());
        add(form);
    }

    /**
     * Method createButtonOk.
     * 
     * @return AjaxButton
     */
    private AjaxButton createButtonOk() {
        return new AjaxButton("buttonOk") {
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                albumService.rename(album, result);
                window.close(target);
            }

            public void onError(AjaxRequestTarget target, Form<?> form) {
                target.add(feedback);
            }
        };
    }

    /**
     * Method createButtonCancel.
     * 
     * @return AjaxButton
     */
    private AjaxButton createButtonCancel() {
        return new AjaxButton("buttonCancel") {
            public void onSubmit(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }

            public void onError(AjaxRequestTarget target, Form<?> form) {
                window.close(target);
            }
        };
    }
}
