package com.pras;

import com.pras.abx.Attribute;
import com.pras.abx.BXCallback;
import com.pras.abx.Node;
import com.pras.utils.Log;

import java.io.FileOutputStream;
import java.util.ArrayList;


/**
 * Handles Binary XML parsing callback and generate XML. You can use your
 * own XML generator and register with Android_BX2 (Android Binary XML) parser.
 *
 * @author prasanta
 */
public class GenXML implements BXCallback {

    private final StringBuffer xml = new StringBuffer();
    // Current line number
    private int cl = 1;
    private Node currentNode = null;
    private String xmlFile;
    private final String tag = getClass().getSimpleName();

    public void startDoc(String xmlFile) {
        this.xmlFile = xmlFile;
        // TODO: Encoding value
        xml.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
    }

    public void startNode(Node node) {
        // TODO Auto-generated method stub
        if (node == null)
            return;

        currentNode = node;
        Log.d(tag, "[GenXML] Node LN: " + node.getLinenumber() + " CL: " + cl);

        if (cl == node.getLinenumber()) {
            // TODO: Remove this case. Only for temp fix.
            // FIX line number problem.
            xml.append("\n");
        } else {
            while (cl < node.getLinenumber()) {
                // Next line
                ln();
            }
        }

        xml.append("<" + node.getName());

        if (node.getIndex() == Node.ROOT) {
            // Add name space value
            xml.append(" xmlns:" + node.getNamespacePrefix() + "=\"" + node.getNamespaceURI() + "\"");
        }

        ArrayList<Attribute> attrs = node.getAttrs();
        Log.d(tag, "[GenXML] Number of Attributes " + attrs.size());
        if (attrs.size() == 0) {
            // Attributes
            xml.append(">");
            ln();
            return;
        }

        for (Attribute attr : attrs) {
            // next line. All attributes in different line
            ln();

            final String name;
            if (attr.getNamespace() == null) {
                name = attr.getName();
            } else {
                name = attr.getNamespace() + ":" + attr.getName();
            }

            xml.append(" " + name + "=\"" + attr.getValue() + "\"");
        }

        xml.append(">");
    }

    public void nodeValue(int lineNumber, String name, String value) {
        // TODO: handle Node value
    }

    public void endNode(Node node) {

        // TODO: Remove this case. Only for temp fix.
        // FIX line number problem.
        if (cl == node.getLinenumber())
            xml.append("\n");

        else if (cl < node.getLinenumber())
            ln();

        // TODO Auto-generated method stub
        if (currentNode.getName().equals(node.getName())) {
            // Add end tag "/>". Remove ">" and add "/>"
            //xml = xml.delete(xml.length() - 1, xml.length());
            int index = xml.lastIndexOf("\"");

            if (index != -1) {
                xml.delete(index + 1, xml.length());
                xml.append("/>");
            } else {
                xml.append("</" + node.getName() + ">");
            }
        } else {
            // there are child nodes
            xml.append("</" + node.getName() + ">");
        }
    }

    public void endDoc() throws Exception {
        // Generate the XML
        if (xmlFile != null && !xmlFile.isEmpty()) {
            FileOutputStream out = new FileOutputStream(xmlFile + ".xml");
            out.write(xml.toString().getBytes());
            out.flush();
            out.close();
        }
    }

    public String getXml() {
        return xml.toString();
    }

    private void ln() {
        cl++;
        xml.append("\n");
    }
}