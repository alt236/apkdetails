package uk.co.alt236.apkdetails.repo.dex.model;

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
    public void isInnerClassShouldNotMatchOuterClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FragmentTransition;");
        assertFalse(dexClassInfo.isInnerClass(clazz));
    }

    @Test
    public void isInnerClassShouldNotMatchOuterClassEndingWithNumber() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FragmentTransition1;");
        assertFalse(dexClassInfo.isInnerClass(clazz));
    }

    @Test
    public void isInnerClassShouldMatchInnerClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FrameMetricsAggregator$FrameMetricsApi24Impl$1;");
        assertTrue(dexClassInfo.isInnerClass(clazz));
    }

    @Test
    public void isLambdaShouldNotMatchOuterClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FragmentTransition;");
        assertFalse(dexClassInfo.isLambda(clazz));
    }

    @Test
    public void isLambdaShouldNotMatchOuterClassEndingWithNumber() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FragmentTransition1;");
        assertFalse(dexClassInfo.isLambda(clazz));
    }

    @Test
    public void isLambdaNotShouldMatchNonLambdaInnerClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FrameMetricsAggregator$FrameMetricsApi24Impl$1;");
        assertFalse(dexClassInfo.isLambda(clazz));
    }

    @Test
    public void isLambdaNotShouldMatchInnerClassCalledLambda() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FrameMetricsAggregator$Lambda;");
        assertFalse(dexClassInfo.isLambda(clazz));
    }

    @Test
    public void isLambdaNotShouldMatchInnerClassCalledLambdaWithAnonymousClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FrameMetricsAggregator$Lambda$0;");
        assertFalse(dexClassInfo.isLambda(clazz));
    }

    @Test
    public void isLambdaShouldMatchLambdaInnerClass() {
        final ClassDef clazz = Mockito.mock(ClassDef.class);
        when(clazz.getType()).thenReturn("Landroid/support/v4/app/FrameMetricsAggregator$$Lambda$0;");
        assertTrue(dexClassInfo.isLambda(clazz));
    }
}