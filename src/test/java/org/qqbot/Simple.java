package org.qqbot;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;
import org.qqbot.utils.HttpUtil;

import javax.net.ssl.HttpsURLConnection;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class Simple {

  @Test
  public void testASCII() throws IOException {
    String baseString = "https://ascii2d.net";
    String uri = "https://lychee.diyigemt.net/uploads/big/fa247c3e0fab408fda17bcaddd758b56.png";
    String ascii = "https://ascii2d.net/search/uri";
    URL url = new URL(ascii);
    HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
    connection.setConnectTimeout(3000);
    connection.setRequestMethod("POST");
    connection.setRequestProperty("uri", uri);
    connection.connect();
    InputStream inputStream = connection.getInputStream();
    String res = null;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      byte buf[] = new byte[1024];
      int read = 0;
      while ((read = inputStream.read(buf)) > 0) {
        out.write(buf, 0, read);
      }
    } finally {
      if (inputStream != null) {
        inputStream.close();
      }
    }
    res = out.toString();
    Document parse = Jsoup.parse(res);
    Elements elements = parse.getElementsByClass("item-box");
    if (elements.size() == 0) return;
    Element element = elements.get(0);
    Elements select = element.select(".image-box > img");
    if (select.size() == 0) return;
    Element image = select.get(0);
    String src = image.attr("src");
    String imageSrc = baseString + src;

  }
}
