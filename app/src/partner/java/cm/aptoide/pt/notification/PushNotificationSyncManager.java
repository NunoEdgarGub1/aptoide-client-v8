package cm.aptoide.pt.notification;

import cm.aptoide.pt.notification.sync.NotificationSyncFactory;
import cm.aptoide.pt.sync.SyncScheduler;

/**
 * Created by danielchen on 19/10/2017.
 */

public class PushNotificationSyncManager implements NotificationSyncScheduler{

  private final SyncScheduler syncScheduler;
  private final NotificationSyncFactory notificationSyncFactory;
  private boolean enabled;

  public PushNotificationSyncManager(SyncScheduler syncScheduler, boolean enabled,
      NotificationSyncFactory notificationSyncFactory) {
    this.syncScheduler = syncScheduler;
    this.enabled = enabled;
    this.notificationSyncFactory = notificationSyncFactory;
  }

  @Override public void schedule() {
    if (enabled) {
      syncScheduler.schedule(notificationSyncFactory.create(
          NotificationSyncFactory.PUSH_NOTIFICATION_SYNC_PERIODIC));
    }
  }

  @Override public void forceSync() {
    if (enabled) {
      syncScheduler.schedule(notificationSyncFactory.create(
          NotificationSyncFactory.PUSH_NOTIFICATION_SYNC_IMMEDIATE));
    }
  }

  @Override public void removeSchedules() {
    syncScheduler.cancel(NotificationSyncFactory.PUSH_NOTIFICATION_SYNC_PERIODIC);
  }

  @Override public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }
}