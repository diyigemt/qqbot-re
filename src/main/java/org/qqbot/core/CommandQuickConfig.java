package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.permission.CheckPermission;
import net.diyigemt.miraiboot.utils.GlobalConfig;

import java.util.List;

@EventHandlerComponent
public class CommandQuickConfig {

  @EventHandler(target = "config")
  @CheckPermission(isAdminOnly = true)
  public void quickConfig(MessageEventPack eventPack, PreProcessorData data) {
    List<String> args = data.getArgs();
    if (args.isEmpty() || args.size() > 2) {
      eventPack.reply("参数不足或有多余参数");
    }
    String command = args.get(0);
    String target = args.get(1);
    String res;
    switch (command) {
      case "enable":
        res = "true";
        break;
      case "disable":
        res = "false";
        break;
      default:
        eventPack.reply("指令有误或不存在");
        return;
    }
    GlobalConfig.getInstance().put(target, res);
    eventPack.reply("将 ", target, " 设置为:", res);
  }
}
