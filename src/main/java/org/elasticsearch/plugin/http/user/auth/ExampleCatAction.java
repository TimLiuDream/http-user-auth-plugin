package org.elasticsearch.plugin.http.user.auth;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.Table;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.cat.AbstractCatAction;
import org.elasticsearch.rest.action.support.RestTable;

import static org.elasticsearch.rest.RestRequest.Method.GET;

/**
 * Example of adding a cat action with a plugin.
 */
public class ExampleCatAction extends AbstractCatAction {
    private final ExamplePluginConfiguration config;

    @Inject
    public ExampleCatAction(Settings settings, RestController controller,
                            Client client, ExamplePluginConfiguration config) {
        super(settings, controller, client);
        this.config = config;
        controller.registerHandler(GET, "/_cat/configured_example", this);
    }

    @Override
    protected void doRequest(final RestRequest request, final RestChannel channel, final Client client) {
        Table table = getTableWithHeader(request);
        table.startRow();
        table.addCell(config.getTestConfig());
        table.endRow();
        try {
            channel.sendResponse(RestTable.buildResponse(table, channel));
        } catch (Throwable e) {
            try {
                channel.sendResponse(new BytesRestResponse(channel, e));
            } catch (Throwable e1) {
                logger.error("failed to send failure response", e1);
            }
        }
    }

    @Override
    protected void documentation(StringBuilder sb) {
        sb.append(documentation());
    }

    public static String documentation() {
        return "/_cat/configured_example\n";
    }

    @Override
    protected Table getTableWithHeader(RestRequest request) {
        final Table table = new Table();
        table.startHeaders();
        table.addCell("test", "desc:test");
        table.endHeaders();
        return table;
    }
}