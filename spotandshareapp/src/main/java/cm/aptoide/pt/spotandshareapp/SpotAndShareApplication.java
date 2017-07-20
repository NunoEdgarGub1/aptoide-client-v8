package cm.aptoide.pt.spotandshareapp;

import android.content.Context;
import cm.aptoide.pt.preferences.AptoidePreferencesConfiguration;
import cm.aptoide.pt.spotandshare.socket.entities.Friend;
import cm.aptoide.pt.v8engine.V8Engine;

/**
 * Created by filipe on 27-06-2017.
 */

public class SpotAndShareApplication extends V8Engine {

  private cm.aptoide.pt.spotandshareandroid.SpotAndShare spotAndShare;
  public SpotAndShareUserManager spotAndShareUserManager;

  @Override public void onCreate() {
    setupCrashReports(BuildConfig.CRASH_REPORTS_DISABLED);
    super.onCreate();
  }

  @Override public AptoidePreferencesConfiguration createConfiguration() {
    return new VanillaConfiguration(getDefaultSharedPreferences());
  }

  public SpotAndShareUserManager getSpotAndShareUserManager() {
    if (spotAndShareUserManager == null) {
      spotAndShareUserManager = new SpotAndShareUserManager(new SpotAndShareUserPersister(
          getSharedPreferences(SpotAndShareUserPersister.SHARED_PREFERENCES_NAME,
              Context.MODE_PRIVATE)));
    }
    return spotAndShareUserManager;
  }

  public cm.aptoide.pt.spotandshareandroid.SpotAndShare getSpotAndShare() {
    if (spotAndShare == null) {
      Friend friend = new Friend(getSpotAndShareUserManager().getUser()
          .getUsername());
      spotAndShare = new cm.aptoide.pt.spotandshareandroid.SpotAndShare(this, friend);
    }
    return spotAndShare;
  }
}