package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessagePreProcessor;
import net.diyigemt.miraiboot.constant.MessagePreProcessorMessageType;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.PlainText;
import net.mamoe.mirai.message.data.SingleMessage;
import net.mamoe.mirai.utils.ExternalResource;
import org.qqbot.function.ASCII2D;
import org.qqbot.utils.HttpUtil;

import java.io.InputStream;
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
    String resUrl = ASCII2D.getResUrl(s);
    if (resUrl == null) {
      eventPack.reply("获取结果失败");
      return;
    }
    InputStream imageStream = new HttpUtil().getInputStream(resUrl);
    if (imageStream == null) {
      eventPack.reply("获取结果图片失败");
      return;
    }
    Image image1 = ExternalResource.uploadAsImage(imageStream, eventPack.getSubject());
    eventPack.reply(new PlainText("结果图片:\n"), image1);
  }
}
