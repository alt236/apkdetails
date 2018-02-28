package uk.co.alt236.apkdetails.repo.manifest;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.alt236.apkdetails.xml.AndroidXmlDocument;
import uk.co.alt236.apkdetails.xml.AndroidXmlValueParser;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

class NodeParser {

    private static final String XML_TAG_ANDROID_NAME = "android:name";
    private static final String XML_TAG_NAME = "name";
    private static final String XML_TAG_ANDROID_REQUIRED = "android:required";
    private static final String XML_TAG_REQUIRED = "required";

    private final AndroidXmlValueParser valueParser;

    private AndroidXmlDocument xmlDocument;

    NodeParser() {
        this.valueParser = new AndroidXmlValueParser();
    }

    public void setXmlDocument(AndroidXmlDocument xmlDocument) {
        this.xmlDocument = xmlDocument;
    }

    List<String> getAndroidNamesOfNodes(List<String> items, String expression) {
        final NodeList nodeList = xmlDocument.getNodes(expression);
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node nameNode = getAndroidNameNode(nodeList.item(i));

            if (nameNode != null) {
                items.add(nameNode.getNodeValue());
            }
        }

        Collections.sort(items);
        return items;
    }

    List<Requirable> getRequirableItems(List<Requirable> items, String expression) {
        final NodeList nodeList = xmlDocument.getNodes(expression);

        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node currentNode = nodeList.item(i);

            final Node nameNode = getAndroidNameNode(currentNode);
            final Node requiredNode = getIsRequiredNode(currentNode);
            final boolean required;

            // The convention is that if the tag is missing, the item is required.
            if (requiredNode == null) {
                required = true;
            } else {
                required = valueParser.parseBoolean(requiredNode.getNodeValue());
            }

            if (nameNode != null && requiredNode != null) {
                final String name = nameNode.getNodeValue();

                items.add(new Requirable(name, required));
            }
        }

        items.sort((o1, o2) -> o1.getName().compareTo(o2.getName()));
        return items;
    }

    @Nullable
    private Node getAndroidNameNode(final Node node) {
        if (node.getAttributes().getNamedItem(XML_TAG_ANDROID_NAME) != null) {
            return node.getAttributes().getNamedItem(XML_TAG_ANDROID_NAME);
        } else {
            return node.getAttributes().getNamedItem(XML_TAG_NAME);
        }
    }

    @Nullable
    private Node getIsRequiredNode(final Node node) {
        if (node.getAttributes().getNamedItem(XML_TAG_ANDROID_REQUIRED) != null) {
            return node.getAttributes().getNamedItem(XML_TAG_ANDROID_REQUIRED);
        } else {
            return node.getAttributes().getNamedItem(XML_TAG_REQUIRED);
        }
    }

}
