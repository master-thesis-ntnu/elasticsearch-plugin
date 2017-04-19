package org.elasticsearch.plugin;

import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHitField;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class QueryExpansionCalculation {
    private HashMap<String, TermData> terms;
    private SearchHit[] searchHits;
    private String originalQuery;
    private boolean hasCalculatedKlScores;

    private int totalNumberOfTermsInTopKDocuments;
    private int totalNumberOfTermsInCollection;

    private Searcher searcher;

    public QueryExpansionCalculation(SearchHit[] searchHits, String originalQuery, NodeClient client) {
        this.searchHits = searchHits;
        this.originalQuery = originalQuery;

        searcher = new Searcher(client);
        terms = new HashMap<String, TermData>();
    }

    public void calculateKlScores() {
        totalNumberOfTermsInCollection = (int) searcher.getNumberOfTermsInCollection();
        generateTermDataFromPhotosArray();

        TermData[] calculatedKlScores = new TermData[searchHits.length];

        int index = 0;
        for (TermData termData : terms.values()) {
            termData.calculateKlScore(totalNumberOfTermsInTopKDocuments, totalNumberOfTermsInCollection);
            calculatedKlScores[index] = termData;

            index++;
        }

        Arrays.sort(calculatedKlScores);

        //  TODO: Return expanded query terms
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
                    int numberOfTimesInCollection = 10; // TODO: Fetch dynamically
                    TermData termData = new TermData(term);
                    termData.setNumberOfTimesInCollection(numberOfTimesInCollection);

                    terms.put(term, termData);
                }
            }

            totalNumberOfTermsInTopKDocuments += tags.size();
        }
    }

    public String getQueryExpandedSearchTerms() {
        if (!hasCalculatedKlScores) {
            throw new RuntimeException("Kullback Leibler scores has to be calculated before expanded terms can be calculated");
        }

        return null;
    }
}
