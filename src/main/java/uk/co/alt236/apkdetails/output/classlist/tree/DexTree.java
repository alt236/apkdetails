package uk.co.alt236.apkdetails.output.classlist.tree;


import uk.co.alt236.apkdetails.repo.dex.model.DexClass;
import uk.co.alt236.apkdetails.repo.dex.model.PackageName;
import uk.co.alt236.apkdetails.tree.Node;
import uk.co.alt236.apkdetails.tree.Tree;

import java.util.Collection;
import java.util.Collections;

public class DexTree implements Tree<DexClass> {
    private static final String ROOT_NODE_ID = "";
    private final Node<DexClass> root;
    private final BatchAdder batchAdder;

    public DexTree() {
        root = new DexNode(new PackageName(ROOT_NODE_ID));
        batchAdder = new BatchAdder(root);
    }

    @Override
    public long getTotalNodeCount() {
        return root.getDescendantCount();
    }

    @Override
    public Node<DexClass> getRoot() {
        return root;
    }

    @Override
    public void addChild(Node<DexClass> child) {
        addChildren(Collections.singletonList(child));
    }

    @Override
    public void addChildren(Collection<Node<DexClass>> children) {
        batchAdder.addChildren(children);
    }


}
