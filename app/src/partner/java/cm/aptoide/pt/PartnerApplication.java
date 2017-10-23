package cm.aptoide.pt;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Environment;
import cm.aptoide.pt.account.LoginPreferences;
import cm.aptoide.pt.crashreports.CrashReport;
import cm.aptoide.pt.database.AccessorFactory;
import cm.aptoide.pt.database.accessors.NotificationAccessor;
import cm.aptoide.pt.database.realm.Notification;
import cm.aptoide.pt.notification.NotificationCenter;
import cm.aptoide.pt.notification.NotificationIdsMapper;
import cm.aptoide.pt.notification.NotificationPolicyFactory;
import cm.aptoide.pt.notification.NotificationProvider;
import cm.aptoide.pt.notification.NotificationSyncScheduler;
import cm.aptoide.pt.notification.NotificationsCleaner;
import cm.aptoide.pt.notification.PushNotificationSyncManager;
import cm.aptoide.pt.notification.SystemNotificationShower;
import cm.aptoide.pt.notification.sync.NotificationSyncFactory;
import cm.aptoide.pt.notification.sync.NotificationSyncManager;
import cm.aptoide.pt.remotebootconfig.BootConfigJSONUtils;
import cm.aptoide.pt.remotebootconfig.datamodel.BootConfig;
import cm.aptoide.pt.remotebootconfig.datamodel.RemoteBootConfig;
import cm.aptoide.pt.view.configuration.FragmentProvider;
import cm.aptoide.pt.view.configuration.implementation.PartnerFragmentProvider;
import java.util.Calendar;
import java.util.TimeZone;
import rx.Completable;

public class PartnerApplication extends AptoideApplication {

  private BootConfig bootConfig;
  private NotificationCenter notificationCenter;
  private PushNotificationSyncManager pushNotificationSyncManager;

  public BootConfig getBootConfig() {
    if (bootConfig == null) {
      bootConfig = BootConfigJSONUtils.getSavedRemoteBootConfig(getBaseContext())
          .getData();
    }
    return bootConfig;
  }

  public void setRemoteBootConfig(RemoteBootConfig remoteBootConfig) {
    BootConfigJSONUtils.saveRemoteBootConfig(getBaseContext(), remoteBootConfig,
        "support@aptoide.com");
    this.bootConfig = remoteBootConfig.getData();
  }

  @Override public String getCachePath() {
    return Environment.getExternalStorageDirectory()
        .getAbsolutePath() + "/." + getDefaultStore() + "/";
  }

  @Override public String getDefaultStore() {
    return getBootConfig().getPartner()
        .getStore()
        .getName();
  }

  @Override public String getMarketName() {
    return getBootConfig().getPartner()
        .getStore()
        .getLabel();
  }

  @Override public LoginPreferences getLoginPreferences() {
    return new LoginPreferences(getBootConfig());
  }

  @Override public String getFeedbackEmail() {
    return getBootConfig().getPartner()
        .getFeedback()
        .getEmail();
  }

  @Override public String getImageCachePath() {
    return getCachePath() + "/" + "icons/";
  }

  @Override public String getAccountType() {
    return BuildConfig.APPLICATION_ID;
  }

  @Override public String getAutoUpdateUrl() {
    return "http://imgs.aptoide.com/latest_version_" + getDefaultStore() + ".xml";
  }

  @Override public String getPartnerId() {
    return String.valueOf(getBootConfig().getPartner()
        .getUid());
  }

  @Override public String getExtraId() {
    return String.valueOf(getBootConfig().getPartner()
        .getUid());
  }

  @Override public String getDefaultTheme() {
    return getBootConfig().getPartner()
        .getAppearance()
        .getTheme();
  }

  @Override public boolean isCreateStoreUserPrivacyEnabled() {
    return false;
  }

  @Override public FragmentProvider createFragmentProvider() {
    return new PartnerFragmentProvider(this);
  }

  @Override public Completable createShortcut() {
    if (bootConfig.getPartner()
        .getSwitches()
        .getOptions()
        .isShortcut()) {
      return super.createShortcut();
    } else {
      return null;
    }
  }


  @Override public NotificationSyncScheduler getNotificationSyncScheduler() {
    if (pushNotificationSyncManager == null) {
      pushNotificationSyncManager = new PushNotificationSyncManager(getSyncScheduler(), true,
          new NotificationSyncFactory(getDefaultSharedPreferences(), getPnpV1NotificationService(),
              getNotificationProvider()));
    }
    return pushNotificationSyncManager;
  }
}