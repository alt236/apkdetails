package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.model.Resources;
import uk.co.alt236.apkdetails.model.common.ZipContents;
import uk.co.alt236.apkdetails.print.section.SectionedKvPrinter;

public class ResourcesOutput implements Output {

    private final ZipContents zipContents;

    public ResourcesOutput(final ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    @Override
    public void output(SectionedKvPrinter printer) {
        final Resources fileInfo = new Resources(zipContents);

        printer.add("Resources");
        printer.startKeyValueSection();
        printer.addKv("Res Raw", fileInfo.getNumberOfRawRes());
        printer.addKv("Res Layouts", fileInfo.getNumberOfLayoutRes());
        printer.addKv("Res Drawables", fileInfo.getNumberOfDrawableRes());
        printer.addKv("Assets", fileInfo.getNumberOfAssets());
        printer.endKeyValueSection();
    }
}
