package org.qqbot.function;

import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.MessageEventPack;

@EventHandlerComponent
public class Test {

  @EventHandler(isAny = true)
  public void test(MessageEventPack eventPack) {
    eventPack.replyNotAt(eventPack.getMessage());
  }
}
