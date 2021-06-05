package org.qqbot;

import net.diyigemt.miraiboot.annotation.MiraiBootApplication;
import net.diyigemt.miraiboot.autoconfig.MiraiApplication;
import net.diyigemt.miraiboot.autoconfig.MiraiBootPlugin;

@MiraiBootApplication
public class Main extends MiraiBootPlugin {
  public static void main(String[] args) {
    MiraiApplication.run(Main.class, args);
  }
}
