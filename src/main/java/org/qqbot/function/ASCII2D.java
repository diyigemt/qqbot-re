package org.qqbot.function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qqbot.constant.ConstantHttp;
import org.qqbot.constant.ConstantSearchImage;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ASCII2D {
  public static String getResUrl(String targetUrl) {
    String res = null;
    InputStream inputStream = null;
    try {
      URL url = new URL(ConstantSearchImage.ASCII2D_SEARCH_URL);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setConnectTimeout(3000);
      connection.setRequestMethod("POST");
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestProperty("User-agent", ConstantHttp.HEADER_USER_AGENT);
      connection.setRequestProperty("Content-Type", "multipart/form-data");
      DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
      dataOutputStream.writeBytes("uri=" + targetUrl);
      inputStream = connection.getInputStream();
    } catch (IOException e) {

    }
    if (inputStream == null) return null;
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      byte buf[] = new byte[1024];
      int read = 0;
      while ((read = inputStream.read(buf)) > 0) {
        out.write(buf, 0, read);
      }
    } catch (IOException e) {

    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {

        }
      }
    }
    res = out.toString();
    Document parse = Jsoup.parse(res);
    Elements elements = parse.getElementsByClass("item-box");
    if (elements.size() == 0) return null;
    Element element = elements.get(0);
    Elements select = element.select(".image-box > img");
    if (select.size() == 0) return null;
    Element image = select.get(0);
    String src = image.attr("src");
    String imageSrc = ConstantSearchImage.ASCII2D_BASE_URL + src;
    return imageSrc;
  }
}
