package uk.co.alt236.apkdetails.output.sections;

import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.ResourcesRepository;
import uk.co.alt236.apkdetails.repo.common.ZipContents;

public class ResourcesOutput implements Output {

    private final ZipContents zipContents;

    public ResourcesOutput(final ZipContents zipContents) {
        this.zipContents = zipContents;
    }

    @Override
    public void output(OutputCollector printer) {
        final ResourcesRepository fileInfo = new ResourcesRepository(zipContents);

        printer.add("Resources");
        printer.startKeyValueSection();
        printer.addKv("Res Raw", fileInfo.getNumberOfRawRes());
        printer.addKv("Res Layouts", fileInfo.getNumberOfLayoutRes());
        printer.addKv("Res Drawables", fileInfo.getNumberOfDrawableRes());
        printer.addKv("Assets", fileInfo.getNumberOfAssets());
        printer.endKeyValueSection();
    }

    @Override
    public OutputType getOutputType() {
        return OutputType.RESOURCES;
    }
}
