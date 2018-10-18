package uk.co.alt236.apkdetails.output.classlist.graphml;

import uk.co.alt236.apkdetails.print.graphml.Edge;
import uk.co.alt236.apkdetails.print.graphml.GraphAdapter;
import uk.co.alt236.apkdetails.print.graphml.Vertex;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;
import uk.co.alt236.apkdetails.tree.Node;

import java.util.List;

public class GraphMlTreeAdapter implements GraphAdapter<Node<DexClass>> {

    @Override
    public List<? extends Node<DexClass>> getNeighbors(Node<DexClass> node) {
        return node.getChildren();
    }

    @Override
    public String getId(Node<DexClass> node) {
        return "".equals(node.getPath()) ? "[root]" : node.getPath();
    }

    @Override
    public Vertex toVertex(Node<DexClass> node) {
        return new Vertex(getId(node));
    }

    @Override
    public Edge toEdge(Node<DexClass> node1, Node<DexClass> node2) {
        return new Edge(getId(node1), getId(node2));

    }
}
