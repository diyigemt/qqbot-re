package org.qqbot.core;

import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.miraiboot.annotation.EventHandler;
import org.miraiboot.annotation.EventHandlerComponent;
import org.miraiboot.entity.MessageEventPack;
import org.miraiboot.entity.PreProcessorData;
import org.miraiboot.interfaces.EventHandlerNext;
import org.miraiboot.utils.FileUtil;
import org.qqbot.entity.SaucenaoDataItem;
import org.qqbot.entity.SaucenaoHeaderItem;
import org.qqbot.entity.SaucenaoResult;
import org.qqbot.function.Saucenao;
import org.qqbot.utils.HttpUtil;

import java.io.File;
import java.util.List;

import static org.qqbot.constant.ConstantImage.FILE_NAME_HNG;
import static org.qqbot.constant.ConstantSaucenao.DBIndex;
import static org.qqbot.constant.ConstantSaucenao.bandDBIndex;

@EventHandlerComponent
public class CommandSearchImage {

	@EventHandler(target = "搜图", start = "/")
	public void searchImage(MessageEventPack eventPack, PreProcessorData data) {
		List<Image> images = eventPack.getMessageByType(Image.class);
		if (images.size() == 0) {
			eventPack.reply("图呢");
			eventPack.onNextNow(new EventHandlerNext() {
				@Override
				public ListeningStatus onNext(MessageEventPack messageEventPack, PreProcessorData preProcessorData) {
					List<Image> messageByType = messageEventPack.getMessageByType(Image.class);
					if (messageByType.size() == 0) {
						messageEventPack.reply("还是没有, 停了");
						return ListeningStatus.STOPPED;
					}
					handlerResult(messageEventPack, messageByType);
					return ListeningStatus.STOPPED;
				}
				@Override
				public void onTimeOut(MessageEventPack eventPack, PreProcessorData data) {
					eventPack.reply("超时, 停了");
				}
			}, data, 10 * 1000L, -1);
		}
		handlerResult(eventPack, images);
	}

	private void handlerResult(MessageEventPack eventPack, List<Image> images) {
		if (images.size() == 0) return;
		Image image = images.get(0);
		String imageUrl = Mirai.getInstance().queryImageUrl(eventPack.getBot(), image);
		SaucenaoResult imageResult = Saucenao.getResult(imageUrl);
		MessageChainBuilder builder = new MessageChainBuilder();
		if (imageResult.getStatus() != 0) {
			eventPack.reply(builder.append(imageResult.getMsg()).build());
			return;
		}
		SaucenaoHeaderItem header = imageResult.getHeader();
		int index_id = header.getIndex_id();
		// 获取r18设置
//		boolean r18 = SettingUtil.getInstance().getBooleanValue(ConstantSetting.SETTING_ALLOW_R18, false);
		boolean r18 = true;
		if (bandDBIndex.containsKey(index_id) && !r18) {
			File resourceFile = FileUtil.getInstance().getImageResourceFile(FILE_NAME_HNG);
			// 获取禁止r18图片
			Image hngImage = ExternalResource.Companion.uploadAsImage(resourceFile, eventPack.getSubject());
			MessageChain chain;
			if (hngImage == null) {
				// 如果没获取到 回复文字
				chain = builder.append("H是禁止的").build();
			} else {
				chain = builder.append(hngImage).build();
			}
			eventPack.reply(chain);
			return;
		}
		SaucenaoDataItem data = imageResult.getData();
		if (!(index_id == 5 || index_id == 21)) {
			data.setDefault(true);
			data.setPixiv(false);
			data.setAniDB_id(false);
		}
		boolean pixiv = data.isPixiv();
		boolean anidb = data.isAniDB();
		if(!(pixiv || anidb)){
			MessageChain chain;
			builder.append(image)
					.append("\n相似度: ")
					.append(header.getSimilarity());
			if (data.isH()) {
				builder.append("\nen_name: ")
						.append(data.getEng_name())
						.append("\njp_name: ")
						.append(data.getJp_name());
			} else {
				builder.append("\n来源: ")
						.append(DBIndex.get(index_id) == null ? "未知" : DBIndex.get(index_id))
						.append("\nurl: ")
						.append(data.getExt_urls().get(0));
			}
			chain = builder.build();
			//TODO: 这里写不写URL呢？ 如果引用不了原图的话这里肯定得给URL
			//TODO: 那就写啊23333
			eventPack.reply(chain);
			return;
		}
		//如果来源与pixiv 尝试通过pixiv.cat反代获取原图
		if (pixiv) {
			String imageSource = Saucenao.constructSourceURL(imageResult);
			if (imageSource != null) {
				image = ExternalResource.Companion.uploadAsImage(new HttpUtil().getInputStream(imageSource), eventPack.getSubject());
				builder.append("已获取原图:\n");
			}
		}
		MessageChain build = builder.append(image)
				.append("\n相似度: ")
				.append(header.getSimilarity())
				.append("\n来源: ")
				.append(pixiv ? "pixiv" : "anidb")
				.append("\n标题: ")
				.append(data.getTitle())
				.append("\n")
				.append(pixiv ? "pixiv_id: " : "anidb_id: ")
				.append(String.valueOf(pixiv ? data.getPixiv_id() : data.getAnidb_aid()))
				.append("\nurl: ")
				.append(data.getExt_urls().get(0))
				.build();
		eventPack.reply(build);
	}
}
