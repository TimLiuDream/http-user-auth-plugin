package org.elasticsearch.plugin.http.user.auth;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.elasticsearch.SpecialPermission;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.rest.BytesRestResponse;
import org.elasticsearch.rest.RestChannel;
import org.elasticsearch.rest.RestFilter;
import org.elasticsearch.rest.RestFilterChain;
import org.elasticsearch.rest.RestRequest;
import org.elasticsearch.rest.RestStatus;

public class AuthRestFilter extends RestFilter {
	Client client;
	ExamplePluginConfiguration config;

	public AuthRestFilter(Client client, ExamplePluginConfiguration config) {
		this.client = client;
		this.config = config;
	}

	@Override
	public void process(RestRequest request, RestChannel channel, RestFilterChain filterChain) throws Exception {
		boolean enable = config.getEnable();
		String name = config.getUserName();
		String password = config.getUserPassword();
		System.out.printf("enable: %s\n", enable);
		System.out.printf("name: %s\n", name);
		System.out.printf("password: %s\n", password);
		if (enable) {
			try {
				// auth check
				Iterable<Entry<String, String>> headers = request.headers();
				for (Entry<String, String> header : headers) {
					System.out.printf("header.getKey(): %s\n", header.getKey());
					System.out.printf("header.getValue(): %s\n", header.getValue());

					if (header.getKey().toLowerCase().equals("authorization")) {
						String authStr = header.getValue();
						System.out.printf("authStr: %s\n", authStr);
						if (authStr == null || authStr.equals("")) {
							// forbidden path
							BytesRestResponse resp = new BytesRestResponse(RestStatus.FORBIDDEN, "Forbidden path");
							channel.sendResponse(resp);
							return;
						}
						authStr = authStr.trim();
						String[] authArr = authStr.split(" ");
						for (int i = 1; i < authArr.length; i++) {
							if (authArr[i].equals("")) {
								continue;
							}
							String auth = name + ":" + password;
							String encAuth = Base64.encodeBase64String(auth.getBytes("UTF-8"));
							if (authArr[i].equals(encAuth)) {
								filterChain.continueProcessing(request, channel);
								return;
							} else {
								// forbidden path
								BytesRestResponse resp = new BytesRestResponse(RestStatus.FORBIDDEN, "Forbidden path");
								channel.sendResponse(resp);
								return;
							}
						}
					}
				}
				// forbidden path
				BytesRestResponse resp = new BytesRestResponse(RestStatus.FORBIDDEN, "Forbidden path");
				channel.sendResponse(resp);
				return;
			} catch (Exception ex) {
				channel.sendResponse(new BytesRestResponse(RestStatus.INTERNAL_SERVER_ERROR, ""));
				Loggers.getLogger(getClass()).error("", ex);
			}
		} else {
			filterChain.continueProcessing(request, channel);
			return;
		}
		return;
	}
}
