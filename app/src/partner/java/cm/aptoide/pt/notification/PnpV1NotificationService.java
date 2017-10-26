package cm.aptoide.pt.notification;

/**
 * Created by danielchen on 23/10/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;
import android.widget.RemoteViews;
import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.pt.AptoideApplication;
import cm.aptoide.pt.PartnerApplication;
import cm.aptoide.pt.R;
import cm.aptoide.pt.dataprovider.interfaces.TokenInvalidator;
import cm.aptoide.pt.dataprovider.model.v1.GetPullNotificationsResponse;
import cm.aptoide.pt.dataprovider.ws.BodyInterceptor;
import cm.aptoide.pt.dataprovider.ws.v1.notification.PullCampaignNotificationsRequest;
import cm.aptoide.pt.dataprovider.ws.v1.notification.PullSocialNotificationRequest;
import cm.aptoide.pt.dataprovider.ws.v3.BaseBody;
import cm.aptoide.pt.dataprovider.ws.v3.GetPushNotificationsResponse;
import cm.aptoide.pt.dataprovider.ws.v3.PushNotificationsRequest;
import cm.aptoide.pt.networking.AuthenticationPersistence;
import cm.aptoide.pt.networking.BodyInterceptorV3;
import cm.aptoide.pt.networking.IdsRepository;
import cm.aptoide.pt.preferences.managed.ManagerPreferences;
import java.util.LinkedList;
import java.util.List;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import rx.Single;

public class PnpV1NotificationService implements NotificationService {
  private final String applicationId;
  private final OkHttpClient httpClient;
  private final Converter.Factory converterFactory;
  private final IdsRepository idsRepository;
  private final String versionName;
  private final AuthenticationPersistence authenticationPersistence;
  private final AptoideAccountManager accountManager;
  private final String extraId;
  private final SharedPreferences sharedPreferences;
  private final Resources resources;
  private Context context;
  private TokenInvalidator tokenInvalidator;
  private BodyInterceptor<BaseBody> bodyInterceptor;

  public PnpV1NotificationService(String applicationId, OkHttpClient httpClient,
      Converter.Factory converterFactory, IdsRepository idsRepository, String versionName,
      String extraId, SharedPreferences sharedPreferences, Resources resources,
      AuthenticationPersistence authenticationPersistence, AptoideAccountManager accountManager,
      Context context, TokenInvalidator tokenInvalidator, BodyInterceptor<BaseBody> bodyInterceptor) {
    this.applicationId = applicationId;
    this.httpClient = httpClient;
    this.converterFactory = converterFactory;
    this.idsRepository = idsRepository;
    this.versionName = versionName;
    this.authenticationPersistence = authenticationPersistence;
    this.extraId = extraId;
    this.sharedPreferences = sharedPreferences;
    this.resources = resources;
    this.accountManager = accountManager;
    this.context = context;
    this.tokenInvalidator = tokenInvalidator;
    this.bodyInterceptor = bodyInterceptor;
  }

  public PnpV1NotificationService(String applicationId, OkHttpClient httpClient,
      Converter.Factory converterFactory, IdsRepository idsRepository, String versionName,
      String extraId, SharedPreferences sharedPreferences, Resources resources,
      AuthenticationPersistence authenticationPersistence, AptoideAccountManager accountManager) {
    this.applicationId = applicationId;
    this.httpClient = httpClient;
    this.converterFactory = converterFactory;
    this.idsRepository = idsRepository;
    this.versionName = versionName;
    this.authenticationPersistence = authenticationPersistence;
    this.extraId = extraId;
    this.sharedPreferences = sharedPreferences;
    this.resources = resources;
    this.accountManager = accountManager;
  }

  @Override public Single<List<AptoideNotification>> getSocialNotifications() {
    return authenticationPersistence.getAuthentication()
        .flatMapObservable(
            authentication -> PullSocialNotificationRequest.of(idsRepository.getUniqueIdentifier(),
                versionName, applicationId, httpClient, converterFactory, extraId,
                authentication.getAccessToken(), sharedPreferences, resources)
                .observe())
        .flatMap(response -> accountManager.accountStatus()
            .first()
            .map(account -> convertSocialNotifications(response, account.getId())))
        .toSingle();
  }

  @Override public Single<List<AptoideNotification>> getCampaignNotifications() {
    return PullCampaignNotificationsRequest.of(idsRepository.getUniqueIdentifier(), versionName,
        applicationId, httpClient, converterFactory, extraId, sharedPreferences, resources)
        .observe()
        .flatMap(response -> accountManager.accountStatus()
            .first()
            .map(account -> convertCampaignNotifications(response, account.getId())))
        .first()
        .toSingle();
  }

  @Override public Single<List<AptoideNotification>> getPushNotifications() {
    return PushNotificationsRequest.of(sharedPreferences, resources,
        context, extraId, bodyInterceptor, tokenInvalidator)
        .observe().map(response -> convertPushNotifications(response)).toSingle();
  }

  private List<AptoideNotification> convertPushNotifications(
      GetPushNotificationsResponse response) {
    List<AptoideNotification> aptoideNotifications = new LinkedList<>();
    for (final GetPushNotificationsResponse.Notification pushNotification : response.getResults()) {
      ManagerPreferences.setLastPushNotificationId(pushNotification.getId().intValue(),
          sharedPreferences);
      aptoideNotifications.add(new AptoideNotification(pushNotification.getImages().getIconUrl(),
          pushNotification.getTitle(), pushNotification.getTargetUrl(),
          pushNotification.getTrackUrl(), pushNotification.getImages().getBannerUrl()));
    }
    return aptoideNotifications;
  }

  private List<AptoideNotification> convertSocialNotifications(
      List<GetPullNotificationsResponse> response, String id) {
    List<AptoideNotification> aptoideNotifications = new LinkedList<>();
    for (final GetPullNotificationsResponse notification : response) {
      String appName = null;
      String graphic = null;
      if (notification.getAttr() != null) {
        appName = notification.getAttr()
            .getAppName();
        graphic = notification.getAttr()
            .getAppGraphic();
      }

      aptoideNotifications.add(
          new AptoideNotification(notification.getBody(), notification.getImg(),
              notification.getTitle(), notification.getUrl(), notification.getType(), appName,
              graphic, AptoideNotification.NOT_DISMISSED, id, notification.getExpire(),
              notification.getUrlTrack(), notification.getUrlTrackNc(), false));
    }
    return aptoideNotifications;
  }

  private List<AptoideNotification> convertCampaignNotifications(
      List<GetPullNotificationsResponse> response, String id) {
    List<AptoideNotification> aptoideNotifications = new LinkedList<>();
    for (final GetPullNotificationsResponse notification : response) {
      String appName = null;
      String graphic = null;
      if (notification.getAttr() != null) {
        appName = notification.getAttr()
            .getAppName();
        graphic = notification.getAttr()
            .getAppGraphic();
      }

      aptoideNotifications.add(
          new AptoideNotification(notification.getAbTestingGroup(), notification.getBody(),
              notification.getCampaignId(), notification.getImg(), notification.getLang(),
              notification.getTitle(), notification.getUrl(), notification.getUrlTrack(), appName,
              graphic, id, notification.getExpire(), notification.getUrlTrackNc(), false));
    }
    return aptoideNotifications;
  }
}

