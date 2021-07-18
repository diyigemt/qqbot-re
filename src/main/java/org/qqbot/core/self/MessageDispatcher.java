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
  @MessageFilter(groups = {"630395874"})
  public void dispatcherMessageSchool(MessageEventPack eventPack, PreProcessorData data) {
    MessageChainBuilder builder = new MessageChainBuilder();
    builder.append("来自家长群的消息:\n")
        .append(eventPack.getMessage());
    eventPack.sendFriendMessage(1355247243L, builder.build());
  }

  @EventHandler(isAny = true)
  public void dispatcherMessage(MessageEventPack eventPack, PreProcessorData data) {
    if (eventPack.isGroup()) return;
    MessageChainBuilder builder = new MessageChainBuilder();
    builder.append("来自")
        .append(eventPack.getSenderNick())
        .append("(")
        .append(String.valueOf(eventPack.getSenderId()))
        .append(")的消息:\n")
        .append(eventPack.getMessage());
    eventPack.sendFriendMessage(1355247243L, builder.build());
  }

  @EventHandler(target = "发送家长群")
  @MessageFilter(accounts = "1355247243")
  public void sendMessageToSchool(MessageEventPack eventPack, PreProcessorData data) {
    List<String> args = data.getArgs();
    if (args.size() != 1) return;
    String s = args.get(0);
    eventPack.sendGroupMessage(630395874L, s);
  }

  @EventHandler(target = "发送好友消息")
  @MessageFilter(accounts = "1355247243")
  public void sendMessage(MessageEventPack eventPack, PreProcessorData data) {
    List<String> args = data.getArgs();
    if (args.size() != 2) return;
    String target = args.get(0);
    String s = args.get(1);
    eventPack.sendFriendMessage(Long.parseLong(target), s);
  }
}
