/*
 * Copyright (c) 2016.
 * Modified by Neurophobic Animal on 06/07/2016.
 */

package cm.aptoide.pt.dataprovider.ws.v7;

import java.util.List;

import cm.aptoide.accountmanager.AptoideAccountManager;
import cm.aptoide.pt.dataprovider.ws.Api;
import cm.aptoide.pt.model.v7.timeline.GetUserTimeline;
import cm.aptoide.pt.networkclient.WebService;
import cm.aptoide.pt.networkclient.okhttp.OkHttpClientFactory;
import cm.aptoide.pt.preferences.secure.SecurePreferences;
import cm.aptoide.pt.utils.AptoideUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.OkHttpClient;
import retrofit2.Converter;
import rx.Observable;

/**
 * Returns the user timeline. A series of cards with information related to application releases, news, application
 * updates and so on.
 */
public class GetUserTimelineRequest extends V7<GetUserTimeline, GetUserTimelineRequest.Body> {

	private String url;

	public GetUserTimelineRequest(String url, Body body, OkHttpClient httpClient, Converter.Factory converterFactory, String baseHost) {
		super(body, httpClient, converterFactory, baseHost);
		this.url = url;
	}

	public static GetUserTimelineRequest of(String url, int limit, int offset, List<String> packages) {
		GetUserTimelineRequest getAppRequest = new GetUserTimelineRequest(url, new Body(SecurePreferences.getAptoideClientUUID(), AptoideAccountManager
				.getAccessToken(), AptoideUtils.Core.getVerCode(), "pool", Api.LANG, Api.MATURE, Api.Q, limit, offset, packages),
				OkHttpClientFactory.newClient(), WebService.getDefaultConverter(), BASE_HOST);
		return getAppRequest;
	}

	@Override
	protected Observable<GetUserTimeline> loadDataFromNetwork(Interfaces interfaces, boolean bypassCache) {
		return interfaces.getUserTimeline(url, body, bypassCache);
	}

	@EqualsAndHashCode(callSuper = true)
	public static class Body extends BaseBody implements Endless {

		@Getter private int limit;
		@Getter @Setter private int offset;
		@Getter private List<String> packageNames;

		public Body(String aptoideId, String accessToken, int aptoideVercode, String cdn, String lang, boolean mature, String q, Integer limit, Integer
				offset, List<String> packageNames) {
			super(aptoideId, accessToken, aptoideVercode, cdn, lang, mature, q);
			this.limit = limit;
			this.offset = offset;
			this.packageNames = packageNames;
		}
	}
}
