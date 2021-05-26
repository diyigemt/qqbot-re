package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import org.qqbot.entity.JokeLibItem;
import org.qqbot.mapper.JokeMapper;
import org.qqbot.utils.MybatisUtil;

import java.util.Random;

@EventHandlerComponent
public class CommandJoke {

    @EventHandler(target = "来个乐子")
    @MessageFilter(isAt = true)
    public void Joke(MessageEventPack eventPack, PreProcessorData data){
        int res = new Random().nextInt(380) + 1;
        JokeLibItem jokeLibItem = MybatisUtil.getInstance().getSingleData(JokeMapper.class, JokeLibItem.class, "getJoke", String.valueOf(res));
        eventPack.reply(jokeLibItem.getJokeBody());
    }
}
