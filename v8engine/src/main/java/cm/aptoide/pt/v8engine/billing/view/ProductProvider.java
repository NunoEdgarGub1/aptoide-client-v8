package cm.aptoide.pt.v8engine.billing.view;

import android.content.Intent;
import android.os.Bundle;
import cm.aptoide.pt.v8engine.billing.Billing;
import cm.aptoide.pt.v8engine.billing.Product;
import rx.Single;

public class ProductProvider {

  private static final String EXTRA_APP_ID =
      "cm.aptoide.pt.v8engine.view.payment.intent.extra.APP_ID";
  private static final String EXTRA_STORE_NAME =
      "cm.aptoide.pt.v8engine.view.payment.intent.extra.STORE_NAME";
  private static final String EXTRA_SPONSORED =
      "cm.aptoide.pt.v8engine.view.payment.intent.extra.SPONSORED";
  private static final String EXTRA_API_VERSION =
      "cm.aptoide.pt.v8engine.view.payment.intent.extra.API_VERSION";
  private static final String EXTRA_PACKAGE_NAME =
      "cm.aptoide.pt.v8engine.view.payment.intent.extra.PACKAGE_NAME";
  private static final String EXTRA_SKU = "cm.aptoide.pt.v8engine.view.payment.intent.extra.SKU";
  private static final String EXTRA_TYPE = "cm.aptoide.pt.v8engine.view.payment.intent.extra.TYPE";
  private static final String EXTRA_DEVELOPER_PAYLOAD =
      "cm.aptoide.pt.v8engine.view.payment.intent.extra.DEVELOPER_PAYLOAD";

  private final Billing billing;

  private final long appId;
  private final String storeName;
  private final boolean sponsored;

  private final int apiVersion;
  private final String type;
  private final String sku;
  private final String packageName;
  private final String developerPayload;

  public static ProductProvider fromIntent(Billing billing, Intent intent) {
    return new ProductProvider(billing, intent.getLongExtra(EXTRA_APP_ID, -1),
        intent.getStringExtra(EXTRA_STORE_NAME), intent.getBooleanExtra(EXTRA_SPONSORED, false),
        intent.getIntExtra(EXTRA_API_VERSION, -1), intent.getStringExtra(EXTRA_TYPE),
        intent.getStringExtra(EXTRA_SKU), intent.getStringExtra(EXTRA_PACKAGE_NAME),
        intent.getStringExtra(EXTRA_DEVELOPER_PAYLOAD));
  }

  public static Bundle createIntentBundle(long appId, String storeName, boolean sponsored) {
    final Bundle bundle = new Bundle();
    bundle.putLong(ProductProvider.EXTRA_APP_ID, appId);
    bundle.putString(ProductProvider.EXTRA_STORE_NAME, storeName);
    bundle.putBoolean(ProductProvider.EXTRA_SPONSORED, sponsored);
    return bundle;
  }

  public static Bundle createIntentBundle(int apiVersion, String packageName, String type,
      String sku, String developerPayload) {
    final Bundle bundle = new Bundle();
    bundle.putInt(ProductProvider.EXTRA_API_VERSION, apiVersion);
    bundle.putString(ProductProvider.EXTRA_PACKAGE_NAME, packageName);
    bundle.putString(ProductProvider.EXTRA_TYPE, type);
    bundle.putString(ProductProvider.EXTRA_SKU, sku);
    bundle.putString(ProductProvider.EXTRA_DEVELOPER_PAYLOAD, developerPayload);
    return bundle;
  }

  private ProductProvider(Billing billing, long appId, String storeName, boolean sponsored,
      int apiVersion, String type, String sku, String packageName, String developerPayload) {
    this.billing = billing;
    this.appId = appId;
    this.storeName = storeName;
    this.sponsored = sponsored;
    this.apiVersion = apiVersion;
    this.type = type;
    this.sku = sku;
    this.packageName = packageName;
    this.developerPayload = developerPayload;
  }

  public Single<Product> getProduct() {

    if (storeName != null) {
      return billing.getPaidAppProduct(appId, storeName, sponsored);
    }

    if (sku != null) {
      return billing.getInAppProduct(apiVersion, packageName, sku, type, developerPayload);
    }

    return Single.error(new IllegalStateException("No product information provided to presenter."));
  }
}
