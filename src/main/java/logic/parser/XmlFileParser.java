package logic.parser;

import model.Model;
import model.Paper;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.StringUtil;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class XmlFileParser extends FileParser {
    public XmlFileParser(Model model) {
        super(model);
    }

    protected void parse(File file) {
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(file);

            Paper paper = parsePaper(document, file);
            Paper p;
            if ((p = model.getPaper(paper.getTitle())) != null) System.out.println("======= Paper exists: " + paper.getTitle() +" =======");
            model.addPaper(paper);
            System.out.println(paper.getTitle());

            NodeList citationNodes = document.getElementsByTagName("citation");
            for (int i = 0; i < citationNodes.getLength(); i++) {
                Paper citation = parseXmlNode(citationNodes.item(i), 0, "");
                model.addPaper(citation);
                model.addCitation(paper.getTitle(), citation.getTitle());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Paper parsePaper(Document document, File file) {
        NodeList nodeList = document.getElementsByTagName("variant");
        Node sectLabel = nodeList.item(0);
        Node parsHed = nodeList.item(1);

        String sectLabelTitle = "", parsHedTitle = "";
        String[] sectLabelAuthors = { }, parsHedAuthors = { };
        int date = Integer.parseInt(20 + file.getName().substring(1, 3));
        String fileName = file.getName();

        if (sectLabel != null) {
            Paper sectLabelPaper = parseXmlNode(nodeList.item(0), date, fileName);
            sectLabelTitle = sectLabelPaper.getTitle();
            sectLabelAuthors = sectLabelPaper.getAuthors();
        }

        if (parsHed != null) {
            Paper parsHedPaper = parseXmlNode(nodeList.item(1), date, fileName);
            parsHedTitle = parsHedPaper.getTitle();
            parsHedAuthors = parsHedPaper.getAuthors();
        }

        String title = sectLabelTitle;
        if (title.equals("")) {
            if (!parsHedTitle.equals("")) title = parsHedTitle;
            else title = file.getName().substring(0, file.getName().length() - 11);
        }

        String[] authors = sectLabelAuthors;
        if (authors.length == 0 && parsHedAuthors.length != 0) {
            authors = parsHedAuthors;
        }
        return new Paper(title, date, authors, fileName);
    }

    private Paper parseXmlNode(Node node, int date, String fileName) {
        NodeList childNodes = node.getChildNodes();
        String title = "";
        String[] authors = { };
        String rawString = "";

        for (int i = 0; i < childNodes.getLength(); i++) {
            // We always want the first occurance in the xml
            switch (childNodes.item(i).getNodeName()) {
                case "authors" :
                case "author" :
                    if (authors.length == 0) authors = childNodes.item(i).getTextContent().trim().split("\n");
                    break;

                case "title" :
                    if (title.equals("")) title = StringUtil.parseString(childNodes.item(i).getTextContent());
                    break;

                case "date" :
                    if (date == 0) {
                        try {
                            String dateString = childNodes.item(i).getTextContent().trim();
                            date = Integer.parseInt(childNodes.item(i).getTextContent());
                        } catch (Exception e) {
                            System.out.println("Date in invalid format: " + e.getMessage());
                        }
                    }
                    break;

                case "rawString" :
                    if (rawString.equals("")) rawString = childNodes.item(i).getTextContent().trim();
                    break;

                default:
                    break;
            }
        }

        if (title.equals("")) title = rawString;
        return new Paper(title, date, authors, fileName);
    }
}
