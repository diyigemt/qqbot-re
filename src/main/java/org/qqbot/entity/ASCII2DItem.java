package org.qqbot.entity;

import lombok.Data;

@Data
public class ASCII2DItem {
  private final String source;
  private final String author;
  private final String name;
  private final String url;
  private String colorSearchUrl;
  private String specificSearchUrl;
}
