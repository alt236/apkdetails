package parser.arsc;


import parser.arsc.ARSCFile.Asset;
import parser.arsc.ARSCFile.Content;
import parser.arsc.ARSCFile.Pkge;
import parser.axml.res.IntReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

public class ARSCParser {

    private final static int ENTRY_FLAG_COMPLEX = 0x01;

    public String parser(File file, int id) {
        ZipFile zip = null;
        try {
            zip = new ZipFile(file);
            InputStream arsc = zip.getInputStream(zip.getEntry("resources.arsc"));
            return parser(arsc, id);
        } catch (Exception e) {
            return null;
        } finally {
            if (zip != null) {
                try {
                    zip.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public String parser(byte[] arsc, int id) {
        String result = null;
        try {
            result = parser(new ByteArrayInputStream(arsc), id);
        } catch (IOException e) {
        }
        return result;
    }

    public String parser(InputStream arscs, int id) throws IOException {
        ARSCFile resources = new ARSCFile(new IntReader(arscs, false));
        return getValue(resources, id);
    }

    @SuppressWarnings("unused")
    private String getValue(ARSCFile resources, int id) {
        /**
         * android的id组成规则如下： package_id = id >> 24; assets_id = (id >> 16) & 
         * 0x000000ff; entry = id & 0x0000ffff; 
         */
        String result = null;
        int pId = id >> 24; // package_id 
        int aId = (id >> 16) & 0x000000ff; // assets_id 
        int entry = id & 0x0000ffff; // entry 
        for (Pkge pkg : resources.pkges) {
            if (pId != pkg.id)
                continue;
            for (Asset as : pkg.assets) {
                if (as.id != aId)
                    continue;
                for (Content content : as.contents) {
                    int offset = content.offsets[entry];
                    if (offset == -1)
                        continue;
                    offset /= 4;
                    int type = content.data[offset++];
                    int specid = content.data[offset++];
                    int valueType;
                    int dataId;
                    if ((type & ENTRY_FLAG_COMPLEX) == 0) {
                        valueType = content.data[offset++];
                        dataId = content.data[offset++];
                    } else {
                        int parentId = content.data[offset++];
                        int cout = content.data[offset++];
                        if (cout < 1)
                            return result;
                        // 只取第一组数据，觉大大大大多数情况cout==1 
                        valueType = content.data[offset++];
                        dataId = content.data[offset++];
                    }
                    if (((valueType >> 24) & 0x00ff) == 3) {
                        result = resources.strings.getHTML(dataId);
                    } else {
                        result = "0x" + Integer.toHexString(id);
                    }
                    return result;
                }
            }
        }
        return result;
    }

}