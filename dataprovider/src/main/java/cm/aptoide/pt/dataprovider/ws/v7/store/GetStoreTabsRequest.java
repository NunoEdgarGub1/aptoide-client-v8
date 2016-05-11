/*
 * Copyright (c) 2016.
 * Modified by Neurophobic Animal on 11/05/2016.
 */

package cm.aptoide.pt.dataprovider.ws.v7.store;

import cm.aptoide.pt.dataprovider.ws.Api;
import cm.aptoide.pt.dataprovider.ws.v7.BaseBody;
import cm.aptoide.pt.dataprovider.ws.v7.V7;
import cm.aptoide.pt.model.v7.store.GetStoreTabs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import rx.Observable;

/**
 * Created by neuro on 22-04-2016.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GetStoreTabsRequest extends V7<GetStoreTabs> {

	private final Body body = new Body();

	private GetStoreTabsRequest(boolean bypassCache) {
		super(bypassCache);
	}

	public static GetStoreTabsRequest of(String storeName, boolean bypassCache) {
		GetStoreTabsRequest getStoreDisplaysRequest = new GetStoreTabsRequest(bypassCache);

		getStoreDisplaysRequest.body.setStoreName(storeName);

		return getStoreDisplaysRequest;
	}

	public static GetStoreTabsRequest of(int storeId, boolean bypassCache) {
		GetStoreTabsRequest getStoreDisplaysRequest = new GetStoreTabsRequest(bypassCache);

		getStoreDisplaysRequest.body.setStoreId(storeId);

		return getStoreDisplaysRequest;
	}

	@Override
	protected Observable<GetStoreTabs> loadDataFromNetwork(Interfaces interfaces) {
		return interfaces.getStoreTabs(body, bypassCache);
	}

	@Data
	@Accessors(chain = true)
	@EqualsAndHashCode(callSuper = true)
	public static class Body extends BaseBody {

		private String lang = Api.LANG;
		private Integer storeId;
		private String storeName;
		private String storePassSha1;
		private String storeUser;
	}
}
