/*
 * Copyright 2008 Android4ME 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at 
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0 
 * 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, 
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
 * See the License for the specific language governing permissions and 
 * limitations under the License. 
 */
package parser.arsc;

import parser.axml.res.IntReader;
import parser.axml.res.StringBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 该class用于解析文件，
 */
public class ARSCFile {

    private static final int
            ARSC_CHUNK_TYPE = 0x000C0002,
            PACKAGE_CHUNK_TYPE = 0x011c0200,
            ASSET_CHUNK_TYPE = 0x00000202,
            CONTENT_CHUNK_TYPE = 0x00000201;
    public StringBlock strings;
    public Pkge[] pkges;

    public ARSCFile(IntReader reader) throws IOException {
        int arsc_type = reader.readInt();
        if (arsc_type != ARSC_CHUNK_TYPE)
            throw new IOException("Expected chunk of type 0x"
                    + Integer.toHexString(ARSC_CHUNK_TYPE) + ", read 0x"
                    + Integer.toHexString(arsc_type) + "."); 
        /* size */
        reader.readInt();
        int pkgCount = reader.readInt();
        try {
            strings = new StringBlock(reader);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        pkges = new Pkge[pkgCount];
        int pkgType = reader.readInt();
        //&& pkgType != 0x1200200
        if (pkgType != PACKAGE_CHUNK_TYPE)
            throw new IOException("Expected chunk of type 0x"
                    + Integer.toHexString(PACKAGE_CHUNK_TYPE) + ", read 0x"
                    + Integer.toHexString(pkgType) + ". Package count: " + pkgCount);
        for (int i = 0; i < pkgCount; i++) {
            pkges[i] = readPackage(reader);
        }
    }

    // ///////////////////////////////// data 

    private static Pkge readPackage(IntReader reader) throws IOException {
        Pkge pkge = new Pkge(); 
        /* size */
        reader.skipInt();
        pkge.id = reader.readInt();
        {
            final int nameLength = 128;
            StringBuilder name = new StringBuilder(16);
            int i = 0;
            for (; i != nameLength; ) {
                ++i;
                int ch = reader.readShort();
                if (ch == 0) {
                    break;
                }
                name.append((char) ch);
            }
            reader.skip((nameLength - i) * 2);
            reader.skip((nameLength * 2) % 4);
            pkge.name = name.toString();
        }

        System.out.println(pkge.name);

        /* signature? */
        reader.skipInt();
        int assetCount = reader.readInt(); 
        /* idNamesOffset */
        reader.skipInt(); 
        /* idNamesCount */
        reader.skipInt();

        pkge.assetNames = new StringBlock(reader);
        pkge.resourceNames = new StringBlock(reader);
        pkge.assets = new Asset[assetCount];

        int assetType = reader.readInt() & 0x0000ffff;
        for (int i = 0; i < assetCount; i++) {
            pkge.assets[i] = readAsset(reader, assetType);
        }
        return pkge;
    }

    private static Asset readAsset(IntReader reader, int type)
            throws IOException {
        if (type != ASSET_CHUNK_TYPE)
            throw new IOException("asset 类型不匹配！"); 
        /* chunkSize */
        reader.skipInt();
        Asset asset = new Asset();
        asset.id = reader.readInt();
        int count = reader.readInt();
        asset.flags = reader.readIntArray(count);
        asset.contents = readContents(reader);
        return asset;
    }

    // ///////////////////////////////// creator 

    private static List<Content> readContents(IntReader reader)
            throws IOException {
        List<Content> contents = new ArrayList<Content>();
        while (reader.available() > 0) {
            int type = reader.readInt() & 0x0000ffff;
            if (type == CONTENT_CHUNK_TYPE) {
            } else if (type == ASSET_CHUNK_TYPE || type == (PACKAGE_CHUNK_TYPE & 0x0000ffff)) {
                return contents;
            } else {
                throw new IOException("content 类型不匹配 type="
                        + Integer.toHexString(type));
            }
            int chunkSize = reader.readInt();
            Content content = new Content();
            content.id = reader.readInt();
            int offsetCount = reader.readInt();
            int dataOffset = reader.readInt();
            int size = reader.readInt(); 
            /* skip Configuartion */
            reader.skip(size - 4);
            content.offsets = reader.readIntArray(offsetCount);
            int dataSize = (chunkSize - dataOffset);
            if ((dataSize % 4) != 0) {
                throw new IOException("Content data size (" + dataSize
                        + ") is not multiple of 4.");
            }
            content.data = reader.readIntArray(dataSize / 4);
            contents.add(content);
        }
        return contents;
    }

    public static class Pkge {
        public int id;
        public String name;
        public StringBlock resourceNames;
        public StringBlock assetNames;
        public Asset[] assets;
    }

    /**
     * 'id'-1 gives asset name index. 'flags' values are essentially
     * changingConfiguration values. 'flags.length' is total resource count in
     * asset.
     */
    public static class Asset {
        public int id;
        public int[] flags;
        public List<Content> contents;
    }

    public static class Content {
        public Configuration configuration;
        public int id;
        public int[] offsets;
        public int[] data;
    }

    /**
     * int values are interpreted according to
     * android.content.res.Configuration.
     */
    public static class Configuration {
        public String language;
        public String country;
        public int orientation;
        public int touchscreen;
        public int keyboard;
        public int keyboardHidden;
        public int navigation;
        public int screenWidth;
        public int screenHeight;
    }

}