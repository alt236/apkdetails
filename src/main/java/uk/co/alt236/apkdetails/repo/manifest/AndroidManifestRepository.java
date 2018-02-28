package uk.co.alt236.apkdetails.repo.manifest;

import uk.co.alt236.apkdetails.decoder.ManifestParser;
import uk.co.alt236.apkdetails.xml.AndroidXmlDocument;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AndroidManifestRepository {

    private final ManifestParser manifestParser;
    private final NodeParser nodeParser;
    private AndroidXmlDocument xmlDocument;

    public AndroidManifestRepository(final File apk) {
        this(new ManifestParser(apk));
    }

    public AndroidManifestRepository(final ManifestParser manifestParser) {
        this.manifestParser = manifestParser;
        this.nodeParser = new NodeParser();
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
        loadManifest();

        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_ACTIVITY;

        return nodeParser.getAndroidNamesOfNodes(items, expression);
    }


    public List<String> getServices() {
        loadManifest();

        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_SERVICE;

        return nodeParser.getAndroidNamesOfNodes(items, expression);
    }

    public List<String> getUsedPermissions() {
        loadManifest();

        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_USES_PERMISSION;

        return nodeParser.getAndroidNamesOfNodes(items, expression);
    }

    public List<String> getReceivers() {
        loadManifest();

        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_RECEIVER;

        return nodeParser.getAndroidNamesOfNodes(items, expression);
    }

    public List<String> getProviders() {
        loadManifest();

        final List<String> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_APPLICATION +
                "/" + Keys.NODE_PROVIDER;

        return nodeParser.getAndroidNamesOfNodes(items, expression);
    }

    public List<Requirable> getUsedFeatures() {
        loadManifest();

        final List<Requirable> items = new ArrayList<>();
        final String expression = "/" + Keys.NODE_MANIFEST +
                "/" + Keys.NODE_USES_FEATURE;

        return nodeParser.getRequirableItems(items, expression);
    }

    private synchronized void loadManifest() {
        if (xmlDocument == null) {
            try {
                xmlDocument = manifestParser.createXmlDocument();
                nodeParser.setXmlDocument(xmlDocument);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }
}
