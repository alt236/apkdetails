package uk.co.alt236.apkdetails.output.tree;


import uk.co.alt236.apkdetails.repo.dex.model.DexClass;
import uk.co.alt236.apkdetails.repo.dex.model.PackageName;
import uk.co.alt236.apkdetails.tree.Node;

import java.util.*;

class BatchAdder {
    private final Node<DexClass> rootNode;

    BatchAdder(Node<DexClass> rootNode) {
        this.rootNode = rootNode;
    }

    public synchronized void addChildren(final Collection<Node<DexClass>> children) {
        final Map<String, Node<DexClass>> cache = new HashMap<>(children.size());

        createParents(cache, children);

        for (final Node<DexClass> child : children) {
            addChild(cache, child);
        }
    }

    private void createParents(Map<String, Node<DexClass>> cache,
                               Collection<Node<DexClass>> children) {
        final Set<String> parentPaths = new HashSet<>();

        for (final Node<DexClass> child : children) {
            parentPaths.add(child.getParentPath());
        }

        final List<String> sortedParents = new ArrayList<>(parentPaths);
        Collections.sort(sortedParents);

        for (final String string : sortedParents) {
            createPath(cache, new PackageName(string));
        }
    }

    private void createPath(final Map<String, Node<DexClass>> cache,
                            final PackageName from) {
        final List<PackageName> packageNames = from.getParents();

        for (final PackageName pName : packageNames) {
            if (!rootNode.getName().equals(pName.toString())
                    && findNode(cache, pName.toString()) == null) {
                addNode(cache, pName.getParent(), new DexNode(pName));
            }
        }

        addNode(cache, from.getParent(), new DexNode(from));
    }

    private void addChild(final Map<String, Node<DexClass>> cache,
                          final Node<DexClass> child) {
        final Node<DexClass> existingNode = findNode(cache, child.getPath());

        if (existingNode != null) {
            throw new IllegalStateException("Trying to add " + child.getPath() + " more than once");
        }


        final String parentPath = child.getParentPath();
        final Node<DexClass> parentNode = findNode(cache, parentPath);

        try {
            parentNode.addChild(child);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error while adding '" + child.getPath() + "' to '" + parentPath + "'");
            System.err.println(rootNode);
            System.exit(1);
        }
    }


    private Node<DexClass> findNode(final Map<String, Node<DexClass>> cache,
                                    final String path) {
        final Node<DexClass> retVal;
        if (cache.containsKey(path)) {
            retVal = cache.get(path);
        } else {
            retVal = rootNode.findDescendant(path);
            if (retVal != null) {
                cache.put(path, retVal);
            }
        }

        return retVal;
    }

    private void addNode(final Map<String, Node<DexClass>> cache,
                         final PackageName parentPath,
                         final Node<DexClass> child) {
        final Node<DexClass> parent = findNode(cache, parentPath.toString());
        parent.addChild(child);
        cache.put(child.getPath(), child);
    }
}
