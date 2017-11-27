package uk.co.alt236.apkdetails.repo.architectures;

import uk.co.alt236.apkdetails.repo.common.Entry;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArchitectureRepository {
    private static final String JNI_DIRECTORY = "lib/";
    private final ZipContents zipContents;

    public ArchitectureRepository(ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    public List<Architecture> getJniArchitectures() {

        final Map<String, List<Entry>> archsAndFiles = getArchitecturesAndFiles();
        final List<Architecture> retVal = new ArrayList<>();
        for (final Map.Entry<String, List<Entry>> mapEntry : archsAndFiles.entrySet()) {
            retVal.add(new Architecture(mapEntry.getKey(), mapEntry.getValue()));
        }

        retVal.sort((o1, o2) -> o1.getName().compareTo(o2.toString()));
        return retVal;
    }


    private final Map<String, List<Entry>> getArchitecturesAndFiles() {
        final List<Entry> libFiles = zipContents
                .getEntries(entry -> entry.getName().startsWith(JNI_DIRECTORY));

        final Map<String, List<Entry>> retVal = new HashMap<>();

        for (final Entry entry : libFiles) {
            final String cleanName = entry.getName().substring(JNI_DIRECTORY.length(), entry.getName().length());

            if (!entry.isDirectory()) {
                final String archName = cleanName.substring(0, cleanName.indexOf("/"));

                if (!retVal.containsKey(archName)) {
                    retVal.put(archName, new ArrayList<>());
                }
                retVal.get(archName).add(entry);
            }
        }

        for (final List<Entry> entries : retVal.values()) {
            entries.sort((o1, o2) -> o1.getName().compareTo(o2.toString()));
        }

        return retVal;
    }
}
