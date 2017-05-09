package org.elasticsearch.plugin;

public class TermData implements Comparable<TermData> {
    private String term;
    private int numberOfTimesInTopKDocuments = 1;
    private int numberOfTimesInCollection;
    private double klScore;

    public TermData(String term) {
        this.term = term;
    }

    public String getTerm() {
        return term;
    }

    public int getNumberOfTimesInTopKDocuments() {
        return numberOfTimesInTopKDocuments;
    }

    public void incrementNumberOfTimesInTopKDocuments() {
        numberOfTimesInTopKDocuments++;
    }

    public int getNumberOfTimesInCollection() {
        return numberOfTimesInCollection;
    }

    public void setNumberOfTimesInCollection(int numberOfTimesInCollection) {
        this.numberOfTimesInCollection = numberOfTimesInCollection;
    }

    public void calculateKlScore(int totalNumberOfTermsInTopKDocuments, int totalNumberOfTermsInCollection) {
        // System.out.println("Term: " + term);
        // System.out.println("Number of times in top k: " + getNumberOfTimesInTopKDocuments());
        // System.out.println("Number of terms in top k: " + totalNumberOfTermsInTopKDocuments);
        // System.out.println("Number of times in collection: " + getNumberOfTimesInCollection());
        // System.out.println("Number of terms in collection: " + totalNumberOfTermsInCollection);
        klScore = KullbackLeibler.calculateKullbackLeiblerDistance(
                numberOfTimesInTopKDocuments,
                totalNumberOfTermsInTopKDocuments,
                numberOfTimesInCollection,
                totalNumberOfTermsInCollection
        );
        // System.out.println("Score: " + klScore);
    }

    public double getKlScore() {
        return klScore;
    }

    @Override
    public String toString() {
        return "TermData{" +
                "term='" + term + '\'' +
                ", numberOfTimesInTopKDocuments=" + numberOfTimesInTopKDocuments +
                ", getTotalNumberOfTimesInCollection=" + numberOfTimesInCollection +
                '}';
    }

    public int compareTo(TermData o) {
        return this.getKlScore() > o.getKlScore()? -1 : 1;
    }
}
