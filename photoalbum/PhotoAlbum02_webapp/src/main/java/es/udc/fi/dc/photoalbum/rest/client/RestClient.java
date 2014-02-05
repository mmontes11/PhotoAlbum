package es.udc.fi.dc.photoalbum.rest.client;

import java.io.Serializable;
import java.util.List;

import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import es.udc.fi.dc.photoalbum.utils.FileDTO;

/**
 * @author usuario
 * @version $Revision: 1.0 $
 */
@SuppressWarnings("serial")
public class RestClient implements Serializable {

    private static Client client = null;
    private final String ENDPOINT_ADDRESS = "http://localhost:8080/PhotoAlbum02_webservice/ws/search";

    /**
     * Method getClient.
     * 
     * @return Client
     */
    public static Client getClient() {
        if (client == null) {
            ClientConfig cc = new DefaultClientConfig();
            cc.getClasses().add(JacksonJsonProvider.class);
            client = Client.create(cc);
        }
        return client;
    }

    /**
     * @param num
     *            Número de recursos a devolver
     * 
     * @param keywordName
     *            Palabra clave del nombre del recurso
     * 
     * @param keywordComment
     *            Palabra clave de los comentarios del recurso
     * 
     * @param keywordTag
     *            Palabra clave de los tags del recurso
     * 
     * @param hotCriterion
     *            Criterio hot: 0->Numero de Likes ; 1-> Numero de Comentarios
     * 
     * 
     * @return List<FileDTO>
     */
    public List<FileDTO> search(Integer num, String keywordName,
            String keywordComment, String keywordTag, int hotCriterion) {
        String path = ENDPOINT_ADDRESS;

        // En la página solo se muestran files
        path += "?resource=0";
        if (num != null) {
            path += "&num=" + String.valueOf(num);
        }
        if (keywordName != null) {
            path += "&keywordName=" + keywordName;
        }
        if (keywordComment != null) {
            path += "&keywordComment=" + keywordTag;
        }
        if (keywordTag != null) {
            path += "&keywordTag" + keywordTag;
        }
        path += "&hotCriterion=" + String.valueOf(hotCriterion);

        WebResource r = getClient().resource(path);

        ClientResponse response = r.accept(MediaType.APPLICATION_JSON).get(
                ClientResponse.class);
        List<FileDTO> files = response
                .getEntity(new GenericType<List<FileDTO>>() {
                });
        return files;
    }

    /**
     * Method main.
     * 
     * @param args
     *            String[]
     */
    public static void main(String args[]) {
        RestClient rc = new RestClient();
        List<FileDTO> files = rc
                .search(Integer.valueOf(5), null, null, null, 0);
        for (FileDTO f : files) {
            System.out.println(f);
        }
    }
}
