package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageSource;

@EventHandlerComponent
public class MessageRecall {
  @EventHandler(isAny = true)
  @MessageFilter(groups = {"827480007"})
  public void recallMessage(MessageEventPack eventPack, PreProcessorData data) {
    long senderId = eventPack.getSenderId();
    if (senderId == 1055651425L) {
      MessageSource messageSource = eventPack.getMessage().get(MessageSource.Key);
      Mirai.getInstance().recallMessage(eventPack.getBot(), messageSource);
    }
  }
}
