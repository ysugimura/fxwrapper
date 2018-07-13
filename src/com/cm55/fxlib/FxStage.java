package com.cm55.fxlib;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.*;
import javafx.stage.*;

public class FxStage {

  private Stage stage;
  
  private SimpleBooleanProperty showingProperty = new SimpleBooleanProperty();
  public SimpleBooleanProperty showingProperty() { return showingProperty; }

  private boolean hidingEvent;
  private FxCallback<FxStage>onShowingCallback;
  private FxCallback<FxStage>onHidingCallback;
  private FxCallback<Boolean>onFocusChanged;
  
  public FxStage() {
 
    stage = new Stage();
    
    // 表示直前のコールバック
    stage.setOnShowing(e-> {
      if (onShowingCallback != null) onShowingCallback.callback(this);
    });

    // 消去直前のコールバック
    stage.setOnHiding(e-> {
      if (onHidingCallback != null) onHidingCallback.callback(this);
      hidingEvent = true;
      try {
        showingProperty.set(false);
      } finally {
        hidingEvent = false;
      }
    });

    // shoingPropertyのハンドリング
    showingProperty.addListener((ChangeListener<Boolean>)(a, o, n)-> {
      if (n) {
        stage.show(); 
      } else {
        if (hidingEvent) return;
        stage.hide();
      }
    });
    
    // フォーカス変更のハンドリング
    this.stage.focusedProperty().addListener((ChangeListener<Boolean>)(ob, o, n)-> {
      if (onFocusChanged != null) onFocusChanged.callback(n);
    });
  }

  /** 表示直前に呼ばれるコールバックを登録 */
  public FxStage setOnShowing(FxCallback<FxStage>callback) {
    this.onShowingCallback = callback;
    return this;
  }

  /** 消去直前に呼ばれるコールバックを登録 */
  public FxStage setOnHiding(FxCallback<FxStage>callback) {
    this.onHidingCallback = callback;
    return this;
  }

  /** フォーカス変更時に呼ばれるコールバックを登録 */
  public FxStage setOnFocusChanged(FxCallback<Boolean>callback) {
    this.onFocusChanged = callback;
    return this;
  }
  
  /** シーンを設定*/
  public FxStage setScene(Scene scene) {
    stage.setScene(scene);
    return this;
  }

  /** タイトルを設定 */
  public FxStage setTitle(String title) {
    stage.setTitle(title);
    return this;
  }
  
  /** オーナーを設定 */
  public FxStage initOwner(Window window) {
    stage.initOwner(window);
    return this;
  }

  public void show() {
    stage.show();
  }
  
  public void showAndWait() {
    stage.showAndWait();
  }
  
  /** Stageを取得 */
  public Stage getStage() {
    return stage;
  }
}
