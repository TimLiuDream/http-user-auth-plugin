package org.elasticsearch.plugin.http.user.auth;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;

public class AuthRestHandler extends BaseRestHandler {

  @Inject
  public AuthRestHandler(Settings settings, RestController restController, Client client,
      ExamplePluginConfiguration config) {
    super(settings, restController, client);
    RestFilter filter = new AuthRestFilter(client, config);
    restController.registerFilter(filter);
  }

  @Override
  protected void handleRequest(RestRequest request, RestChannel channel, Client client) {
  }
}
