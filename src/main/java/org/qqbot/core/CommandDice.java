package org.qqbot.core;

import net.diyigemt.miraiboot.annotation.EventHandler;
import net.diyigemt.miraiboot.annotation.EventHandlerComponent;
import net.diyigemt.miraiboot.annotation.MessageFilter;
import net.diyigemt.miraiboot.constant.MessageFilterMatchType;
import net.diyigemt.miraiboot.entity.MessageEventPack;
import net.diyigemt.miraiboot.entity.PreProcessorData;
import org.qqbot.entity.DiceLogItem;
import org.qqbot.entity.DiceMessageItem;
import org.qqbot.entity.DiceResultItem;
import org.qqbot.function.Dice;
import org.qqbot.mapper.DiceMapper;
import org.qqbot.utils.MybatisUtil;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理骰子指令
 * @author diyigemt HayThem
 */
@EventHandlerComponent
public class CommandDice {

	private static final Pattern dicePattern = Pattern.compile("^([0-9]+)[dD*]([0-9]+)");

	@EventHandler(isAny = true)
	@MessageFilter(value = "^([0-9]+)[dD*]([0-9]+)", matchType = MessageFilterMatchType.REGEX_FIND)
	public void dice(MessageEventPack eventPack, PreProcessorData data) {
		String command = data.getCommandText();
		Matcher matcher = dicePattern.matcher(command);
		List<String> args = data.getArgs();
		if (args == null || args.size() > 2) {
			handleErrorArgs(eventPack);
			return;
		}
		if (args.size() == 2 && args.get(1).equals("log")) {
			handleLog(eventPack);
			return;
		}
		String senderId = String.valueOf(eventPack.getSenderId());
		DiceResultItem resultItem = Dice.getRoll(args.get(0), eventPack.getSenderName(), senderId);
		List<DiceMessageItem> messageItems = MybatisUtil.getInstance().getListData(DiceMapper.class, DiceMessageItem.class, "getDiceMessage", resultItem.getResultSum());
		if (messageItems.size() == 0) {
			eventPack.reply(resultItem.toString());
			return;
		}
		if (messageItems.size() == 1) {
			eventPack.reply(resultItem.setMessage(messageItems.get(0).getMessage()).toString());
			return;
		}
		final DiceMessageItem[] message = {messageItems.get(0)};
		messageItems.forEach(item -> {
			if (senderId.equals(item.getReceiverId())) {
				message[0] = item;
			}
		});
		eventPack.reply(resultItem.setMessage(message[0].getMessage()).toString());
	}

	private void handleErrorArgs(MessageEventPack eventPack) {
		// TODO 帮助
	}

	private void handleLog(MessageEventPack eventPack) {
		List<DiceLogItem> logs = MybatisUtil.getInstance().getListData(DiceMapper.class, DiceLogItem.class, "getSenderDiceLog", String.valueOf(eventPack.getSenderId()));
		if (logs == null) {
			eventPack.reply("无");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (DiceLogItem item : logs) {
			sb.append(item.toString())
					.append("\n");
		}
		eventPack.reply(sb.toString());
	}
}
