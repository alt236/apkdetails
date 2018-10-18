package uk.co.alt236.apkdetails.output.classlist;

import uk.co.alt236.apkdetails.output.classlist.graphml.GraphMlTreeAdapter;
import uk.co.alt236.apkdetails.output.classlist.tree.ClassTreeAdapter;
import uk.co.alt236.apkdetails.output.classlist.tree.DexNode;
import uk.co.alt236.apkdetails.output.classlist.tree.DexTree;
import uk.co.alt236.apkdetails.print.ClassListPrinter;
import uk.co.alt236.apkdetails.print.graphml.GraphMLPrinter;
import uk.co.alt236.apkdetails.print.tree.TreePrinter;
import uk.co.alt236.apkdetails.print.writer.StringCollector;
import uk.co.alt236.apkdetails.repo.dex.DexRepository;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;
import uk.co.alt236.apkdetails.tree.Node;
import uk.co.alt236.apkdetails.tree.Tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ClassListToTextMapper {

    private final DexRepository dexRepository;

    public ClassListToTextMapper(final DexRepository dexRepository) {
        this.dexRepository = dexRepository;
    }


    public String getClassList() {
        final StringCollector stringCollector = new StringCollector();
        final ClassListPrinter classListPrinter = new ClassListPrinter();

        final Collection<DexClass> classes = dexRepository.getAllClasses();
        final List<DexClass> sortedList = new ArrayList<>(classes);

        sortedList.sort(Comparator.comparing(DexClass::getType));

        classListPrinter.print(stringCollector, sortedList);
        return stringCollector.getCollectedString();
    }


    public String getClassTree() {
        final Tree<DexClass> tree = createTree(dexRepository);
        final TreePrinter<Node<DexClass>> treePrinter = new TreePrinter<>(new ClassTreeAdapter());
        final StringCollector stringCollector = new StringCollector();

        treePrinter.print(stringCollector, tree.getRoot());
        return stringCollector.getCollectedString();
    }


    public String getClassGraph() {

        final Tree<DexClass> tree = createTree(dexRepository);
        final StringCollector stringCollector = new StringCollector();
        final GraphMLPrinter<Node<DexClass>> graphMLPrinter = new GraphMLPrinter<>(new GraphMlTreeAdapter());

        graphMLPrinter.print(stringCollector, tree.getRoot());
        return stringCollector.getCollectedString();
    }


    private Tree<DexClass> createTree(final DexRepository dexRepository) {
        final Tree<DexClass> tree = new DexTree();
        final Collection<DexClass> classes = dexRepository.getAllClasses();
        final List<Node<DexClass>> dexNodes = classes.stream().map(DexNode::new).collect(Collectors.toList());
        tree.addChildren(dexNodes);

        return tree;
    }
}
