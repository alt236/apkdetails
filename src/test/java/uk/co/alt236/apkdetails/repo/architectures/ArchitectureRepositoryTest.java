package uk.co.alt236.apkdetails.repo.architectures;

import org.junit.Test;
import org.mockito.Mockito;
import uk.co.alt236.apkdetails.repo.common.Entry;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class ArchitectureRepositoryTest {

    @Test
    public void shouldReturnEmptylistOnEmptyZip() {
        final ZipContents zipContents = Mockito.mock(ZipContents.class);
        when(zipContents.getEntries(Mockito.any())).thenReturn(Collections.emptyList());

        final ArchitectureRepository architectureRepository = new ArchitectureRepository(zipContents);
        assertEquals(0, architectureRepository.getJniArchitectures().size());
    }


    @Test
    public void shouldIgnoreFilesDirectlyUnderLibDir() {
        final ZipContents zipContents = Mockito.mock(ZipContents.class);
        final List<Entry> entries = Arrays.asList(
                crateEntry("lib/a"),
                crateEntry("lib/b")
        );

        when(zipContents.getEntries(Mockito.any())).thenReturn(entries);

        final ArchitectureRepository architectureRepository = new ArchitectureRepository(zipContents);
        assertEquals(0, architectureRepository.getJniArchitectures().size());
    }

    @Test
    public void shouldIncludeEmptyArchDirectories() {
        final ZipContents zipContents = Mockito.mock(ZipContents.class);
        final List<Entry> entries = Arrays.asList(
                crateEntry("lib/arch1/"),
                crateEntry("lib/b")
        );

        when(zipContents.getEntries(Mockito.any())).thenReturn(entries);

        final ArchitectureRepository architectureRepository = new ArchitectureRepository(zipContents);
        final List<Architecture> architectures = architectureRepository.getJniArchitectures();

        assertEquals(1, architectures.size());
        assertEquals("arch1", architectures.get(0).getName());
        assertEquals(0, architectures.get(0).getFiles().size());
    }

    @Test
    public void shouldFindFilesUnderAnArch() {
        final ZipContents zipContents = Mockito.mock(ZipContents.class);
        final List<Entry> entries = Arrays.asList(
                crateEntry("lib/arch1/abc"),
                crateEntry("lib/b")
        );

        when(zipContents.getEntries(Mockito.any())).thenReturn(entries);

        final ArchitectureRepository architectureRepository = new ArchitectureRepository(zipContents);
        final List<Architecture> architectures = architectureRepository.getJniArchitectures();
        assertEquals(1, architectures.size());

        final Architecture architecture = architectureRepository.getJniArchitectures().get(0);
        assertEquals("arch1", architecture.getName());
        assertEquals(1, architecture.getFiles().size());
        assertEquals("lib/arch1/abc", architecture.getFiles().get(0).getName());
    }

    private Entry crateEntry(final String path) {
        final Entry entry = Mockito.mock(Entry.class);
        when(entry.getName()).thenReturn(path);
        when(entry.isDirectory()).thenReturn(path.endsWith("/"));

        return entry;
    }
}