package fi.loezi.unifud.util;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpWorker {

    private final DefaultHttpClient client;

    public HttpWorker() {

        this.client = new DefaultHttpClient();
    }

    public String getJson(final String url) throws IOException{

        final HttpGet request = new HttpGet(url);
        final HttpResponse response = client.execute(request);

        return EntityUtils.toString(response.getEntity(), "UTF-8");
    }
}
