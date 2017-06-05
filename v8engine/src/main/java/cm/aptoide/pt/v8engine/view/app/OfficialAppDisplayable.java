package cm.aptoide.pt.v8engine.view.app;

import android.util.Pair;
import cm.aptoide.pt.model.v7.GetAppMeta;
import cm.aptoide.pt.v8engine.R;
import cm.aptoide.pt.v8engine.view.recycler.displayable.Displayable;

public class OfficialAppDisplayable extends Displayable {

  private final Pair<String, GetAppMeta> messageGetApp;

  public OfficialAppDisplayable() {
    messageGetApp = null;
  }

  public OfficialAppDisplayable(Pair<String, GetAppMeta> messageGetApp) {
    this.messageGetApp = messageGetApp;
  }

  public Pair<String, GetAppMeta> getMessageGetApp() {
    return messageGetApp;
  }

  @Override protected Configs getConfig() {
    return new Configs(1, true);
  }

  @Override public int getViewLayout() {
    return R.layout.official_app_displayable_layout;
  }
}