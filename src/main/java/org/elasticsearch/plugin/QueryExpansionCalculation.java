package org.elasticsearch.plugin;

import org.elasticsearch.search.SearchHit;

import java.util.HashMap;

public class QueryExpansionCalculation {
    private HashMap<String, TermData> terms;
    private SearchHit[] searchHits;
    private String originalQuery;
    private boolean hasCalculatedKlScores;

    public QueryExpansionCalculation(SearchHit[] searchHits, String originalQuery) {
        this.searchHits = searchHits;
        this.originalQuery = originalQuery;

        terms = new HashMap<String, TermData>();
    }

    public void calculateKlScores() {

    }

    public String getQueryExpandedSearchTerms() {
        if (!hasCalculatedKlScores) {
            throw new RuntimeException("Kullback Leibler scores has to be calculated before expanded terms can be calculated");
        }

        return null;
    }
}
