package org.elasticsearch.plugin;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QueryExpansion {
    private HashMap<String, TermData> terms;
    private int totalNumberOfTermsInTopKDocuments;
    private Searcher searcher;
    private SearchHit[] searchHits;
    private QueryUtil queryUtil;

    public QueryExpansion(NodeClient client) {
        searcher = new Searcher(client);
        terms = new HashMap<>();
    }

    public SearchResponse getQueryExpandedSearch(String originalQuery) {
        queryUtil = new QueryUtil(originalQuery);
        searchHits = searcher.termSearch(queryUtil.getQueryTermsFromQueryString());

        generateTermDataFromPhotosArray();
        calculateKlScores();

        String[] queryExpandedSearchTerms = queryUtil.getExpandedSearchTerms();

        return searcher.termSearchWithSearchResponse(queryExpandedSearchTerms);
    }

    private void calculateKlScores() {
        int totalNumberOfTermsInCollection = (int) searcher.getNumberOfTermsInCollection();
        TermData[] calculatedKlScores = new TermData[searchHits.length];

        int index = 0;
        for (TermData termData : terms.values()) {
            termData.calculateKlScore(totalNumberOfTermsInTopKDocuments, totalNumberOfTermsInCollection);
            calculatedKlScores[index] = termData;

            index++;
        }

        Arrays.sort(calculatedKlScores);
        queryUtil.setScoredTerms(calculatedKlScores);
    }

    private void generateTermDataFromPhotosArray() {
        for (SearchHit searchHit : searchHits) {
            SearchHitField searchHitField = searchHit.field(FieldNames.TAGS_FIELD_NAME);

            List tags = searchHitField.getValues();
            for (Object tag : tags) {
                String term = (String) tag;

                if (terms.containsKey(term)) {
                    terms.get(term).incrementNumberOfTimesInTopKDocuments();
                } else {
                    int numberOfTimesInCollection = (int) searcher.getNumberOfTimesInCollection(term);

                    TermData termData = new TermData(term);
                    termData.setNumberOfTimesInCollection(numberOfTimesInCollection);

                    terms.put(term, termData);
                }
            }

            totalNumberOfTermsInTopKDocuments += tags.size();
        }
    }
}
