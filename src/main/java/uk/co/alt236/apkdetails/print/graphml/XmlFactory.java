package uk.co.alt236.apkdetails.print.graphml;

import java.util.Collection;

class XmlFactory {
    private static final String SPACE_1 = "    ";
    private static final String SPACE_2 = SPACE_1 + SPACE_1;

    private static final char NEW_LINE = '\n';
    private static final String GRAPHML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<graphml xmlns=\"http://graphml.graphdrawing.org/xmlns\"  \n" +
            "      xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "      xsi:schemaLocation=\"http://graphml.graphdrawing.org/xmlns \n" +
            "        http://graphml.graphdrawing.org/xmlns/1.0/graphml.xsd\">";

    private static final String GRAPHML_FOOTER = "</graphml>";

    public String createXml(final Collection<Vertex> vertices,
                            final Collection<Edge> edges) {

        final StringBuilder sb = new StringBuilder();
        sb.append(GRAPHML_HEADER);
        sb.append(NEW_LINE);
        sb.append(SPACE_1);
        sb.append(getGraphDeclaration());
        sb.append(NEW_LINE);


        for (final Vertex vertex : vertices) {
            sb.append(SPACE_2);
            sb.append(toXml(vertex));
            sb.append(NEW_LINE);
        }

        for (final Edge edge : edges) {
            sb.append(SPACE_2);
            sb.append(toXml(edge));
            sb.append(NEW_LINE);
        }

        sb.append(SPACE_1);
        sb.append("</graph>");
        sb.append(NEW_LINE);

        sb.append(GRAPHML_FOOTER);
        sb.append(NEW_LINE);
        return sb.toString();
    }

    private String toXml(Vertex vertex) {
        return "<node id=" + quote(vertex.getId()) + "/>";
    }

    private String toXml(Edge edge) {
        return "<edge  source=" + quote(edge.getId1()) + " target=" + quote(edge.getId2()) + "/>";
    }

    private String getGraphDeclaration() {
        return "<graph id=" + quote("G") + " edgedefault=" + quote("undirected") + ">";
    }

    private String quote(String id) {
        return "\"" + id + "\"";
    }
}
