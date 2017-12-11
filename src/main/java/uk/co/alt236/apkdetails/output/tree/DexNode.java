package uk.co.alt236.apkdetails.output.tree;


import uk.co.alt236.apkdetails.repo.dex.model.DexClass;
import uk.co.alt236.apkdetails.repo.dex.model.PackageName;
import uk.co.alt236.apkdetails.tree.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DexNode implements Node<DexClass> {
    private final String name;
    private final String path;
    private final String parentPath;
    private final List<Node<DexClass>> children;
    private final DexClass dexClass;

    public DexNode(PackageName packageName) {
        this.path = packageName.toString();
        this.name = packageName.getLeaf().toString();
        this.parentPath = packageName.getParent().toString();
        this.dexClass = null;
        this.children = new ArrayList<>();
    }

    public DexNode(DexClass dexClass) {
        this.path = dexClass.getType();
        this.name = dexClass.getSimpleName();
        this.parentPath = dexClass.getPackageName().toString();
        this.dexClass = dexClass;
        this.children = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public String getParentPath() {
        return parentPath;
    }

    @Override
    public int getChildCount() {
        return children.size();
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public void addChild(Node<DexClass> child) {
        children.add(child);
    }

    @Override
    public DexClass getPayload() {
        return dexClass;
    }

    @Override
    public Node<DexClass> findDescendant(String nodePath) {
        if (nodePath.equals(path)) {
            return this;
        }

        if (!nodePath.startsWith(parentPath)) {
            return null;
        }

        for (final Node<DexClass> child : children) {
            if (nodePath.equals(child.getPath())) {
                return child;
            } else if (!child.isLeaf()) {
                Node<DexClass> childResult = child.findDescendant(nodePath);
                if (childResult != null) {
                    return childResult;
                }
            }
        }

        return null;
    }

    @Override
    public long getDescendantCount() {
        long retVal = getChildCount();

        for (final Node<DexClass> child : children) {
            retVal += child.getDescendantCount();
        }

        return retVal;
    }

    @Override
    public List<Node<DexClass>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String toString() {
        return "DexNode{" +
                "path='" + path + '\'' +
                ", parentPath='" + parentPath + '\'' +
                ", children=" + children +
                '}';
    }
}
