package org.elasticsearch.plugin;

public class KullbackLeibler {
    public static double calculateKullbackLeiblerDistance(
            int numberOfTimesInTopkDocuments, int totalNumberOfTermsInTopKDocuments,
            int numberOfTimesInCollection, int totalNumberOfTermsInCollection) {

        if (numberOfTimesInCollection == 0) {
            return 0;
        }

        double prel = (double) numberOfTimesInTopkDocuments  / (double) totalNumberOfTermsInTopKDocuments;
        double pcol = (double) numberOfTimesInCollection     / (double) totalNumberOfTermsInCollection;

        return prel * Math.log(prel / pcol);
    }
}
