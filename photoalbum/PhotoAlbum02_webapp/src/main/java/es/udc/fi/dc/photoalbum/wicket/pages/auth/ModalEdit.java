package es.udc.fi.dc.photoalbum.wicket.pages.auth;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import es.udc.fi.dc.photoalbum.hibernate.Comment;
import es.udc.fi.dc.photoalbum.spring.CommentService;

/**
 */
@SuppressWarnings("serial")
public class ModalEdit extends WebPage {

    private String result;
    @SpringBean
    private CommentService commentService;
    private ModalWindow window;
    private FeedbackPanel feedback;
    private Comment comment;

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
     * Constructor for ModalEdit.
     * 
     * @param comment
     *            Comment
     * @param window
     *            ModalWindow
     */
    public ModalEdit(final Comment comment, final ModalWindow window) {
        this.result = comment.getCommentText();
        this.window = window;
        this.comment = comment;
        Form<Void> form = new Form<Void>("form");
        final FeedbackPanel feedback = new FeedbackPanel("feedback");
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        this.feedback = feedback;
        TextArea<String> name = new TextArea<String>("name",
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
                System.out.println("RESULT:" + result);
                commentService.updateCommentText(comment.getId(), result);
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
