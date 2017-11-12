package view;

import logic.Controller;
import org.junit.Test;
import util.FileParserStub;
import util.ModelStub;

import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class HttpUITest {
    @Test
    public void connectionTest() throws Exception {
        Controller controller = new Controller(new ModelStub(), new FileParserStub());
        HttpUI ui = new HttpUI(controller);
        ui.start();

        String strUrl = "http://localhost:8000/abc";

        URL url = new URL(strUrl);
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
        urlConn.connect();

        assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());

        strUrl += "?foo=bar";
        url = new URL(strUrl);
        urlConn = (HttpURLConnection) url.openConnection();
        urlConn.connect();

        assertEquals(HttpURLConnection.HTTP_OK, urlConn.getResponseCode());
    }
}
