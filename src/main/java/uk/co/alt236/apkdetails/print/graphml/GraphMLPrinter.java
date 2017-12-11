package uk.co.alt236.apkdetails.print.graphml;

import uk.co.alt236.apkdetails.print.file.FileWriter;

import java.util.*;

public class GraphMLPrinter<T> {

    private final GraphAdapter<T> graphAdapter;
    private final Map<String, Vertex> vertexMap;
    private final List<Edge> edgeList;
    private final XmlFactory xmlFactory;

    public GraphMLPrinter(final GraphAdapter<T> adapter) {
        this.graphAdapter = adapter;
        this.vertexMap = new HashMap<>();
        this.edgeList = new ArrayList<>();
        this.xmlFactory = new XmlFactory();
    }

    public synchronized void print(FileWriter fileWriter, T root) {
        vertexMap.clear();
        edgeList.clear();

        parse(root);
        final List<Vertex> vertices = new ArrayList<>(vertexMap.values());
        vertices.sort(Comparator.comparing(Vertex::getId));

        final List<Edge> edges = new ArrayList<>(edgeList);
        edges.sort(Comparator.comparing(o -> (o.getId1() + "_" + o.getId2())));

        final String xml = xmlFactory.createXml(vertices, edges);

        fileWriter.outln(xml);
        fileWriter.close();
    }

    private void parse(T node) {
        final Vertex vertex = graphAdapter.toVertex(node);
        addVertexIfMissing(vertex);

        for (final T neighbourT : graphAdapter.getNeighbors(node)) {
            final Vertex neighbour = graphAdapter.toVertex(node);
            addVertexIfMissing(neighbour);
            edgeList.add(graphAdapter.toEdge(node, neighbourT));
            parse(neighbourT);
        }
    }

    private void addVertexIfMissing(Vertex vertex) {
        if (!vertexMap.containsKey(vertex.getId())) {
            vertexMap.put(vertex.getId(), vertex);
        }
    }
}
