package uk.co.alt236.apkdetails.output;

import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.output.tree.ClassTreeAdapter;
import uk.co.alt236.apkdetails.output.tree.DexNode;
import uk.co.alt236.apkdetails.output.tree.DexTree;
import uk.co.alt236.apkdetails.print.ClassListPrinter;
import uk.co.alt236.apkdetails.print.file.FileWriter;
import uk.co.alt236.apkdetails.print.tree.TreePrinter;
import uk.co.alt236.apkdetails.repo.common.ZipContents;
import uk.co.alt236.apkdetails.repo.dex.DexRepository;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;
import uk.co.alt236.apkdetails.repo.manifest.AndroidManifestRepository;
import uk.co.alt236.apkdetails.tree.Node;
import uk.co.alt236.apkdetails.tree.Tree;
import uk.co.alt236.apkdetails.util.Colorizer;
import uk.co.alt236.apkdetails.util.FileSizeFormatter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FilesOutputter {

    private final FileSizeFormatter fileSizeFormatter;
    private final Colorizer colorizer;

    public FilesOutputter(final FileSizeFormatter fileSizeFormatter,
                          final Colorizer colorizer) {
        this.fileSizeFormatter = fileSizeFormatter;
        this.colorizer = colorizer;
    }

    public void doOutput(OutputPathFactory outputPathFactory,
                         ZipContents zipContents,
                         AndroidManifestRepository manifestRepository,
                         DexRepository dexRepository,
                         boolean verbose) {

        saveManifest(outputPathFactory, manifestRepository);
        saveClassTree(outputPathFactory, dexRepository);
        saveClassList(outputPathFactory, dexRepository);
    }


    private void saveManifest(final OutputPathFactory outputPathFactory,
                              final AndroidManifestRepository repository) {
        final File outputFile = outputPathFactory.getManifestFile();
        if (outputFile != null) {
            Logger.get().out("Manifest File: " + outputFile);
            FileWriter writer = new FileWriter(outputFile);
            String xml;

            try {
                xml = repository.getXml();
            } catch (IllegalStateException e) {
                xml = e.getMessage();
            }

            writer.outln(xml);
            writer.close();
        }
    }

    private void saveClassTree(OutputPathFactory outputPathFactory,
                               DexRepository dexRepository) {

        final File outputFile = outputPathFactory.getClassTreeFile();
        if (outputFile != null) {
            Logger.get().out("Class Tree file: " + outputFile);
            final FileWriter fileWriter = new FileWriter(outputFile);

            final Tree<DexClass> tree = new DexTree();
            final Collection<DexClass> classes = dexRepository.getAllClasses();
            final List<Node<DexClass>> dexNodes = classes.stream().map(DexNode::new).collect(Collectors.toList());
            tree.addChildren(dexNodes);

            final TreePrinter<Node<DexClass>> treePrinter = new TreePrinter<>(new ClassTreeAdapter());
            treePrinter.print(fileWriter, tree.getRoot());
        }
    }

    private void saveClassList(OutputPathFactory outputPathFactory,
                               DexRepository dexRepository) {

        final File outputFile = outputPathFactory.getClassListFile();
        if (outputFile != null) {
            Logger.get().out("Class List file: " + outputFile);
            final FileWriter fileWriter = new FileWriter(outputFile);
            final ClassListPrinter classListPrinter = new ClassListPrinter();

            final Collection<DexClass> classes = dexRepository.getAllClasses();

            final List<DexClass> sortedList = new ArrayList<>(classes);
            sortedList.sort(Comparator.comparing(DexClass::getType));

            classListPrinter.print(fileWriter, sortedList);
        }
    }

}
