package fi.loezi.unifud.Service;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class Http {

    public String get(final String url) throws IOException {

        final HttpResponse response = new DefaultHttpClient().execute(new HttpGet(url));

        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }
}
