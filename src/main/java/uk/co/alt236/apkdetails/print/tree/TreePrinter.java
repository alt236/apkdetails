package uk.co.alt236.apkdetails.print.tree;

import uk.co.alt236.apkdetails.print.writer.Writer;

import java.util.List;


public class TreePrinter<T> {

    private final TreeAdapter<T> treeAdapter;

    public TreePrinter(final TreeAdapter<T> adapter) {
        this.treeAdapter = adapter;
    }

    public void print(Writer writer, T root) {
        print(writer, root, "", true);
        writer.close();
    }

    private void print(Writer writer, T node, String prefix, boolean isTail) {
        final String line = prefix + getNameJoin(isTail) + treeAdapter.getName(node);
        writer.outln(line);

        final List<? extends T> children = treeAdapter.getChildren(node);

        for (int i = 0; i < children.size() - 1; i++) {
            final String text = prefix + getChildJoin(isTail);
            print(writer, children.get(i), text, false);
        }

        if (children.size() > 0) {
            final String text = prefix + getChildJoin(isTail);
            print(writer, children.get(children.size() - 1), text, true);
        }
    }

    private String getNameJoin(boolean isTail) {
        return (isTail ? "└── " : "├── ");
    }

    private String getChildJoin(boolean isTail) {
        return (isTail ? "    " : "│   ");
    }
}
