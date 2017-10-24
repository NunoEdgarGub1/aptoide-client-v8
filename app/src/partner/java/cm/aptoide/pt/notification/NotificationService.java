package cm.aptoide.pt.notification;

import java.util.List;
import rx.Single;

/**
 * Created by danielchen on 23/10/2017.
 */

public interface NotificationService {

  Single<List<AptoideNotification>> getSocialNotifications();

  Single<List<AptoideNotification>> getCampaignNotifications();

  Single<List<AptoideNotification>> getPushNotifications();
}
