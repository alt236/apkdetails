package uk.co.alt236.apkdetails.model;

import uk.co.alt236.apkdetails.xml.AndroidXPathFactory;
import uk.co.alt236.apkdetails.xml.AndroidXmlDocument;

import java.util.Locale;

public class Manifest {
    private static final String NS_DECLARATION_TEMPLATE = "xmlns:%s=\"%s\"";
    private static final String NS_DECLARATION = String.format(
            Locale.US, NS_DECLARATION_TEMPLATE, AndroidXPathFactory.DEFAULT_NS_PREFIX, AndroidXPathFactory.NS_RESOURCES);
    
    private static final String NODE_MANIFEST = "manifest";
    private static final String NODE_APPLICATION = "application";
    private static final String NODE_ACTIVITY = "activity";
    private static final String NODE_ACTIVITY_ALIAS = "activity-alias";
    private static final String NODE_SERVICE = "service";
    private static final String NODE_RECEIVER = "receiver";
    private static final String NODE_PROVIDER = "provider";
    private static final String NODE_INTENT = "intent-filter";
    private static final String NODE_ACTION = "action";
    private static final String NODE_CATEGORY = "category";
    private static final String NODE_USES_SDK = "uses-sdk";
    private static final String NODE_PERMISSION = "permission";
    private static final String NODE_PERMISSION_TREE = "permission-tree";
    private static final String NODE_PERMISSION_GROUP = "permission-group";
    private static final String NODE_USES_PERMISSION = "uses-permission";
    private static final String NODE_INSTRUMENTATION = "instrumentation";
    private static final String NODE_USES_LIBRARY = "uses-library";
    private static final String NODE_SUPPORTS_SCREENS = "supports-screens";
    private static final String NODE_COMPATIBLE_SCREENS = "compatible-screens";
    private static final String NODE_USES_CONFIGURATION = "uses-configuration";
    private static final String NODE_USES_FEATURE = "uses-feature";
    private static final String NODE_METADATA = "meta-data";
    private static final String NODE_DATA = "data";
    private static final String NODE_GRANT_URI_PERMISSION = "grant-uri-permission";
    private static final String NODE_PATH_PERMISSION = "path-permission";
    private static final String NODE_SUPPORTS_GL_TEXTURE = "supports-gl-texture";
    private static final String ATTRIBUTE_PACKAGE = "package";
    private static final String ATTRIBUTE_VERSIONCODE = "versionCode";
    private static final String ATTRIBUTE_VERSIONNAME = "versionName";
    private static final String ATTRIBUTE_NAME = "name";
    private static final String ATTRIBUTE_REQUIRED = "required";
    private static final String ATTRIBUTE_GLESVERSION = "glEsVersion";
    private static final String ATTRIBUTE_PROCESS = "process";
    private static final String ATTRIBUTE_DEBUGGABLE = "debuggable";
    private static final String ATTRIBUTE_LABEL = "label";
    private static final String ATTRIBUTE_ICON = "icon";
    private static final String ATTRIBUTE_MIN_SDK_VERSION = "minSdkVersion";
    private static final String ATTRIBUTE_TARGET_SDK_VERSION = "targetSdkVersion";
    private static final String ATTRIBUTE_TARGET_PACKAGE = "targetPackage";
    private static final String ATTRIBUTE_TARGET_ACTIVITY = "targetActivity";
    private static final String ATTRIBUTE_MANAGE_SPACE_ACTIVITY = "manageSpaceActivity";
    private static final String ATTRIBUTE_EXPORTED = "exported";
    private static final String ATTRIBUTE_RESIZEABLE = "resizeable";
    private static final String ATTRIBUTE_ANYDENSITY = "anyDensity";
    private static final String ATTRIBUTE_SMALLSCREENS = "smallScreens";
    private static final String ATTRIBUTE_NORMALSCREENS = "normalScreens";
    private static final String ATTRIBUTE_LARGESCREENS = "largeScreens";
    private static final String ATTRIBUTE_REQ_5WAYNAV = "reqFiveWayNav";
    private static final String ATTRIBUTE_REQ_NAVIGATION = "reqNavigation";
    private static final String ATTRIBUTE_REQ_HARDKEYBOARD = "reqHardKeyboard";
    private static final String ATTRIBUTE_REQ_KEYBOARDTYPE = "reqKeyboardType";
    private static final String ATTRIBUTE_REQ_TOUCHSCREEN = "reqTouchScreen";
    private static final String ATTRIBUTE_THEME = "theme";
    private static final String ATTRIBUTE_BACKUP_AGENT = "backupAgent";
    private static final String ATTRIBUTE_PARENT_ACTIVITY_NAME = "parentActivityName";

    private final AndroidXmlDocument xmlDocument;

    public Manifest(final String xml) {
        this.xmlDocument = new AndroidXmlDocument(xml);
    }

    public String getXml() {
        return xmlDocument.getXml();
    }

    public String getApplicationId() {
        final String expression = "/" + NODE_MANIFEST +
                "/@" + ATTRIBUTE_PACKAGE;

        return xmlDocument.getStringValue(expression);
    }

    public String getVersionCode() {
        final String expression = getNamespacedRoot() +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_VERSIONCODE;

        return xmlDocument.getStringValue(expression);
    }


    public String getVersionName() {
        final String expression = getNamespacedRoot() +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_VERSIONNAME;

        return xmlDocument.getStringValue(expression);
    }


    public String getMinSdkVersion() {
        String expression = getNamespacedRoot() +
                "/" + NODE_USES_SDK +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_MIN_SDK_VERSION;

        return xmlDocument.getStringValue(expression);
    }

    public String getTargetSdkVersion() {
        String expression = getNamespacedRoot() +
                "/" + NODE_USES_SDK +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_TARGET_SDK_VERSION;

        return xmlDocument.getStringValue(expression);
    }

    public String isDebuggable() {
        final String expression = getNamespacedRoot() +
                "/" + NODE_APPLICATION +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_DEBUGGABLE;

        return xmlDocument.getStringValue(expression);
    }

    private String getNamespacedRoot() {
        return "/" + NODE_MANIFEST + "[@" + NS_DECLARATION + "]";
    }
}
