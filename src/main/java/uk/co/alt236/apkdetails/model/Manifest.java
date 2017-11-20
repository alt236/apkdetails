package uk.co.alt236.apkdetails.model;

import uk.co.alt236.apkdetails.xml.AndroidXPathFactory;
import uk.co.alt236.apkdetails.xml.AndroidXmlDocument;

public class Manifest {
    public static final String NODE_MANIFEST = "manifest";
    public static final String NODE_APPLICATION = "application";
    public static final String NODE_ACTIVITY = "activity";
    public static final String NODE_ACTIVITY_ALIAS = "activity-alias";
    public static final String NODE_SERVICE = "service";
    public static final String NODE_RECEIVER = "receiver";
    public static final String NODE_PROVIDER = "provider";
    public static final String NODE_INTENT = "intent-filter";
    public static final String NODE_ACTION = "action";
    public static final String NODE_CATEGORY = "category";
    public static final String NODE_USES_SDK = "uses-sdk";
    public static final String NODE_PERMISSION = "permission";
    public static final String NODE_PERMISSION_TREE = "permission-tree";
    public static final String NODE_PERMISSION_GROUP = "permission-group";
    public static final String NODE_USES_PERMISSION = "uses-permission";
    public static final String NODE_INSTRUMENTATION = "instrumentation";
    public static final String NODE_USES_LIBRARY = "uses-library";
    public static final String NODE_SUPPORTS_SCREENS = "supports-screens";
    public static final String NODE_COMPATIBLE_SCREENS = "compatible-screens";
    public static final String NODE_USES_CONFIGURATION = "uses-configuration";
    public static final String NODE_USES_FEATURE = "uses-feature";
    public static final String NODE_METADATA = "meta-data";
    public static final String NODE_DATA = "data";
    public static final String NODE_GRANT_URI_PERMISSION = "grant-uri-permission";
    public static final String NODE_PATH_PERMISSION = "path-permission";
    public static final String NODE_SUPPORTS_GL_TEXTURE = "supports-gl-texture";
    public static final String ATTRIBUTE_PACKAGE = "package";
    public static final String ATTRIBUTE_VERSIONCODE = "versionCode";
    public static final String ATTRIBUTE_VERSIONNAME = "versionName";
    public static final String ATTRIBUTE_NAME = "name";
    public static final String ATTRIBUTE_REQUIRED = "required";
    public static final String ATTRIBUTE_GLESVERSION = "glEsVersion";
    public static final String ATTRIBUTE_PROCESS = "process";
    public static final String ATTRIBUTE_DEBUGGABLE = "debuggable";
    public static final String ATTRIBUTE_LABEL = "label";
    public static final String ATTRIBUTE_ICON = "icon";
    public static final String ATTRIBUTE_MIN_SDK_VERSION = "minSdkVersion";
    public static final String ATTRIBUTE_TARGET_SDK_VERSION = "targetSdkVersion";
    public static final String ATTRIBUTE_TARGET_PACKAGE = "targetPackage";
    public static final String ATTRIBUTE_TARGET_ACTIVITY = "targetActivity";
    public static final String ATTRIBUTE_MANAGE_SPACE_ACTIVITY = "manageSpaceActivity";
    public static final String ATTRIBUTE_EXPORTED = "exported";
    public static final String ATTRIBUTE_RESIZEABLE = "resizeable";
    public static final String ATTRIBUTE_ANYDENSITY = "anyDensity";
    public static final String ATTRIBUTE_SMALLSCREENS = "smallScreens";
    public static final String ATTRIBUTE_NORMALSCREENS = "normalScreens";
    public static final String ATTRIBUTE_LARGESCREENS = "largeScreens";
    public static final String ATTRIBUTE_REQ_5WAYNAV = "reqFiveWayNav";
    public static final String ATTRIBUTE_REQ_NAVIGATION = "reqNavigation";
    public static final String ATTRIBUTE_REQ_HARDKEYBOARD = "reqHardKeyboard";
    public static final String ATTRIBUTE_REQ_KEYBOARDTYPE = "reqKeyboardType";
    public static final String ATTRIBUTE_REQ_TOUCHSCREEN = "reqTouchScreen";
    public static final String ATTRIBUTE_THEME = "theme";
    public static final String ATTRIBUTE_BACKUP_AGENT = "backupAgent";
    public static final String ATTRIBUTE_PARENT_ACTIVITY_NAME = "parentActivityName";

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
        final String expression = "/" + NODE_MANIFEST +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_VERSIONCODE;

        return xmlDocument.getStringValue(expression);
    }


    public String getVersionName() {
        final String expression = "/" + NODE_MANIFEST +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_VERSIONNAME;

        return xmlDocument.getStringValue(expression);
    }


    public String getMinSdkVersion() {
        String expression = "/" + NODE_MANIFEST +
                "/" + NODE_USES_SDK +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_MIN_SDK_VERSION;

        return xmlDocument.getStringValue(expression);
    }

    public String getTargetSdkVersion() {
        String expression = "/" + NODE_MANIFEST +
                "/" + NODE_USES_SDK +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_TARGET_SDK_VERSION;

        return xmlDocument.getStringValue(expression);
    }

    public String isDebuggable() {
        final String expression = "/" + NODE_MANIFEST +
                "/" + NODE_APPLICATION +
                "/@" + AndroidXPathFactory.DEFAULT_NS_PREFIX +
                ":" + ATTRIBUTE_DEBUGGABLE;

        return xmlDocument.getStringValue(expression);
    }
}
