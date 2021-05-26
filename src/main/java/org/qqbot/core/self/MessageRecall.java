package org.qqbot.core.self;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.message.data.MessageSource;

@EventHandlerComponent
public class MessageRecall {
  @EventHandler(isAny = true)
  @MessageFilter(groups = {"827480007", "1002484182"})
  public void recallMessage(MessageEventPack eventPack, PreProcessorData data) {
    long senderId = eventPack.getSenderId();
    if (senderId == 1055651425L || senderId == 1328343252L) {
      MessageSource messageSource = eventPack.getMessage().get(MessageSource.Key);
      Mirai.getInstance().recallMessage(eventPack.getBot(), messageSource);
    }
  }
}
