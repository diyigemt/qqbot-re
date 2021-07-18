package org.qqbot.function;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.qqbot.constant.ConstantHttp;
import org.qqbot.constant.ConstantSearchImage;
import org.qqbot.entity.ASCII2DItem;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;

public class ASCII2D {
  public static ASCII2DItem getRes(String targetUrl) {
    return getRes(targetUrl, "mutil");
  }

  public static ASCII2DItem getRes(String targetUrl, String type) {
    String page = getPage(ConstantSearchImage.ASCII2D_SEARCH_URL + type, targetUrl);
    if (page == null) return null;
    Document parse = Jsoup.parse(page);
    Elements elements = parse.getElementsByClass("item-box");
    if (elements.size() == 0) return null;
    Element element = elements.get(0);
    Elements h6 = element.getElementsByTag("h6");
    if (h6.size() == 0) return null;
    Elements a = h6.get(0).getElementsByTag("a");
    Elements img = h6.get(0).getElementsByTag("img");
    if (img.size() == 0 || a.size() != 2) return null;
    String source = img.get(0).attr("alt");
    String name = a.get(0).text();
    String url = a.get(0).attr("href");
    String author = a.get(1).text();
    ASCII2DItem res = new ASCII2DItem(source, author, name, url);
    Elements specificSearch = element.getElementsByClass("detail-link");
    if (specificSearch.size() >= 2) {
      Elements a1 = specificSearch.get(0).getElementsByTag("a");
      if (a1.size() != 0) {
        String colorSearchUrl = ConstantSearchImage.ASCII2D_BASE_URL + a1.get(0).attr("href");
        res.setColorSearchUrl(colorSearchUrl);
      }
      Elements a2 = specificSearch.get(1).getElementsByTag("a");
      if (a1.size() != 0) {
        String specificSearchUrl = ConstantSearchImage.ASCII2D_BASE_URL + a2.get(0).attr("href");
        res.setSpecificSearchUrl(specificSearchUrl);
      }
    }
    return res;
  }

  private static String getPage(String urlString, String imgString) {
    String page = null;
    InputStream inputStream = null;
    try {
      URL url = new URL(urlString);
      HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
      connection.setConnectTimeout(3000);
      connection.setRequestMethod("POST");
      connection.setDoInput(true);
      connection.setDoOutput(true);
      connection.setRequestProperty("User-agent", ConstantHttp.HEADER_USER_AGENT);
      connection.setRequestProperty("Content-Type", "multipart/form-data");
      DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
      dataOutputStream.writeBytes("uri=" + imgString);
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
    page = out.toString();
    return page;
  }
}
