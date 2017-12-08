package uk.co.alt236.apkdetails.repo.manifest;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.xml.AndroidXmlDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AndroidManifestRepository {

    private final ManifestParser manifestParser;
    private AndroidXmlDocument xmlDocument;

    public AndroidManifestRepository(final File apk) {
        this(new ManifestParser(apk));
    }

    public AndroidManifestRepository(final ManifestParser manifestParser) {
        this.manifestParser = manifestParser;
    }

    public String getXml() {
        loadManifest();
        return xmlDocument.getXml();
    }

    public String getApplicationId() {
        loadManifest();

        final String expression = "/" + Keys.NODE_MANIFEST +
                "/@" + Keys.ATTRIBUTE_PACKAGE;

        return xmlDocument.getStringValue(expression);
    }

    public long getVersionCode() {
        loadManifest();

        final String expression = "/" + Keys.NODE_MANIFEST +
                "/@" + Keys.ATTRIBUTE_VERSIONCODE;

        return xmlDocument.getLongValue(expression);
    }


    public String getVersionName() {
        loadManifest();

        final String expression = "/" + Keys.NODE_MANIFEST +
                "/@" + Keys.ATTRIBUTE_VERSIONNAME;

        return xmlDocument.getStringValue(expression);
    }


    public int getMinSdkVersion() {
        loadManifest();

        String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_USES_SDK +
                "/@" + Keys.ATTRIBUTE_MIN_SDK_VERSION;

        return xmlDocument.getIntValue(expression);
    }

    public int getPlatformBuildSdkVersion() {
        loadManifest();

        String expression = "/" + Keys.NODE_MANIFEST +
                "/@" + Keys.ATTRIBUTE_PLATFORM_BUILD_SDK_VERSION;

        return xmlDocument.getIntValue(expression);
    }

    public int getTargetSdkVersion() {
        loadManifest();

        String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_USES_SDK +
                "/@" + Keys.ATTRIBUTE_TARGET_SDK_VERSION;

        return xmlDocument.getIntValue(expression);
    }

    public boolean isDebuggable() {
        loadManifest();

        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/@" + Keys.ATTRIBUTE_DEBUGGABLE;

        return xmlDocument.getBooleanValue(expression);
    }

    public List<String> getActivities() {
        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_ACTIVITY;

        return getAndroidNamesOfNodes(items, expression);
    }


    public List<String> getServices() {
        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_SERVICE;

        return getAndroidNamesOfNodes(items, expression);
    }

    public List<String> getUsedPermissions() {
        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_USES_PERMISSION;

        return getAndroidNamesOfNodes(items, expression);
    }

    public List<String> getReceivers() {
        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_RECEIVER;

        return getAndroidNamesOfNodes(items, expression);
    }

    public List<String> getProviders() {
        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_PROVIDER;

        return getAndroidNamesOfNodes(items, expression);
    }


    private synchronized void loadManifest() {
        if (xmlDocument == null) {
            try {
                xmlDocument = manifestParser.createXmlDocument();
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private List<String> getAndroidNamesOfNodes(List<String> items, String expression) {
        loadManifest();
        final NodeList nodeList = xmlDocument.getNodes(expression);
        for (int i = 0; i < nodeList.getLength(); i++) {
            final Node node = nodeList.item(i);

            final Node nameNode;

            if (node.getAttributes().getNamedItem("android:name") != null) {
                nameNode = node.getAttributes().getNamedItem("android:name");
            } else {
                nameNode = node.getAttributes().getNamedItem("name");
            }

            items.add(nameNode.getNodeValue());
        }

        Collections.sort(items);
        return items;
    }
}
