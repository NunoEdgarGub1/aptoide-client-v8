package cm.aptoide.pt.notification;

import android.support.annotation.NonNull;
import cm.aptoide.pt.database.accessors.NotificationAccessor;
import cm.aptoide.pt.database.realm.Notification;
import io.realm.Sort;
import java.util.List;
import rx.Completable;
import rx.Observable;
import rx.Scheduler;
import rx.Single;

/**
 * Created by trinkes on 09/05/2017.
 */

public class NotificationProvider {

  private final NotificationAccessor notificationAccessor;
  private final Scheduler scheduler;

  public NotificationProvider(NotificationAccessor notificationAccessor, Scheduler scheduler) {
    this.scheduler = scheduler;
    this.notificationAccessor = notificationAccessor;
  }

  @NonNull private Notification convertToNotification(AptoideNotification aptoideNotification) {

    return new Notification(aptoideNotification.getExpire(),
        aptoideNotification.getAbTestingGroup(), aptoideNotification.getBody(),
        aptoideNotification.getCampaignId(), aptoideNotification.getImg(),
        aptoideNotification.getLang(), aptoideNotification.getTitle(), aptoideNotification.getUrl(),
        aptoideNotification.getUrlTrack(), aptoideNotification.getNotificationCenterUrlTrack(),
        aptoideNotification.getTimeStamp(), aptoideNotification.getType(),
        aptoideNotification.getDismissed(), aptoideNotification.getAppName(),
        aptoideNotification.getGraphic(), aptoideNotification.getOwnerId(),
        aptoideNotification.isProcessed());
  }

  public Single<List<AptoideNotification>> getDismissedNotifications(
      @AptoideNotification.NotificationType Integer[] notificationsTypes, long startTime,
      long endTime) {
    return notificationAccessor.getDismissed(notificationsTypes, startTime, endTime)
        .first()
        .flatMap(notifications -> Observable.from(notifications)
            .map(notification -> convertToAptoideNotification(notification))
            .toList())
        .toSingle();
  }

  private AptoideNotification convertToAptoideNotification(Notification notification) {
    return new AptoideNotification(notification.getAbTestingGroup(), notification.getBody(),
        notification.getCampaignId(), notification.getImg(), notification.getLang(),
        notification.getTitle(), notification.getUrl(), notification.getUrlTrack(),
        notification.getTimeStamp(), notification.getType(), notification.getDismissed(),
        notification.getAppName(), notification.getGraphic(), notification.getOwnerId(),
        notification.getExpire(), notification.getNotificationCenterUrlTrack(),
        notification.isProcessed());
  }

  public Completable save(List<AptoideNotification> aptideNotifications) {
    return Observable.from(aptideNotifications)
        .map(aptoideNotification -> convertToNotification(aptoideNotification))
        .toList()
        .doOnNext(notifications -> notificationAccessor.insertAll(notifications))
        .toCompletable();
  }

  public Observable<List<AptoideNotification>> getNotifications(int entries) {
    return notificationAccessor.getAllSorted(Sort.DESCENDING)
        .flatMap(notifications -> Observable.from(notifications)
            .map(notification -> convertToAptoideNotification(notification))
            .take(entries)
            .toList());
  }

  public Observable<List<Notification>> getNotifications() {
    return notificationAccessor.getAll();
  }

  public Observable<List<AptoideNotification>> getAptoideNotifications() {
    return notificationAccessor.getAll()
        .flatMap(notifications -> Observable.from(notifications)
            .map(notification -> convertToAptoideNotification(notification))
            .toList());
  }

  public Single<Notification> getLastShowed(Integer[] notificationType) {
    return notificationAccessor.getLastShowed(notificationType);
  }

  public Completable save(Notification notification) {
    return Completable.fromAction(() -> {
      notificationAccessor.insert(notification);
    })
        .subscribeOn(scheduler);
  }

  public Observable<List<AptoideNotification>> getUnreadNotifications() {
    return notificationAccessor.getUnread()
        .flatMap(notifications -> Observable.from(notifications)
            .map(notification -> convertToAptoideNotification(notification))
            .toList());
  }

  public Completable save(AptoideNotification notification) {
    return save(convertToNotification(notification));
  }
}