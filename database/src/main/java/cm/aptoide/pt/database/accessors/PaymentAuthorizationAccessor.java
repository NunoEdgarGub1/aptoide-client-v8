/*
 * Copyright (c) 2016.
 * Modified by Marcelo Benites on 23/11/2016.
 */

package cm.aptoide.pt.database.accessors;

import cm.aptoide.pt.database.realm.PaymentAuthorization;
import java.util.List;
import rx.Observable;

/**
 * Created by marcelobenites on 23/11/16.
 */

public class PaymentAuthorizationAccessor extends SimpleAccessor<PaymentAuthorization> {

  public PaymentAuthorizationAccessor(Database db) {
    super(db, PaymentAuthorization.class);
  }

  public Observable<List<PaymentAuthorization>> getPaymentAuthorizations() {
    return database.getAllSorted(PaymentAuthorization.class, PaymentAuthorization.PAYMENT_ID);
  }

  public void saveAll(List<PaymentAuthorization> paymentAuthorizations) {
    database.insertAll(paymentAuthorizations);
  }

  public void save(PaymentAuthorization paymentAuthorization) {
    database.insert(paymentAuthorization);
  }
}
