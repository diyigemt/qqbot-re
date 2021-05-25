package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.entity.HttpProperties;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import net.diyigemt.miraiboot.utils.SendMessageLib;
import org.qqbot.constant.ConstantHttp;
import org.qqbot.constant.ConstantVoice;
import org.qqbot.entity.VoiceInfoItem;
import org.qqbot.mapper.MeaVoiceMapper;
import org.qqbot.utils.MybatisUtil;

import java.util.Random;

@EventHandlerComponent
public class CommandMeaVoice{

	@EventHandler(target = "咩")
	public void MeaVoice(MessageEventPack eventPack, PreProcessorData data){
		if(!eventPack.isGroup()){
			eventPack.reply("当前功能仅限群组使用");
		}

		int res = new Random().nextInt(ConstantVoice.MAX_MEA_BUTTON_COUNT) + 1;
		VoiceInfoItem voiceInfoItem = MybatisUtil.getInstance().getSingleData(MeaVoiceMapper.class, VoiceInfoItem.class, "getVoiceInfo", String.valueOf(res));
		if(voiceInfoItem == null){
			eventPack.reply("获取资源数据失败");
			return;
		}

		HttpProperties properties = new HttpProperties();
		properties.setRequestProperties("Host", ConstantHttp.HEADER_HOST);
		properties.setRequestProperties("referer", ConstantHttp.HEADER_REFERER);
		properties.setRequestProperties("Cookie", ConstantHttp.HEADER_COOKIE);

		SendMessageLib.VoiceMsgSender(eventPack, voiceInfoItem.getUrl(), properties);
	}
}
