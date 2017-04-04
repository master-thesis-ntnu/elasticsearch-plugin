package org.elasticsearch.plugin;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.bytes.BytesReference;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentHelper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.*;

import java.io.IOException;
import java.util.Map;

import static org.elasticsearch.rest.RestRequest.Method.POST;

public class QueryExpansionRestHandler extends BaseRestHandler {
    private static final String INDEX_NAME = "photos";

    @Inject
    public QueryExpansionRestHandler(Settings settings, RestController controller) {
        super(settings);
        controller.registerHandler(POST, "/_expansion", this);
    }

    @Override
    protected RestChannelConsumer prepareRequest(RestRequest request, NodeClient client) throws IOException {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME)
                .setQuery(QueryBuilders.termsQuery("tags", "test"))
                .get();
        String searchString = retrieveSearchStringFromRequest(request.content());

        String result = searchString;
        return channel -> {
            Photos photos = new Photos(result);
            XContentBuilder builder = channel.newBuilder();
            builder.startObject();
            photos.toXContent(builder, request);
            builder.endObject();
            channel.sendResponse(new BytesRestResponse(RestStatus.OK, builder));
        };
    }

    private String retrieveSearchStringFromRequest(BytesReference postRequestContent) {
        String searchQuery = null;
        Map<String, Object> requestBodyDictionary = XContentHelper.convertToMap(postRequestContent, false).v2();

        if (requestBodyDictionary.containsKey("search_query")) {
            searchQuery = (String) requestBodyDictionary.get("search_query");
        }

        return searchQuery;
    }
}
