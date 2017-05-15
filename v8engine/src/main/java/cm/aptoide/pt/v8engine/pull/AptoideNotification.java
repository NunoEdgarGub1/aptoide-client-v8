package cm.aptoide.pt.v8engine.pull;

import android.support.annotation.IntDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import lombok.ToString;

/**
 * Created by trinkes on 03/05/2017.
 */

@ToString(of = { "title" }) public class AptoideNotification {
  public static final int CAMPAIGN = 0;
  public static final int COMMENT = 1;
  public static final int LIKE = 2;
  public static final int POPULAR = 3;
  private final String appName;
  private final String graphic;
  private String abTestingGroup;
  private String body;
  private int campaignId;
  private String img;
  private String lang;
  private String title;
  private String url;
  private String urlTrack;
  private @NotificationType int type;
  private long timeStamp;
  private boolean showed;

  public AptoideNotification(String body, String img, String title, String url, int type,
      long timeStamp, String appName, String graphic) {
    this.body = body;
    this.img = img;
    this.title = title;
    this.url = url;
    this.type = type;
    this.timeStamp = timeStamp;
    this.appName = appName;
    this.graphic = graphic;
  }

  public AptoideNotification(String body, String img, String title, String url, int type,
      String appName, String graphic) {
    this(body, img, title, url, type, System.currentTimeMillis(), appName, graphic);
  }

  public AptoideNotification(String abTestingGroup, String body, int campaignId, String img,
      String lang, String title, String url, String urlTrack, String appName, String graphic) {
    this(abTestingGroup, body, campaignId, img, lang, title, url, urlTrack,
        System.currentTimeMillis(), CAMPAIGN, false, appName, graphic);
  }

  public AptoideNotification(String abTestingGroup, String body, int campaignId, String img,
      String lang, String title, String url, String urlTrack, long timeStamp, int type,
      boolean showed, String appName, String graphic) {
    this(body, img, title, url, type, timeStamp, appName, graphic);
    this.abTestingGroup = abTestingGroup;
    this.campaignId = campaignId;
    this.lang = lang;
    this.urlTrack = urlTrack;
    this.showed = showed;
  }

  public String getAppName() {
    return appName;
  }

  public String getGraphic() {
    return graphic;
  }

  public boolean isShowed() {
    return showed;
  }

  public void setShowed(boolean showed) {
    this.showed = showed;
  }

  public @NotificationType int getType() {
    return type;
  }

  public String getAbTestingGroup() {
    return abTestingGroup;
  }

  public String getBody() {
    return body;
  }

  public int getCampaignId() {
    return campaignId;
  }

  public String getImg() {
    return img;
  }

  public String getLang() {
    return lang;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  public String getUrlTrack() {
    return urlTrack;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  @Retention(RetentionPolicy.SOURCE) @IntDef({ CAMPAIGN, COMMENT, LIKE, POPULAR })
  public @interface NotificationType {
  }
}
