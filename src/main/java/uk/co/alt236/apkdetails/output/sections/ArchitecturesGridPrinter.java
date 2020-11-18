package uk.co.alt236.apkdetails.output.sections;

import com.jakewharton.fliptables.FlipTable;
import uk.co.alt236.apkdetails.repo.architectures.Architecture;
import uk.co.alt236.apkdetails.repo.architectures.ArchitectureRepository;
import uk.co.alt236.apkdetails.repo.architectures.SoComparison;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArchitecturesGridPrinter {

    public void print(ArchitectureRepository archs) {
        final List<SoComparison> sos = archs.getSoLibComparison();
        final List<String> architectures = archs.getJniArchitectures().stream().map(Architecture::getName).collect(Collectors.toList());

        final List<List<String>> lines = getLines(architectures, sos);

        final List<String> headers = new ArrayList<>();
        headers.add(" ");
        headers.addAll(architectures);

        final String[][] dataArray = lines.stream()
                .map(l -> l.toArray(new String[0]))
                .toArray(String[][]::new);

        System.out.println(FlipTable.of(headers.toArray(new String[0]), dataArray));

    }

    final List<List<String>> getLines(List<String> architectures, List<SoComparison> sos) {
        final List<List<String>> lines = new ArrayList<>();

        for (SoComparison comparison : sos) {
            final List<String> line = new ArrayList<>();
            line.add(comparison.getSoLibrary());
            for (String architecture : architectures) {
                if (comparison.getArchitectures().contains(architecture)) {
                    line.add("X");
                } else {
                    line.add("");
                }
            }
            lines.add(line);
        }
        return lines;
    }

}
