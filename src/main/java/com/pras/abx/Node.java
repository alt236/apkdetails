package com.pras.abx;

import java.util.ArrayList;

public class Node {

    public static final int ROOT = 1;
    /**
     * Node order index
     */
    private int index;
    private int linenumber;
    private String name;
    private String namespacePrefix;
    private String namespaceURI;
    private int namespaceLineNumber;
    private ArrayList<Attribute> attrs = new ArrayList<>();

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLinenumber() {
        return linenumber;
    }

    public void setLinenumber(int linenumber) {
        this.linenumber = linenumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNamespacePrefix() {
        return namespacePrefix;
    }

    public void setNamespacePrefix(String namespacePrefix) {
        this.namespacePrefix = namespacePrefix;
    }

    public String getNamespaceURI() {
        return namespaceURI;
    }

    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    public int getNamespaceLineNumber() {
        return namespaceLineNumber;
    }

    public void setNamespaceLineNumber(int namespaceLineNumber) {
        this.namespaceLineNumber = namespaceLineNumber;
    }

    public ArrayList<Attribute> getAttrs() {
        return attrs;
    }

    public void setAttrs(ArrayList<Attribute> attrs) {
        this.attrs = attrs;
    }

    public void addAttribute(Attribute attr) {
        attrs.add(attr);
    }
}