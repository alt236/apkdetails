package uk.co.alt236.apkdetails.print.graphml;

public class Edge {

    private final String id1;
    private final String id2;

    public Edge(final String id1,
                final String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    public String getId1() {
        return id1;
    }

    public String getId2() {
        return id2;
    }
}
