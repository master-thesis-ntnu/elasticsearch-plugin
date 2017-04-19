package org.elasticsearch.plugin;

import org.elasticsearch.action.fieldstats.FieldStatsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;

public class Searcher {
    private NodeClient client;

    private static final String INDEX_NAME = "photos";

    public Searcher(NodeClient client) {
        this.client = client;
    }

    public SearchHits termSearch(String termQuery) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME)
                .setQuery(QueryBuilders.termsQuery("tags", termQuery))
                .get();

        return searchResponse.getHits();
    }

    public long getNumberOfTimesInCollection(String term) {
        SearchResponse searchResponse = client.prepareSearch(INDEX_NAME)
                .setQuery(QueryBuilders.termQuery("tags", term))
                .get();

        return searchResponse.getHits().getTotalHits();
    }

    public long getNumberOfTermsInCollection() {
        FieldStatsResponse fieldStatsResponse = client.prepareFieldStats().setFields(FieldNames.TAGS_FIELD_NAME).get();

        return fieldStatsResponse.getAllFieldStats().get(FieldNames.TAGS_FIELD_NAME).getSumTotalTermFreq();
    }
}
