package uk.co.alt236.apkdetails.print.graphml;

import java.util.List;

public interface GraphAdapter<T> {

    String getId(T node);

    Vertex toVertex(T node);

    Edge toEdge(T node1, T node2);

    List<? extends T> getNeighbors(T node);
}
