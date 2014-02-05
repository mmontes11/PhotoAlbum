package es.udc.fi.dc.photoalbum.wicket;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.request.component.IRequestablePage;
import org.apache.wicket.request.mapper.parameter.PageParameters;

import es.udc.fi.dc.photoalbum.hibernate.File;
import es.udc.fi.dc.photoalbum.wicket.models.CustomFilesModel;
import es.udc.fi.dc.photoalbum.wicket.models.FileModelForNavigate;

/**
 */
@SuppressWarnings("serial")
public class CustomNavigateForm<T> extends Form<T> {

    /**
     * Constructor for CustomNavigateForm.
     * 
     * @param path
     *            String
     * @param fileId
     *            int
     * @param files
     *            ArrayList<File>
     * @param cls
     *            Class<? extends IRequestablePage>
     */
    public CustomNavigateForm(String path, int fileId, ArrayList<File> files,
            final Class<? extends IRequestablePage> cls) {
        super(path);
        CustomFilesModel ldm = new CustomFilesModel(files);
        Iterator<File> iterator = ldm.getObject().iterator();
        boolean b = true;
        int i = -1;
        while (b) {
            i++;
            if (iterator.next().getId() == fileId) {
                b = false;
            }
        }
        final FileModelForNavigate previous = new FileModelForNavigate(-1);
        final FileModelForNavigate next = new FileModelForNavigate(-1);
        if ((i == 0) && (i + 1 == ldm.getObject().size())) {
            previous.setId(-1);
            next.setId(-1);
        } else if (i == 0) {
            next.setId(ldm.getObject().get(i + 1).getId());
            previous.setId(-1);
        } else if (i + 1 == ldm.getObject().size()) {
            previous.setId(ldm.getObject().get(i - 1).getId());
            next.setId(-1);
        } else {
            previous.setId(ldm.getObject().get(i - 1).getId());
            next.setId(ldm.getObject().get(i + 1).getId());
        }
        Button back = new Button("back") {
            @Override
            public void onSubmit() {
                PageParameters pars = new PageParameters();
                pars.add("album", previous.getObject().getAlbum().getName());
                pars.add("fid", previous.getObject().getId());
                setResponsePage(cls, pars);
            }
        };
        Button forward = new Button("forward") {
            @Override
            public void onSubmit() {
                PageParameters pars = new PageParameters();
                pars.add("album", next.getObject().getAlbum().getName());
                pars.add("fid", next.getObject().getId());
                setResponsePage(cls, pars);
            }
        };
        if (previous.getId() == -1) {
            back.setVisible(false);
        }
        if (next.getId() == -1) {
            forward.setVisible(false);
        }
        add(back);
        add(forward);
    }

    /**
     * Constructor for CustomNavigateForm.
     * 
     * @param path
     *            String
     * @param tagId
     *            int
     * @param fileId
     *            int
     * @param files
     *            ArrayList<File>
     * @param cls
     *            Class<? extends IRequestablePage>
     */
    public CustomNavigateForm(String path, final int tagId, int fileId,
            ArrayList<File> files, final Class<? extends IRequestablePage> cls) {
        super(path);
        CustomFilesModel ldm = new CustomFilesModel(files);
        Iterator<File> iterator = ldm.getObject().iterator();
        boolean b = true;
        int i = -1;
        while (b) {
            i++;
            if (iterator.next().getId() == fileId) {
                b = false;
            }
        }
        final FileModelForNavigate previous = new FileModelForNavigate(-1);
        final FileModelForNavigate next = new FileModelForNavigate(-1);
        if ((i == 0) && (i + 1 == ldm.getObject().size())) {
            previous.setId(-1);
            next.setId(-1);
        } else if (i == 0) {
            next.setId(ldm.getObject().get(i + 1).getId());
            previous.setId(-1);
        } else if (i + 1 == ldm.getObject().size()) {
            previous.setId(ldm.getObject().get(i - 1).getId());
            next.setId(-1);
        } else {
            previous.setId(ldm.getObject().get(i - 1).getId());
            next.setId(ldm.getObject().get(i + 1).getId());
        }
        Button back = new Button("back") {
            @Override
            public void onSubmit() {
                PageParameters pars = new PageParameters();
                pars.add("album", previous.getObject().getAlbum().getName());
                pars.add("fid", previous.getObject().getId());
                pars.add("tag", tagId);
                setResponsePage(cls, pars);
            }
        };
        Button forward = new Button("forward") {
            @Override
            public void onSubmit() {
                PageParameters pars = new PageParameters();
                pars.add("album", next.getObject().getAlbum().getName());
                pars.add("fid", next.getObject().getId());
                pars.add("tag", tagId);
                setResponsePage(cls, pars);
            }
        };
        if (previous.getId() == -1) {
            back.setVisible(false);
        }
        if (next.getId() == -1) {
            forward.setVisible(false);
        }
        add(back);
        add(forward);
    }

    /**
     * Constructor for CustomNavigateForm.
     * 
     * @param path
     *            String
     * @param tagId
     *            int
     * @param fileId
     *            int
     * @param userEmail
     *            String
     * @param files
     *            ArrayList<File>
     * @param cls
     *            Class<? extends IRequestablePage>
     */
    public CustomNavigateForm(String path, final int tagId, int fileId,
            final String userEmail, ArrayList<File> files,
            final Class<? extends IRequestablePage> cls) {
        super(path);
        CustomFilesModel ldm = new CustomFilesModel(files);
        Iterator<File> iterator = ldm.getObject().iterator();
        boolean b = true;
        int i = -1;
        while (b) {
            i++;
            if (iterator.next().getId() == fileId) {
                b = false;
            }
        }
        final FileModelForNavigate previous = new FileModelForNavigate(-1);
        final FileModelForNavigate next = new FileModelForNavigate(-1);
        if ((i == 0) && (i + 1 == ldm.getObject().size())) {
            previous.setId(-1);
            next.setId(-1);
        } else if (i == 0) {
            next.setId(ldm.getObject().get(i + 1).getId());
            previous.setId(-1);
        } else if (i + 1 == ldm.getObject().size()) {
            previous.setId(ldm.getObject().get(i - 1).getId());
            next.setId(-1);
        } else {
            previous.setId(ldm.getObject().get(i - 1).getId());
            next.setId(ldm.getObject().get(i + 1).getId());
        }
        Button back = new Button("back") {
            @Override
            public void onSubmit() {
                PageParameters pars = new PageParameters();
                pars.add("album", previous.getObject().getAlbum().getName());
                pars.add("fid", previous.getObject().getId());
                pars.add("tag", tagId);
                pars.add("user", userEmail);
                setResponsePage(cls, pars);
            }
        };
        Button forward = new Button("forward") {
            @Override
            public void onSubmit() {
                PageParameters pars = new PageParameters();
                pars.add("album", next.getObject().getAlbum().getName());
                pars.add("fid", next.getObject().getId());
                pars.add("tag", tagId);
                pars.add("user", userEmail);
                setResponsePage(cls, pars);
            }
        };
        if (previous.getId() == -1) {
            back.setVisible(false);
        }
        if (next.getId() == -1) {
            forward.setVisible(false);
        }
        add(back);
        add(forward);
    }

}
