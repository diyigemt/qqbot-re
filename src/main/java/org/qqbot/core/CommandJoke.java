package org.qqbot.core;

import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.qqbot.entity.JokeLibItem;
import org.qqbot.mapper.JokeMapper;
import org.qqbot.utils.MybatisUtil;

import java.util.Random;

@EventHandlerComponent
public class CommandJoke {

    @EventHandler(target = "来个乐子")
    public void Joke(MessageEventPack eventPack, PreProcessorData data){
        int res = new Random().nextInt(380) + 1;
        JokeLibItem jokeLibItem = MybatisUtil.getInstance().getSingleData(JokeMapper.class, JokeLibItem.class, "getJoke", String.valueOf(res));
        eventPack.reply(jokeLibItem.getJokeBody());
    }
}
