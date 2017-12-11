package uk.co.alt236.apkdetails.tree;

import java.util.Collection;

public interface Tree<T> {
    long getTotalNodeCount();

    void addChild(Node<T> child);

    void addChildren(Collection<Node<T>> children);

    Node<T> getRoot();

}
