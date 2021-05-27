package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.utils.FileUtil;
import net.diyigemt.miraiboot.utils.SendMessageLib;
import net.diyigemt.miraiboot.utils.builder.ImageMessageBuilder;

import java.io.File;

import static org.qqbot.constant.ConstantImage.FILE_NAME_HNG;

@EventHandlerComponent
public class CommandText {
    @EventHandler(target = "text")
    public void text(MessageEventPack eventPack, PreProcessorData data){
        File resourceFile = FileUtil.getInstance().getImageResourceFile(FILE_NAME_HNG);
        String path = resourceFile.getPath();//相对路径
        String AbsPath = resourceFile.getAbsolutePath();//绝对路径

        SendMessageLib.ImageMessageSender(eventPack, path);
        eventPack.reply("分割线");
        SendMessageLib.ImageMessageSender(eventPack, AbsPath);
        eventPack.reply("分割线");
        new ImageMessageBuilder(eventPack).add(path).send();
        new ImageMessageBuilder(eventPack).add(AbsPath).send();
    }
}
