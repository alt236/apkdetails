/*
 * Copyright (C) 2012 Prasanta Paul, http://prasanta-paul.blogspot.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pras;

import com.pras.abx.Android_BX2;
import com.pras.utils.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * Unpackage APK file with readable class, resources and XML.
 * You can Read AndroidManifest.uk.co.alt236.apkdetails.xml from code using- android.content.pm.PackageManager.queryXX
 * TODO:
 * 1. Parse resources.arsc
 * 2. Fail free. It shouldn't halt once first error occures.
 *
 * @author Prasanta Paul
 */
class ApkExtractor {

    private final int BUFFER = 2048;
    private boolean debug = true;
    private final String tag = getClass().getSimpleName();
    private final ArrayList<String> xmlFiles = new ArrayList<>();
    private String dexFile = null;
    private String resFile = null;

    /*
     * Supported Argumenets
     * - args[0] - File name
     * - args[1] - Production or Debug mode
     * e.g.
     * - Debug
     * ApkExtractor "YP Mobile.apk" 1
     * - Production
     * ApkExtractor "YP Mobile.apk" 2
     */
    public static void main(String args[]) {
        try {
            ApkExtractor ex = new ApkExtractor();

            // TODO: remove hard coded file
            //ex.unZip("D:\\Workspace\\APKExtractor\\res\\Fragment.apk");
            //ex.unZip("E:\\Workspace3.7\\APKExtractor\\res\\Fragment.apk");
            // ex.unZip("E:\\Workspace3.7\\APKExtractor\\res\\YPmobile.apk");

            if (args == null || args.length == 0) {
                throw new Exception("Please mention APK file.\nUsage java -cp CLASSPATH com.pras.ApkExtractor test.apk");
            }

            String file = args[0];
            if (args.length > 1) {
                try {
                    System.out.println("Log Level: " + args[1]);
                    System.out.println("1- Debug, 2- Production. Debug is slow.");
                    int logLevel = Integer.parseInt(args[1]);

                    if (logLevel != Log.DEBUG_LEVEL && logLevel != Log.PRODUCTION_LEVEL)
                        throw new Exception("Unsupported loglevel " + logLevel + ". Supported values: Debug- 1, Production - 2");

                    Log.setLogLevel(logLevel);

                } catch (Exception e) {
                    throw new Exception("Incorrect Log Level. Please mention APK file.\nUsage java -cp CLASSPATH com.pras.ApkExtractor test.apk");
                }
            }

            System.out.println("Parsing data, please wait...");


            final ManifestParser manifestParser = new ManifestParser(new File(file));
            System.out.println(manifestParser.parse());

            // Unzip content
//			ex.unZip(file);
            // Parse Binary XML
//			ex.decodeBX();
            // Decode DEX file
//			ex.decodeDex();

            Log.exitLogger();
            System.out.println("Done!");
            // EXperimentel. Still working on it...
            //ex.decodeResource();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * APK files are Zip file. Using Java Zip util to decompress APK resources.
     * 1. Decompress
     * 2. Decode Android Binary XML (AndroidManifest and Layout files)
     * 3. Convert Dex/ODex files to Jar.
     *
     * @param apkFile
     * @throws Exception
     */
    public void unZip(String apkFile) throws Exception {
        Log.p(tag, apkFile);
        File file = new File(apkFile);

		/*
         * Create the Base Directory, whose name should be same as Zip file name.
		 * All decompressed contents will be placed under this folder.
		 */
        String apkFileName = file.getName();

        if (apkFileName.indexOf('.') != -1)
            apkFileName = apkFileName.substring(0, apkFileName.indexOf('.'));

        Log.d(tag, "Folder name: " + apkFileName);

        File extractFolder = new File((file.getParent() == null ? "" : file.getParent() + File.separator) + apkFileName);
        if (!extractFolder.exists())
            extractFolder.mkdir();

		/*
         * Read zip entries.
		 */
        FileInputStream fin = new FileInputStream(apkFile);
        ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fin));

		/*
         * Zip InputStream shifts its index to every Zip entry when getNextEntry() is called.
		 * If this method returns null, Zip InputStream reaches EOF.
		 */
        ZipEntry ze = null;
        BufferedOutputStream dest;

        while ((ze = zin.getNextEntry()) != null) {
            Log.d(tag, "Zip entry: " + ze.getName() + " Size: " + ze.getSize());
			/*
			 * Create decompressed file for each Zip entry. A Zip entry can be a file or directory.
			 * ASSUMPTION: APK Zip entry uses Unix style File seperator- "/"
			 *
			 * 1. Create the prefix Zip Entry folder, if it is not yet available
			 * 2. Create the individual Zip Entry file.
			 */
            String zeName = ze.getName();
            String zeFolder = zeName;

            if (ze.isDirectory()) {
                zeName = null; // Don't create  Zip Entry file
            } else {
                if (!zeName.contains("/")) // Zip entry uses "/"
                    zeFolder = null; // It is File. don't create Zip entry Folder
                else {
                    zeFolder = zeName.substring(0, zeName.lastIndexOf("/"));
                    zeName = zeName.substring(zeName.lastIndexOf("/") + 1);
                }
            }

            Log.d(tag, "zeFolder: " + zeFolder + " zeName: " + zeName);

            // Create Zip Entry Folder
            File zeFile = extractFolder;
            if (zeFolder != null) {
                zeFile = new File(extractFolder.getPath() + File.separator + zeFolder);
                if (!zeFile.exists())
                    zeFile.mkdirs();
            }

            // Create Zip Entry File
            if (zeName == null)
                continue;

            // Keep track of XML files, they are in Android Binary XML format
            if (zeName.endsWith(".uk.co.alt236.apkdetails.xml"))
                xmlFiles.add(zeFile.getPath() + File.separator + zeName);

            // Keep track of the Dex/ODex file. Need to convert to Jar
            if (zeName.endsWith(".dex") || zeName.endsWith(".odex"))
                dexFile = zeFile.getPath() + File.separator + zeName;

            // Keep track of Resources.arsc file- resources.arsc
            if (zeName.endsWith(".arsc"))
                resFile = zeFile.getPath() + File.separator + zeName;

            // Write Zip entry File to the disk
            int count;
            byte data[] = new byte[BUFFER];

            FileOutputStream fos = new FileOutputStream(zeFile.getPath() + File.separator + zeName);
            dest = new BufferedOutputStream(fos, BUFFER);

            while ((count = zin.read(data, 0, BUFFER)) != -1) {
                dest.write(data, 0, count);
            }
            dest.flush();
            dest.close();
        }

        // Close Zip InputStream
        zin.close();
        fin.close();
    }

    /**
     * Convert .dex/.odex file to .jar.
     *  .jar file will contain .class files which can be decompiled.
     *  Works with ICS APK.
     *
     * Dex2Jar Lib-
     * http://code.google.com/p/dex2jar
     *
     * Decompiler- JD GUI
     * http://java.decompiler.free.fr/?q=jdgui
     */
	
	/* Mhill - I don't need this for now...
	public void decodeDex() throws Exception {
		Log.p(tag, "Decode DEX/ODEX...");
		if(dexFile == null){
			Log.p("No .dex/.odex file. Skip decodeDex()");
			return;
		}
		String jarFile = dexFile + ".jar";
		// Read Dex file
		Dex2jar dj = Dex2jar.from(new File(dexFile));
		
		// Create Jar
		dj.to(jarFile);
		Log.p("Converted Dex/ODex to Jar.");
		Log.p("I'm Done! ....huh :-)");
	}
	*/

    /**
     * Decode binary XML files
     */
    public void decodeBX() throws Exception {
        Log.p(tag, "Decode Binary XML...");
        Log.d(tag, "Number of Binary XML files: " + xmlFiles.size());
        Log.d(tag, "-> " + xmlFiles);


        // Convert WBXML to XML
		/*
		 * aapt (Android Assents Packaging Tool) converts XML files to Android Binary XML. It is not same as
		 * WBXML format.
		 */
        for (String xmlFile : xmlFiles) {
            Log.p(tag, "XML File: " + xmlFile);

            // 23rd March, 2012. Prasanta.
            // Skip exception while parsing any file and complete the complete parsing cycle.
            Android_BX2 abx2;
            try {
                abx2 = new Android_BX2(new GenXML());
                abx2.parse(xmlFile);

            } catch (Exception ex) {
                Log.e(tag, "Fail to parse - " + xmlFile, ex);
            } finally {
                abx2 = null;
            }
        }
    }

    /**
     * TODO: more work to do. :-(
     *
     * @throws Exception
     */
    public void decodeResource() throws Exception {
        Android_BX2 abx = new Android_BX2(null);
        abx.parseResourceTable(resFile);
    }
}
