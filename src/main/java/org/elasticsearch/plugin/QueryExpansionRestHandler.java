package org.elasticsearch.plugin;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;

import java.io.IOException;

import static org.elasticsearch.rest.RestRequest.Method.GET;

public class QueryExpansionRestHandler extends BaseRestHandler {

    @Inject
    public QueryExpansionRestHandler(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(GET, "/_hello", this);
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {

        return channel -> {
            Photos photos = new Photos();
            XContentBuilder builder = channel.newBuilder();
            builder.startObject();
            photos.toXContent(builder, request);
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        };
    }
}
