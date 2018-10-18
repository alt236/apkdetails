package uk.co.alt236.apkdetails.output.classlist.tree;

import uk.co.alt236.apkdetails.print.tree.TreeAdapter;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;
import uk.co.alt236.apkdetails.tree.Node;

import java.util.List;

public class ClassTreeAdapter implements TreeAdapter<Node<DexClass>> {

    @Override
    public String getName(Node<DexClass> node) {
        if (node.isLeaf()) {
            return node.getName();
        } else {
            return node.getName() + " (" + node.getDescendantCount() + ")";
        }
    }

    @Override
    public List<? extends Node<DexClass>> getChildren(Node<DexClass> node) {
        return node.getChildren();
    }
}
