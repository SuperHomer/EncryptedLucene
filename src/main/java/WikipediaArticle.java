public class WikipediaArticle {
    private String title;
    private String text;

    public WikipediaArticle() {
        this.title = "";
        this.text = "";
    }

    public WikipediaArticle(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }
}
