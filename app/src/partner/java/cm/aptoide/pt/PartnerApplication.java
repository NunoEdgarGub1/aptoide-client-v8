package cm.aptoide.pt;

import android.os.Environment;
import cm.aptoide.pt.account.LoginPreferences;
import cm.aptoide.pt.dataprovider.WebService;
import cm.aptoide.pt.notification.NotificationCenter;
import cm.aptoide.pt.notification.NotificationService;
import cm.aptoide.pt.notification.NotificationSyncScheduler;
import cm.aptoide.pt.notification.PnpV1NotificationService;
import cm.aptoide.pt.notification.PushNotificationSyncManager;
import cm.aptoide.pt.notification.sync.NotificationSyncFactory;
import cm.aptoide.pt.remotebootconfig.BootConfigJSONUtils;
import cm.aptoide.pt.remotebootconfig.datamodel.BootConfig;
import cm.aptoide.pt.remotebootconfig.datamodel.RemoteBootConfig;
import cm.aptoide.pt.view.configuration.FragmentProvider;
import cm.aptoide.pt.view.configuration.implementation.PartnerFragmentProvider;
import rx.Completable;
import rx.Single;

public class PartnerApplication extends AptoideApplication {

  private BootConfig bootConfig;
  private NotificationCenter notificationCenter;
  private PushNotificationSyncManager pushNotificationSyncManager;
  private NotificationService pnpV1NotificationService;

  public BootConfig getBootConfig() {
    return getApplicationPreferences().getBootConfig();
  }

  public void setRemoteBootConfig(RemoteBootConfig remoteBootConfig) {
    BootConfigJSONUtils.saveRemoteBootConfig(getBaseContext(), remoteBootConfig,
        "support@aptoide.com");
    getApplicationPreferences().setBootConfig(remoteBootConfig.getData());
  }

  @Override public Completable createShortcut() {
    return Single.just(getApplicationPreferences())
        .map(PartnerApplicationPreferences::getBootConfig)
        .flatMapCompletable(bootConfig -> {
          if (bootConfig.getPartner()
              .getSwitches()
              .getOptions()
              .isShortcut()) {
            return super.createShortcut();
          } else {
            return Completable.complete();
          }
        });
  }

  @Override public LoginPreferences getLoginPreferences() {
    return new LoginPreferences(getBootConfig());
  }

  @Override public PartnerApplicationPreferences getApplicationPreferences() {
    final BootConfig bootConfig = BootConfigJSONUtils.getSavedRemoteBootConfig(getBaseContext())
        .getData();
    return new PartnerApplicationPreferences(bootConfig);
  }

  @Override public FragmentProvider createFragmentProvider() {
    final ApplicationPreferences appPreferences = getApplicationPreferences();
    return new PartnerFragmentProvider(appPreferences.getDefaultThemeName(),
        appPreferences.getDefaultStoreName(), appPreferences.hasMultiStoreSearch());
  }


  @Override public NotificationSyncScheduler getNotificationSyncScheduler() {
    if (pushNotificationSyncManager == null) {
      pushNotificationSyncManager = new PushNotificationSyncManager(getSyncScheduler(), true,
          new NotificationSyncFactory(getDefaultSharedPreferences(), getPnpV1NotificationService(),
              getNotificationProvider()));
    }
    return pushNotificationSyncManager;
  }

  @Override public NotificationService getPnpV1NotificationService() {
    if (pnpV1NotificationService == null) {
      final ApplicationPreferences appPreferences = getApplicationPreferences();
      pnpV1NotificationService =
          new PnpV1NotificationService(BuildConfig.APPLICATION_ID, getDefaultClient(),
              WebService.getDefaultConverter(), getIdsRepository(), BuildConfig.VERSION_NAME,
              appPreferences.getExtraId(), getDefaultSharedPreferences(), getResources(),
              getAuthenticationPersistence(), getAccountManager(), getBaseContext(),
              getTokenInvalidator(), getBodyInterceptorV3());
    }
    return pnpV1NotificationService;
  }
}