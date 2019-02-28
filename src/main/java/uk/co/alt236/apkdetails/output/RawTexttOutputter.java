package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.cli.CommandLineOptions;
import uk.co.alt236.apkdetails.output.classlist.ClassListToTextMapper;
import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.print.section.OutputCollector;
import uk.co.alt236.apkdetails.repo.dex.DexRepository;
import uk.co.alt236.apkdetails.repo.manifest.AndroidManifestRepository;
import uk.co.alt236.apkdetails.util.Colorizer;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

public class RawTexttOutputter {

    public RawTexttOutputter(FileSizeFormatter fileSizeFormatter, Colorizer colorizer) {

    }


    public void doOutput(OutputPathFactory outputPathFactory,
                         CommandLineOptions cli,
                         DexRepository dexRepository,
                         AndroidManifestRepository manifestRepository) {

        final ClassListToTextMapper classListToTextMapper = new ClassListToTextMapper(dexRepository, cli.isVerbose());

        final OutputCollector collector = new OutputCollector();

        collector.startKeyValueSection();
        collector.addKv("Path", outputPathFactory.getApk().toString());
        collector.endKeyValueSection();

        if (cli.isPrintClassTree()) {
            collector.addSectionLine();
            collector.add(classListToTextMapper.getClassTree());
        }

        if (cli.isPrintClassList()) {
            collector.addSectionLine();
            collector.add(classListToTextMapper.getClassList());
        }

        if (cli.isPrintClassGraph()) {
            collector.addSectionLine();
            collector.add(classListToTextMapper.getClassGraph());
        }

        if (cli.isPrintManifest()) {
            collector.addSectionLine();
            collector.add(manifestRepository.getXml());
        }

        Logger.get().out(collector.toString());
    }
}
