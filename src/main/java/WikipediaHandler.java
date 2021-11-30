import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

public class WikipediaHandler extends DefaultHandler {
    private static final String MEDIAS = "mediawiki";
    private static final String PAGE = "page";
    private static final String TITLE = "title";
    private static final String TEXT = "text";

    private List<WikipediaArticle> articles = new ArrayList<WikipediaArticle>();
    private StringBuilder elementValue;

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (elementValue == null) {
            elementValue = new StringBuilder();
        } else {
            elementValue.append(ch, start, length);
        }
    }

    @Override
    public void startDocument() throws SAXException {
        articles = new ArrayList<WikipediaArticle>();
    }

    @Override
    public void startElement(String uri, String lName, String qName, Attributes attr) throws SAXException {
        switch (qName) {
            case MEDIAS:
                articles = new ArrayList<>();
                break;
            case PAGE:
                articles.add(new WikipediaArticle());
                break;
            case TITLE:
            case TEXT:
                elementValue = new StringBuilder();
                break;
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (qName) {
            case TITLE:
                latestArticle().setTitle(elementValue.toString());
                break;
            case TEXT:
                latestArticle().setText(elementValue.toString());
                break;
        }
    }

    private WikipediaArticle latestArticle() {
        List<WikipediaArticle> articleList = articles;
        int latestArticleIndex = articleList.size() - 1;
        return articleList.get(latestArticleIndex);
    }

    public List<WikipediaArticle> getArticles() {
        return articles;
    }


}

