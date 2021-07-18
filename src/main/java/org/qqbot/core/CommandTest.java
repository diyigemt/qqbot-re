package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessagePreProcessor;
import net.diyigemt.miraiboot.constant.MessagePreProcessorMessageType;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.qqbot.entity.ASCII2DItem;
import org.qqbot.function.ASCII2D;

import java.util.List;

@EventHandlerComponent
public class CommandTest {

  @EventHandler(target = "ascii")
  @MessagePreProcessor(filterType = MessagePreProcessorMessageType.Image)
  public void ascii(MessageEventPack eventPack, PreProcessorData<Object> data) {
    List<Image> images = eventPack.getMessageByType(Image.class);
    if (images.size() == 0) {
      eventPack.reply("没图啊, 是不是被夹了");
      return;
    }
    Image image = images.get(0);
    String s = Image.queryUrl(image);
    ASCII2DItem res = ASCII2D.getRes(s);
    if (res == null) {
      eventPack.reply("获取结果失败");
      return;
    }
    MessageChainBuilder builder = new MessageChainBuilder();
    MessageChain build;
    build = builder.append(image)
        .append("\n")
        .append("来源:")
        .append(res.getSource())
        .append("\n")
        .append("标题:")
        .append(res.getName())
        .append("\n")
        .append("作者:")
        .append(res.getAuthor())
        .append("\n")
        .append("url:")
        .append(res.getUrl())
        .build();
    eventPack.reply(build);
//    InputStream imageStream = new HttpUtil().getInputStream(res.getUrl());
//    if (imageStream == null) {
//      eventPack.reply("获取结果图片失败");
//      return;
//    }
//    Image image1 = ExternalResource.uploadAsImage(imageStream, eventPack.getSubject());
//    eventPack.reply(new PlainText("结果图片:\n"), image1);
//  }
  }
}
