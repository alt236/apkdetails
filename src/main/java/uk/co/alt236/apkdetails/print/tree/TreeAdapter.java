package uk.co.alt236.apkdetails.print.tree;

import java.util.List;

public interface TreeAdapter<T> {

    String getName(T node);

    List<? extends T> getChildren(T node);
}