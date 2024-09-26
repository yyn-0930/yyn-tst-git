package jp.co.maruzenshowa.malos.backend.base;

import io.awspring.cloud.sns.core.SnsTemplate;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * AbstractOutBoxScheduleService.java <br>
 * Copyright MARUZENSHOWA LIMITED 2024 <br>
 * <br>
 * OutBoxスケジュール送信基底サービス <br>
 * OutBoxスケジュール送信(SQS、SNS)機能を提供する. <br>
 * 
 * @param <T> OutBoxのエンティティクラス. <br>
 */
public abstract class AbstractOutBoxScheduleService<T extends BaseOutBoxEntity> {

  @Autowired
  private SqsTemplate sqsTemplate;

  @Autowired
  private SnsTemplate snsTemplate;

  protected abstract BaseOutBoxRepository<T> getRepository();

  /**
   * メソッド名 ： OutBox送信処理. <br>
   * メソッド説明 ： OutBoxの定期送信(SQS、SNS)を行う <br>
   * <br>
   */
  @Scheduled(fixedRateString = "${app.outbox.fixedRate}")
  public void outBoxSend() {
    List<T> outBoxMessageList = getRepository().findAllMsgToSend();
    List<Message<T>> messageList = new ArrayList<>();
    String targetQueue = "";
    for (T messageEntity : outBoxMessageList) {
      if (StringUtils.EMPTY.equals(targetQueue)) {
        targetQueue = messageEntity.getDestination();
      }
      if (messageEntity.isSqs()) {
        if (targetQueue.equals(messageEntity.getDestination())) {
          Message<T> message =
              MessageBuilder.withPayload(messageEntity).copyHeaders(messageEntity.getMessageHeaders()).build();
          messageList.add(message);
        } else {
          sqsTemplate.sendMany(targetQueue, messageList);
          messageList.clear();
        }
      } else {
        snsTemplate.convertAndSend(messageEntity.getDestination(), messageEntity, messageEntity.getMessageHeaders());
      }
      messageEntity.markSended();
      getRepository().save(messageEntity);
    }
    if (!messageList.isEmpty()) {
      sqsTemplate.sendMany(targetQueue, messageList);
    }
  }
}
