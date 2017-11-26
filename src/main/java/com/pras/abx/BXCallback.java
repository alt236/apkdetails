package com.pras.abx;


public interface BXCallback {

    /**
     * Start XML document
     */
    void startDoc(String xmlFile);

    /**
     * Start of XML document
     *
     * @param node
     */
    void startNode(Node node);

    /**
     * TODO: read Node value
     *
     * @param lineNumber
     * @param name
     * @param value
     */
    void nodeValue(int lineNumber, String name, String value);

    /**
     * @param node
     */
    void endNode(Node node);

    void endDoc() throws Exception;
}
