import com.testcodec.encryption.AESEngine;
import com.testcodec.lucene86.EncryptedLucene86Codec;
import com.testcodec.simpletext.EncryptedSimpleTextCodec;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.lucene86.Lucene86Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

public class lucene_test {


    // dataset source: https://www.kaggle.com/jealousleopard/goodreadsbooks
    private static String indexPath = "/home/yoan/Documents/master/TM/index";
    private static Directory index;

    static {
        try {
            index = new NIOFSDirectory(Paths.get(indexPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final StandardAnalyzer analyzer = new StandardAnalyzer(new CharArraySet(Arrays.asList("the", "in"), true));

    private static ArrayList<ArrayList<String>> books = new ArrayList<ArrayList<String>>();



    public static void main(String[] args) throws IOException, org.apache.lucene.queryparser.classic.ParseException {

        feedBooks();
        System.out.println(books.size());

        IndexWriter indexWriter = setupIndex();
        doIndex(indexWriter,books.size());

//        for (int i = 0; i < 9; i++) {
//            indexWriter = setupIndex();
//            doIndex(indexWriter,1);
//        }


        doSearch();

        /*CheckIndex checkIndex = new CheckIndex(index);
        checkIndex.setInfoStream(System.out, true);
        checkIndex.checkIndex();*/
    }

    public static void feedBooks() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("/home/yoan/Documents/master/TM/books.csv"));
        String row;
        int cpt = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            if (cpt > 0) {
                try {
                    ArrayList<String> book = new ArrayList<>();
                    book.add(data[1]);
                    book.add(data[2].replace('/', ','));
                    books.add(book);
                }
                catch (Exception e) {
                    System.out.println("Error with row '" + data[1] + "': " + e.getMessage());
                }
            }
            cpt++;
        }
    }

    private static IndexWriter setupIndex() throws IOException {
        ServiceLoader<Codec> serviceLoader = ServiceLoader.load(Codec.class);
        serviceLoader.forEach((service) -> System.out.println(service.getName()));

//        WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

//        SimpleTextCodec codec = new SimpleTextCodec();
        EncryptedSimpleTextCodec codec = new EncryptedSimpleTextCodec();
//        Lucene86Codec codec = new Lucene86Codec(); // default one
//        EncryptedLucene86Codec codec = new EncryptedLucene86Codec();


        codec.setEncryptionEngine(new AESEngine(indexPath));

        config.setCodec(codec);
        config.setInfoStream(System.out); // used to debug index operations

        return new IndexWriter(index, config);
    }

    private static void doIndex(IndexWriter indexWriter, int limit) throws IOException {


        FieldType myField = new FieldType();
        myField.setStored(true);
        myField.setTokenized(true);
        myField.setStoreTermVectors(true);
        myField.setStoreTermVectorPositions(true);
        myField.setStoreTermVectorOffsets(true);
        myField.setStoreTermVectorPayloads(true);
        myField.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
        myField.setDocValuesType(DocValuesType.NONE);
        myField.setOmitNorms(true);

        FieldType myUselessField = new FieldType();
        myUselessField.setStored(false);
        myUselessField.setIndexOptions(IndexOptions.NONE);

        for (int i = 0; i < limit; i++) {
            Document doc = new Document();
            //System.out.println(books.get(i).get(0));
            doc.add(new Field("title", books.get(i).get(0), myField));
            doc.add(new StringField("authors", books.get(i).get(1), Store.YES));
            indexWriter.addDocument(doc);
        }

        indexWriter.close();
    }

    private static void doSearch() throws IOException, org.apache.lucene.queryparser.classic.ParseException {

        String queryStr = "harry";
        Query q = new QueryParser("title", analyzer).parse(queryStr);

        int hitsPerPage = 10;
        IndexReader indexReader = DirectoryReader.open(index);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(q, hitsPerPage);
        ScoreDoc[] hits = topDocs.scoreDocs;

        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
            int docId = hits[i].doc;
            Document d = indexSearcher.doc(docId);
            System.out.println((i + 1) + ". " + d.get("title") + "\t - \t" + d.get("authors"));
        }
    }

}
