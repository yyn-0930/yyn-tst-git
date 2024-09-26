package jp.co.maruzenshowa.malos.backend.base;

import java.util.List;

/**
 * BaseOutBoxRepository.java <br>
 * Copyright MARUZENSHOWA LIMITED 2024 <br>
 * <br>
 * OutBox基底リポジトリ <br>
 * OutBox利用する機能を定義する. <br>
 * 
 * @param <T> OutBoxのエンティティクラス. <br>
 */
public interface BaseOutBoxRepository<T extends BaseOutBoxEntity> {
  
  /**
   * メソッド名 ： 送信メッセージ取得処理. <br>
   * メソッド説明 ： 送信メッセージを取得する <br>
   * <br>
   */
  List<T> findAllMsgToSend();

  /**
   * メソッド名 ： 送信済み更新処理. <br>
   * メソッド説明 ： 送信状態は送信済みに更新する <br>
   * <br>
   */
  T save(T baseOutBoxEntity);
}
