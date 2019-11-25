package uk.co.alt236.apkdetails.output;

import org.jf.dexlib2.dexbacked.DexBackedField;
import org.jf.dexlib2.dexbacked.DexBackedMethod;
import org.jf.dexlib2.iface.MethodParameter;
import uk.co.alt236.apkdetails.output.loging.Logger;
import uk.co.alt236.apkdetails.print.writer.FileWriter;
import uk.co.alt236.apkdetails.repo.dex.DexRepository;
import uk.co.alt236.apkdetails.repo.dex.model.DexClass;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.regex.Pattern;

public class PlantUmlOutputter {

    public void doOutput(OutputPathFactory outputPathFactory,
                         DexRepository dexRepository,
                         String packagePrefix) {

        final Collection<DexClass> filteredClasses = getFilteredClasses(dexRepository, packagePrefix);

        int countProcessed = 0;
        int countSkipped = 0;
        for (DexClass dexClass : filteredClasses) {
            if (isClassEligible(dexClass)) {
                processClass(outputPathFactory, dexClass);
                countProcessed++;
            } else {
                countSkipped++;
            }
        }
        Logger.get().out(String.format("Processed %d and skipped %d classes.", countProcessed, countSkipped));
    }

    private boolean isClassEligible(DexClass dexClass) {
        if (dexClass.getSimpleName().endsWith("_Factory")) {
            return false;
        } else if (dexClass.getSimpleName().startsWith("R$")) {
            return false;
        } else if (dexClass.getSimpleName().endsWith("_MembersInjector")) {
            return false;
        } else if (dexClass.getSimpleName().contains("_Impl$")) {
            return false;
        } else if (dexClass.getSimpleName().endsWith("_ViewBinding")) {
            return false;
        } else if (dexClass.isLambda()) {
            return false;
        } else if (dexClass.getSimpleName().endsWith("$WhenMappings")) {
            return false;
        } else if (dexClass.getSimpleName().endsWith("$1")) {
            return false;
        } else if (dexClass.getSimpleName().startsWith("-$$")) {
            return false;
        } else if (dexClass.getSimpleName().startsWith("R.")) {
            return false;
        } else if (dexClass.getSimpleName().startsWith("BuildConfig")) {
            return false;
        } else {
            return true;
        }
    }

    private void processClass(OutputPathFactory outputPathFactory, final DexClass dexClass) {

        final StringBuilder sb = new StringBuilder();
        final StringBuilder mb = new StringBuilder();

        mb.append("# ").append(dexClass.getSimpleName()).append("\n");

        sb.append("@startuml");
        sb.append("\n");
        sb.append("\t")
                .append(getClassModifier(dexClass))
                .append(" ")
                .append(dexClass.getPackageName().toString())
                .append(".")
                .append(dexClass.getSimpleName())
                .append(" {");

        final StringBuilder fieldEntry = new StringBuilder();

        List<DexBackedField> fields = new ArrayList<>();


        dexClass.getDexBackedClassDef().getFields().forEach((Consumer<DexBackedField>) dexBackedField -> {
            if (!Modifier.isVolatile(dexBackedField.accessFlags)
                    && !dexBackedField.getName().startsWith("_")
                    && !dexBackedField.getName().startsWith("<")) {
                fields.add(dexBackedField);
            }
        });
        fields.sort(Comparator.comparingInt(DexBackedField::getAccessFlags));

        fields.forEach(dexBackedField -> {
            sb.append("\n\t\t")
                    .append(getStaticPrefix(dexBackedField.accessFlags))
                    .append(getVisibilityPrefix(dexBackedField.accessFlags))
                    .append(" ")
                    .append(dexBackedField.getName())
                    .append(" : ")
                    .append(getSanitisedFieldType(dexBackedField.getType()));

            fieldEntry.append("+ ")
                    .append(getMarkdownStaticPrefix(dexBackedField.accessFlags))
                    .append("**")
                    .append(dexBackedField.getName())
                    .append("**")
                    .append(" : ")
                    .append(getSanitisedFieldType(dexBackedField.getType()))
                    .append("  \n");
        });

        mb.append("## Fields")
                .append("\n")
                .append(fieldEntry.toString().trim())
                .append("\n");


        mb.append("## Methods")
                .append("\n");

        sb.append("\n");

        List<DexBackedMethod> methods = new ArrayList<>();
        dexClass.getDexBackedClassDef().getMethods().forEach((Consumer<DexBackedMethod>) dexBackedMethod ->
                {
                    if (!Modifier.isVolatile(dexBackedMethod.accessFlags)
                            && !dexBackedMethod.getName().startsWith("<")
                            && !dexBackedMethod.getName().startsWith("_")
                            && !dexBackedMethod.getName().startsWith("lambda"))
                        methods.add(dexBackedMethod);
                }
        );
        methods.sort(Comparator.comparingInt(dexBackedMethod2 -> dexBackedMethod2.accessFlags));


        methods.forEach(dexBackedMethod -> {
            sb.append("\n\t\t")
                    .append(getStaticPrefix(dexBackedMethod.accessFlags))
                    .append(getVisibilityPrefix(dexBackedMethod.accessFlags))
                    .append(" ")
                    .append(dexBackedMethod.getName())
                    .append("()");

            mb.append("\n\n")
                    .append("**")
                    .append(getMarkdownVisibilityPrefix(dexBackedMethod.accessFlags))
                    .append(getMarkdownStaticPrefix(dexBackedMethod.accessFlags))
                    .append(dexBackedMethod.getName())
                    .append("()")
                    .append("**  ")
                    .append("\n")
                    .append("* **Input**  ");

            if (dexBackedMethod.getParameters().isEmpty()) {
                mb.append("\n   - N/A  ");
            } else {
                dexBackedMethod.getParameters().forEach((Consumer<MethodParameter>) methodParameter -> mb
                        .append("\n   - ")
                        .append(methodParameter.getName())
                        .append(" : ")
                        .append(getSanitisedFieldType(methodParameter.getType()))
                        .append("  "));
            }

            mb.append("\n")
                    .append("* **Output**  \n   - ")
                    .append(getSanitisedFieldType(dexBackedMethod.getReturnType()))
                    .append("  ");

        });
        sb.append("\n\t}");
        sb.append("\n@enduml");


        //Logger.get().out(sb.toString());
        //Logger.get().out(mb.toString());

        final File plantUmlFile = outputPathFactory.getPlantUmlFile(dexClass);
        FileWriter plantUmlWriter = new FileWriter(plantUmlFile);
        plantUmlWriter.outln(sb.toString());
        plantUmlWriter.close();

        final File markdownFile = outputPathFactory.getMarkdownFile(dexClass);
        FileWriter markdownWriter = new FileWriter(markdownFile);
        markdownWriter.outln(mb.toString());
        markdownWriter.close();


    }

    private String getClassModifier(DexClass dexClass) {
        final String prefix;
        if (Objects.equals(dexClass.getDexBackedClassDef().getSuperclass(), "Ljava/lang/Enum;")) {
            prefix = "enum";
        } else if (Modifier.isInterface(dexClass.getDexBackedClassDef().getAccessFlags())) {
            prefix = "interface";
        } else if (Modifier.isAbstract(dexClass.getDexBackedClassDef().getAccessFlags())) {
            prefix = "abstract class";
        } else {
            prefix = "class";
        }
        return prefix;
    }

    private String getStaticPrefix(int accessFlags) {
        final String prefix;
        if (Modifier.isStatic(accessFlags)) {
            prefix = "{static} ";
        } else if (Modifier.isAbstract(accessFlags)) {
            prefix = "{abstract} ";
        } else {
            prefix = "";
        }
        return prefix;
    }

    private String getMarkdownStaticPrefix(int accessFlags) {
        final String prefix;
        if (Modifier.isStatic(accessFlags)) {
            prefix = "static ";
        } else if (Modifier.isInterface(accessFlags)) {
            prefix = "interface ";
        } else if (Modifier.isAbstract(accessFlags)) {
            prefix = "abstract ";
        } else {
            prefix = "";
        }
        return prefix;
    }

    private String getVisibilityPrefix(int accessFlags) {
        String visibilityPrefix;
        if (Modifier.isPublic(accessFlags)) {
            visibilityPrefix = "+";
        } else if (Modifier.isProtected(accessFlags)) {
            visibilityPrefix = "#";
        } else if (Modifier.isPrivate(accessFlags)) {
            visibilityPrefix = "-";
        } else {
            visibilityPrefix = "#";
        }
        return visibilityPrefix;
    }

    private String getMarkdownVisibilityPrefix(int accessFlags) {
        String visibilityPrefix;
        if (Modifier.isPublic(accessFlags)) {
            visibilityPrefix = "public ";
        } else if (Modifier.isProtected(accessFlags)) {
            visibilityPrefix = "protected ";
        } else if (Modifier.isPrivate(accessFlags)) {
            visibilityPrefix = "private ";
        } else {
            visibilityPrefix = "";
        }
        return visibilityPrefix;
    }

    private String getSanitisedFieldType(String type) {
        final String sanitisedType;
        if (type.contains("/")) {
            String[] parts = type.split(Pattern.quote("/"));
            String finalPart = parts[parts.length - 1];
            sanitisedType = finalPart.replaceAll(";", "");
        } else {
            switch (type) {
                case "V":
                    sanitisedType = "Void";
                    break;
                case "B":
                    sanitisedType = "Byte";
                    break;
                case "S":
                    sanitisedType = "Short";
                    break;
                case "C":
                    sanitisedType = "Character";
                    break;
                case "I":
                    sanitisedType = "Integer";
                    break;
                case "J":
                    sanitisedType = "Long";
                    break;
                case "F":
                    sanitisedType = "Float";
                    break;
                case "D":
                    sanitisedType = "Double";
                    break;
                case "Z":
                    sanitisedType = "Boolean";
                    break;
                default:
                    // fallback
                    sanitisedType = type;
                    break;
            }
        }
        return sanitisedType;
    }

    private Collection<DexClass> getFilteredClasses(final DexRepository dexRepository,
                                                    final String packagePrefix) {
        final Collection<DexClass> classes = dexRepository.getAllClasses();

        Logger.get().out(String.format("Total Classes size: %d", classes.size()));
        final Collection<DexClass> filteredClasses = new ArrayList<>();
        classes.forEach(dexClass -> {
            final String packageName = dexClass.getPackageName().toString();
            if (packageName.startsWith(packagePrefix) && !dexClass.isLambda()) {
                filteredClasses.add(dexClass);
            }
        });
        Logger.get().out(String.format("Filtered Classes size: %d", filteredClasses.size()));

        return filteredClasses;
    }

}
