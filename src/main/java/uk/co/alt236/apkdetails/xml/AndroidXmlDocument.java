package uk.co.alt236.apkdetails.xml;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.io.StringReader;

public class AndroidXmlDocument {
    private final String xml;
    private final Document document;

    public AndroidXmlDocument(final String xml) {
        this.xml = xml;
        final InputSource is = new InputSource(new StringReader(xml));

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            final DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(is);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    public String getXml() {
        return xml;
    }

    public long getLongValue(final String xPathExpression) {
        final String result = evaluateExpressionForString(xPathExpression);
        try {
            return Long.parseLong(result);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public int getIntValue(final String xPathExpression) {
        final String result = evaluateExpressionForString(xPathExpression);
        try {
            return Integer.parseInt(result);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public boolean getBooleanValue(final String xPathExpression) {
        final String result = evaluateExpressionForString(xPathExpression);
        return Boolean.parseBoolean(result);
    }

    public String getStringValue(String expression) {
        final String result = evaluateExpressionForString(expression);
        return result == null ? "" : result.trim();
    }

    public NodeList getNodes(String expression) {
        return evaluateExpressionForNodeList(expression);
    }

    private String evaluateExpressionForString(final String expression) {
        final XPath xpath = AndroidXPathFactory.newXPath();
        try {
            return xpath.evaluate(expression, document);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }

    private NodeList evaluateExpressionForNodeList(final String expression) {
        final XPath xpath = AndroidXPathFactory.newXPath();
        try {
            return (NodeList) xpath.evaluate(expression, document, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
