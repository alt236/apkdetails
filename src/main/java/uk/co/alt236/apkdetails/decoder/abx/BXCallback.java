package uk.co.alt236.apkdetails.decoder.abx;


public interface BXCallback {

    /**
     * Start XML document
     */
    public void startDoc(String xmlFile);

    /**
     * Start of XML document
     *
     * @param node
     */
    public void startNode(Node node);

    /**
     * TODO: read Node value
     *
     * @param lineNumber
     * @param name
     * @param value
     */
    public void nodeValue(int lineNumber, String name, String value);

    /**
     * @param node
     */
    public void endNode(Node node);

    public void endDoc() throws Exception;
}
