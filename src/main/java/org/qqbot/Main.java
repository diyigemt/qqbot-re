package org.qqbot;

import org.miraiboot.annotation.MiraiBootApplication;
import org.miraiboot.autoconfig.MiraiApplication;

@MiraiBootApplication
public class Main {
  public static void main(String[] args) {
    MiraiApplication.run(Main.class, args);
  }
}
