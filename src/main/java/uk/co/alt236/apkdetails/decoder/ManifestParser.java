package uk.co.alt236.apkdetails.decoder;

import uk.co.alt236.apkdetails.decoder.abx.Android_BX2;
import uk.co.alt236.apkdetails.decoder.utils.Log;
import uk.co.alt236.apkdetails.repo.AndroidManifestRepository;
import uk.co.alt236.apkdetails.xml.XmlFormatter;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ManifestParser {
    private final int BUFFER = 2048;
    private final String TAG = getClass().getSimpleName();
    private final File apkFile;
    private final XmlFormatter xmlFormatter;


    public ManifestParser(File file) {
        this.apkFile = file;
        this.xmlFormatter = new XmlFormatter();
    }

    public AndroidManifestRepository parse() throws Exception {
        final byte[] binaryXMLManifest = getBinaryManifest(apkFile);

        final GenXML dataReceiver = new GenXML();
        final Android_BX2 abx2 = new Android_BX2(dataReceiver);

        abx2.parse(binaryXMLManifest);
        return new AndroidManifestRepository(xmlFormatter.format(dataReceiver.getXml()));
    }


    private byte[] getBinaryManifest(File file) throws IOException {
        final FileInputStream fin = new FileInputStream(file);
        final ZipInputStream zin = new ZipInputStream(new BufferedInputStream(fin));

        ZipEntry ze = null;
        BufferedOutputStream dest;

        byte[] retVal = null;
        while ((ze = zin.getNextEntry()) != null) {
            Log.d(TAG, "Zip entry: " + ze.getName() + " Size: " + ze.getSize());
            if (ze.getName().equalsIgnoreCase("AndroidManifest.xml")) {
                String zeName = ze.getName();
                String zeFolder = zeName;

                if (ze.isDirectory()) {
                    continue;
                }

                // Write Zip entry File to string
                int count;
                byte data[] = new byte[BUFFER];


                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//				FileOutputStream fos = new FileOutputStream(zeFile.getPath() + File.separator + zeName);
//				dest = new BufferedOutputStream(fos, BUFFER);

                while ((count = zin.read(data, 0, BUFFER)) != -1) {
                    baos.write(data, 0, count);
                    //dest.write(data, 0, count);
                }
                baos.flush();
                baos.close();

                retVal = baos.toByteArray();

                break;
            }
        }

        // Close Zip InputStream
        zin.close();
        fin.close();

        return retVal;
    }
}
