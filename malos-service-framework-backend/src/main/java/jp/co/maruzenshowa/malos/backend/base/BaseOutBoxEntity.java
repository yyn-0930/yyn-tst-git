package jp.co.maruzenshowa.malos.backend.base;

import java.util.Map;

/**
 * BaseOutBoxEntity.java <br>
 * Copyright MARUZENSHOWA LIMITED 2024 <br>
 * <br>
 * OutBoxエンティティインターフェース <br>
 * 
 */
public interface BaseOutBoxEntity {

  /**
   * メソッド名 ： 送信済みの設定処理. <br>
   * メソッド説明 ： 送信状態の項目は送信済みを設定する <br>
   * <br>
   */
  void markSended();

  /**
   * メソッド名 ： 送信先取得処理. <br>
   * メソッド説明 ： 非同期メッセージの送信先を取得する <br>
   * <br>
   */
  String getDestination();

  /**
   * メソッド名 ： SQS判定処理. <br>
   * メソッド説明 ： SQSの場合、trueを返却する <br>
   * <br>
   */
  boolean isSqs();

  /**
   * メソッド名 ： メッセージヘッダー取得処理. <br>
   * メソッド説明 ： 非同期メッセージのヘッダーを取得する <br>
   * <br>
   */
  Map<String, Object> getMessageHeaders();
}
