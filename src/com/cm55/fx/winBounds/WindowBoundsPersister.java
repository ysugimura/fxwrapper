package com.cm55.fx.winBounds;

import com.cm55.fx.*;

import javafx.geometry.*;
import javafx.stage.*;

/** 
 * ウインドウの領域状態のロードとセーブ
 * <p>
 * Stageと、その状態をロード・セーブするファイル名を指定する。指定した時に、そのファイルが存在するなら、ウインドウ状態をロードして
 * ウインドウに設定する。同時にStageの状態をトラッキングするオブジェクトを走行させる。
 * </p>
 * <p>
 * finish()時は、最後のStageの状態を先に指定されたファイルにセーブする。
 * </p>
 * @author admin
 */
public class WindowBoundsPersister<T extends WindowBounds> {
  
  public interface LoadSave<T extends WindowBounds> {
    /** WindowBoundsをロードするが、無いときはnullでもよい */
    public T load();
    
    /** WindowBoundsをセーブする */
    public void save(T value);
    public Class<T>targetClass();
  }
  
  private final Stage stage;
  private final LoadSave<T>loadSave;
  private final FxStageBoundsHolder stageBoundsHolder;
      
  /**
   * 対象とするウインドウとセーブファイルの指定。ファイルからロードし、状態をウインドウに設定する
   * @param stage
   */
  public WindowBoundsPersister(FxStage stage, LoadSave<T>loadSave) {
    this.stage = stage.getStage();
    this.loadSave = loadSave;
    WindowBounds windowBounds = loadSave.load();
    if (windowBounds != null) {
      windowBounds.setToStage(stage);    
    }
    stageBoundsHolder = new FxStageBoundsHolder(stage);
  }

  /**
   * 対象とするダイアログとセーブファイルの指定。ファイルからロードし、状態をウインドウに設定する。
   * @param dialog
   * @param clazz
   */
  public WindowBoundsPersister(FxDialog<?> dialog, LoadSave<T>loadSave) {
    this.stage = (Stage)dialog.getDialogPane().getScene().getWindow();
    this.loadSave = loadSave;
    WindowBounds windowBounds = loadSave.load();
    if (windowBounds != null) {
      windowBounds.setToDialog(dialog);    
    }
    stageBoundsHolder = new FxStageBoundsHolder(dialog);
  }
  
  /**
   * ウインドウ状態をファイルにセーブする
   */
  public void finish() {
    if (stage == null) return;
    Rectangle2D bounds = stageBoundsHolder.getLastBounds();
    T windowBounds;
    try {
      windowBounds = loadSave.targetClass().newInstance();
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    windowBounds.setMaximized(stage.isMaximized());
    windowBounds.setBounds(bounds);
    loadSave.save(windowBounds); 
  }
}