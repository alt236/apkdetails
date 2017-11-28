package uk.co.alt236.apkdetails.repo.dex;

import org.jf.dexlib2.iface.ClassDef;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

public class DexClassInfoTest {

    private DexClassInfo dexClassInfo;

    @Before
    public void setUp() {
        dexClassInfo = new DexClassInfo();
    }

    @Test
    public void shouldNotMatchOuterClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FragmentTransition;");
        assertFalse(dexClassInfo.isInnerClass(clazz));
    }

    @Test
    public void shouldNotMatchOuterClassEndingWithNumber() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FragmentTransition1;");
        assertFalse(dexClassInfo.isInnerClass(clazz));
    }

    @Test
    public void shouldMatchInnerClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FrameMetricsAggregator$FrameMetricsApi24Impl$1;");
        assertTrue(dexClassInfo.isInnerClass(clazz));
    }
}