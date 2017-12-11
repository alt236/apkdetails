package uk.co.alt236.apkdetails.print.tree;

import uk.co.alt236.apkdetails.print.file.FileWriter;

import java.util.List;


public class TreePrinter<T> {

    private final TreeAdapter<T> treeAdapter;

    public TreePrinter(final TreeAdapter<T> adapter) {
        this.treeAdapter = adapter;
    }

    public void print(FileWriter fileWriter, T root) {
        print(fileWriter, root, "", true);
        fileWriter.close();
    }

    private void print(FileWriter fileWriter, T node, String prefix, boolean isTail) {
        final String line = prefix + getNameJoin(isTail) + treeAdapter.getName(node);
        fileWriter.outln(line);

        final List<? extends T> children = treeAdapter.getChildren(node);

        for (int i = 0; i < children.size() - 1; i++) {
            final String text = prefix + getChildJoin(isTail);
            print(fileWriter, children.get(i), text, false);
        }

        if (children.size() > 0) {
            final String text = prefix + getChildJoin(isTail);
            print(fileWriter, children.get(children.size() - 1), text, true);
        }
    }

    private String getNameJoin(boolean isTail) {
        return (isTail ? "└── " : "├── ");
    }

    private String getChildJoin(boolean isTail) {
        return (isTail ? "    " : "│   ");
    }
}
