package org.elasticsearch.plugin;

public class QueryUtil {
    private String[] originalQueryTerms;
    private TermData[] scoredTerms;

    private static final String SPLIT_CHARACTER = " ";
    private static final int MAX_NUMBER_OF_SEARCH_TERMS = 5;

    public QueryUtil(String queryString) {
        originalQueryTerms = queryString.split(SPLIT_CHARACTER);
    }

    public String[] getQueryTermsFromQueryString() {
        return originalQueryTerms;
    }

    public void setScoredTerms(TermData[] scoredTerms) {
        this.scoredTerms = scoredTerms;
    }

    /**
     * This method has to be called after setScoredTerms.
     * @return MultiPhraseQuery
     */
    public String[] getExpandedSearchTerms() {
        if (scoredTerms == null) {
            throw new IllegalArgumentException("Scored terms is not set");
        }

        int sizeOfNewQueryTerms = getSizeOfNewQueryTerms();
        String[] expandedQueryTerms1 = new String[sizeOfNewQueryTerms];

        int newQueryTermsIndex = 0;

        for (int i = 0; i < sizeOfNewQueryTerms; i++) {
            if (i < originalQueryTerms.length) {
                expandedQueryTerms1[i] = originalQueryTerms[i];
            } else {
                for (int j = newQueryTermsIndex; j < scoredTerms.length; j++) {
                    String term = scoredTerms[j].getTerm();

                    if (!termExistsInOriginalQuery(term)) {
                        expandedQueryTerms1[i] = term;
                        newQueryTermsIndex++;

                        break;
                    }

                    newQueryTermsIndex++;
                }
            }
        }

        return expandedQueryTerms1;
    }

    private boolean termExistsInOriginalQuery(String term) {
        return ArrayUtils.contains(originalQueryTerms, term);
    }

    private int getSizeOfNewQueryTerms() {
        int combinedTermSize = originalQueryTerms.length + scoredTerms.length;

        return combinedTermSize > MAX_NUMBER_OF_SEARCH_TERMS? MAX_NUMBER_OF_SEARCH_TERMS : combinedTermSize;
    }

    private String[] splitStringOnCharacter(String string, String splitCharacter) {
        return string.split(splitCharacter);
    }
}