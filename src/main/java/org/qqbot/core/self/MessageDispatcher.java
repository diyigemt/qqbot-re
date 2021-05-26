package org.qqbot.core.self;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.MessageSource;

import java.util.List;

@EventHandlerComponent
public class MessageDispatcher {
  @EventHandler(isAny = true)
  @MessageFilter(groups = {"827480007"})
  public void dispatcherMessage(MessageEventPack eventPack, PreProcessorData data) {
    Friend friend = eventPack.getFriend(1355247243L);
    MessageChainBuilder builder = new MessageChainBuilder();
    builder.append("来自家族群的消息:\n")
        .append(eventPack.getMessage());
    friend.sendMessage(builder.build());
    long senderId = eventPack.getSenderId();
    if (senderId == 1055651425L) {
      MessageSource messageSource = eventPack.getMessage().get(MessageSource.Key);
      Mirai.getInstance().recallMessage(eventPack.getBot(), messageSource);
    }
  }

  @EventHandler(target = "发送家长群")
  @MessageFilter(accounts = "1355247243")
  public void sendMessage(MessageEventPack eventPack, PreProcessorData data) {
    List<String> args = data.getArgs();
    if (args.size() != 1) return;
    String s = args.get(0);
    eventPack.getGroup(131312L).sendMessage(s);
  }
}
