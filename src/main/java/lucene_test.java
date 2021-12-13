import com.testcodec.encryption.AESEngine;
import com.testcodec.lucene86.EncryptedLucene86Codec;
import com.testcodec.simpletext.EncryptedSimpleTextCodec;
import org.apache.commons.compress.compressors.CompressorException;
import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.CompressorStreamFactory;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.Codec;
import org.apache.lucene.codecs.lucene86.Lucene86Codec;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.*;
import org.apache.lucene.document.Field.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.NIOFSDirectory;
import org.apache.lucene.util.BytesRef;
import org.apache.lucene.util.IntsRefBuilder;
import org.apache.lucene.util.fst.*;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.*;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ServiceLoader;

public class lucene_test {


    // dataset source: https://www.kaggle.com/jealousleopard/goodreadsbooks
    private static String indexPath = "/Users/yoanmarti/Documents/Master/TM/code/index";
    private static Directory index;

    static {
        try {
            index = new NIOFSDirectory(Paths.get(indexPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final CharArraySet stopWords = new CharArraySet(Arrays.asList("a", "and", "around", "every", "for", "from", "in", "is", "it", "not", "on", "one", "the", "to", "under"), true);
    //private static final StandardAnalyzer analyzer = new StandardAnalyzer(stopWords);
    private static final StandardAnalyzer analyzer = new StandardAnalyzer();

    private static ArrayList<ArrayList<String>> books = new ArrayList<ArrayList<String>>();



    public static void main(String[] args) throws IOException, org.apache.lucene.queryparser.classic.ParseException, ParserConfigurationException, SAXException {

        feedBooks();
        //feedBooksGutengerg();
        //feedWikipedia();
        System.out.println("Documents feeded: "+books.size());

        /*final double sum = books.stream().map(x -> x.get(0).split(" ").length).reduce(0, (x,y) -> x+y );
        final double avg = sum / books.size();
        System.out.println(avg);*/


        IndexWriter indexWriter = setupIndex("EncryptedLucene86Codec", false);
        doIndex(indexWriter,books.size());
        System.out.println("--> Indexation finished");
        doSearch(false);
        System.out.println("--> Search finished");


        //processPerformanceTests("Lucene86Codec", 10);



        //CheckIndex checkIndex = new CheckIndex(index);
        //checkIndex.setInfoStream(System.out, true);
        //checkIndex.checkIndex();


        /*String inputValues[] = {"cat", "dog", "dogs"};
        long outputValues[] = {5, 7, 12};

        PositiveIntOutputs outputs = PositiveIntOutputs.getSingleton();
        Builder<Long> builder = new Builder<Long>(FST.INPUT_TYPE.BYTE1, outputs);
        BytesRef scratchBytes = new BytesRef();
        IntsRefBuilder scratchInts = new IntsRefBuilder();
        for (int i = 0; i < inputValues.length; i++) {
            //scratchBytes.copyChars(inputValues[i]);
            scratchBytes = new BytesRef(inputValues[i]);
            builder.add(Util.toIntsRef(scratchBytes, scratchInts), outputValues[i]);
        }
        FST<Long> fst = builder.finish();

        BytesRefFSTEnum<Long> iterator = new BytesRefFSTEnum<Long>(fst);
        while (iterator.next() != null) {
            BytesRefFSTEnum.InputOutput<Long> mapEntry = iterator.current();
            System.out.println(mapEntry.input.utf8ToString());
            System.out.println(mapEntry.output);
        }*/


    }

    public static void processPerformanceTests(String codecName, int nbTest) throws IOException, ParseException {
        IndexWriter indexWriter;
        long[] time = new long[3];
        String separator = ";";
        ArrayList<String> results;
        for (int i = 0; i < nbTest; i++) {
            results = new ArrayList<>();
            indexWriter = setupIndex(codecName, false);
            time[0] = System.currentTimeMillis();
            doIndex(indexWriter,books.size());
            time[1] = System.currentTimeMillis();
            doSearch(false);
            time[2] = System.currentTimeMillis();
            results.add(codecName+separator+"Index time"+separator+"ms"+separator+Long.toString(time[1]-time[0]));
            results.add(codecName+separator+"Search time"+separator+"ms"+separator+Long.toString(time[2]-time[1]));
            results.add(codecName+separator+"Index size"+separator+"bytes"+separator+Long.toString(getIndexSize()));
            storeTestData(results);
        }
    }

    public static void storeTestData(ArrayList<String> rows) throws IOException {
        System.out.println("WRITING RESULTS !!");
        File fout = new File("/Users/yoanmarti/Documents/Master/TM/code/results.csv");
        FileOutputStream fos = new FileOutputStream(fout, true);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
        for (String row:
             rows) {
            bw.write(row);
            bw.newLine();
        }

        bw.close();
    }

    public static long getIndexSize() throws IOException {
        Path folder = Paths.get(indexPath);
        long size = Files.walk(folder)
                .filter(p -> p.toFile().isFile())
                .mapToLong(p -> p.toFile().length())
                .sum();
        return size;
    }

    public static void feedBooks() throws IOException {
        BufferedReader csvReader = new BufferedReader(new FileReader("/Users/yoanmarti/Documents/Master/TM/code/documents.csv"));
        String row;
        int cpt = 0;
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(";");
            if (cpt > 0) {
                try {
                    ArrayList<String> book = new ArrayList<>();
                    book.add(data[0]);
                    book.add(data[1].replace('/', ','));
                    books.add(book);
                }
                catch (Exception e) {
                    System.out.println("Error with row '" + data[1] + "': " + e.getMessage());
                }
            }
            cpt++;
        }
    }

    public static void feedBooksGutengerg() throws IOException {
        final File folder = new File("/Users/yoanmarti/Documents/Master/TM/code/data/files");
        final List<File> fileList = Arrays.asList(folder.listFiles());

        for (File file: fileList) {
            ArrayList<String> book = new ArrayList<>();
            //System.out.println(file.getName());
            String content = FileUtils.readFileToString(file, "UTF-8");
            book.add(content);
            book.add(file.getName().replace(".txt", ""));
            books.add(book);
        }


    }

    public static void feedWikipedia() throws IOException, ParserConfigurationException, SAXException {
        String xmlFile = "/Users/yoanmarti/Downloads/enwiki-20210820-pages-articles-multistream1.xml";

        // page->title,page->revision->text

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        WikipediaHandler wikipediaHandler = new WikipediaHandler();

        saxParser.parse(xmlFile, wikipediaHandler);

        List<WikipediaArticle> articles = wikipediaHandler.getArticles();

        for (WikipediaArticle article: articles) {
            ArrayList<String> book = new ArrayList<>();
            book.add(article.getText());
            book.add(article.getTitle());
            books.add(book);
        }
        //System.out.println(articles.size());


        /*FileInputStream fin = new FileInputStream(xmlFile);
        BufferedInputStream bis = new BufferedInputStream(fin);
        CompressorInputStream input = null;
        try {
            input = new CompressorStreamFactory().createCompressorInputStream(bis);
            BufferedReader br = new BufferedReader(new InputStreamReader(input));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            String line = "";
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }

            System.out.println(br.lines().toArray().length);
            org.w3c.dom.Document doc = builder.parse();
            Element root = doc.getDocumentElement();
            System.out.println(root.toString());


        } catch (CompressorException | ParserConfigurationException e) {
            e.printStackTrace();
        }*/


    }


    private static IndexWriter setupIndex(String codecName, boolean debug) throws IOException {
        ServiceLoader<Codec> serviceLoader = ServiceLoader.load(Codec.class);
        //serviceLoader.forEach((service) -> System.out.println(service.getName()));

//        WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);

        //SimpleTextCodec codec = new SimpleTextCodec();
        //EncryptedSimpleTextCodec codec = new EncryptedSimpleTextCodec();
       //Lucene86Codec codec = new Lucene86Codec(); // default one
       //EncryptedLucene86Codec codec = new EncryptedLucene86Codec();
       Codec codec;
       switch (codecName) {
           case "SimpleTextCodec":
               codec = new SimpleTextCodec();
               break;
           case "EncryptedSimpleTextCodec":
               codec = new EncryptedSimpleTextCodec();
               ((EncryptedSimpleTextCodec) codec).setEncryptionEngine(new AESEngine(indexPath));
               break;
           case "EncryptedLucene86Codec":
               codec = new EncryptedLucene86Codec();
               ((EncryptedLucene86Codec) codec).setEncryptionEngine(new AESEngine(indexPath));
               break;
           case "Lucene86Codec":
           default:
               codec = new Lucene86Codec();
               break;
       }
        config.setCodec(codec);
        config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        if (debug)
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
            doc.add(new Field("content", books.get(i).get(0), myField));
            doc.add(new StringField("document", books.get(i).get(1), Store.YES));
            indexWriter.addDocument(doc);
        }

        indexWriter.close();
    }

    private static void doSearch(boolean scoreExplanation) throws IOException, org.apache.lucene.queryparser.classic.ParseException {

        String queryStr = "blue";
        Query q = new QueryParser("content", analyzer).parse(queryStr);

        int hitsPerPage = 10;
        int docId = -1;
        IndexReader indexReader = DirectoryReader.open(index);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(q, hitsPerPage);
        ScoreDoc[] hits = topDocs.scoreDocs;

        System.out.println("Found " + hits.length + " hits for " + queryStr);
        Document d;
        Terms termVector;
        BytesRef term = null;
        PostingsEnum docsAndPosEnum = null;
        int offset = -1;
        for(int i=0;i<hits.length;++i) {
            docId = hits[i].doc;
            d = indexSearcher.doc(docId);
            termVector = indexReader.getTermVector(docId, "content");

            TermsEnum itr = termVector.iterator();
            while((term = itr.next()) != null){
                docsAndPosEnum = itr.postings(docsAndPosEnum, PostingsEnum.ALL);
                //System.out.println(docsAndPosEnum.nextPosition());
                int nextDoc = docsAndPosEnum.nextDoc();
                assert nextDoc != DocIdSetIterator.NO_MORE_DOCS;
                final int fr = docsAndPosEnum.freq();
                final int p = docsAndPosEnum.nextPosition();
                final int o = docsAndPosEnum.startOffset();
                if (term.utf8ToString().equals(queryStr)) {
                    offset = o;
                    break;
                    //System.out.println("p=" + p + ", o=" + o + ", l=" + term.length + ", f=" + fr + ", s=" + term.utf8ToString());
                }
            }
            final int substringSize = 20;
            try{
                System.out.println((i + 1) + ". " + " ..."+d.get("content").substring(offset-substringSize,offset+substringSize).replace("\n", "")+"... " + "\t - \t" + d.get("document")  + "\t - \t" + hits[i].score);
            } catch (Exception e) {
                System.out.println((i + 1) + ". " + d.get("content") + "\t - \t" + d.get("document")  + "\t - \t" + hits[i].score);
            }

        }

        if (scoreExplanation) {
            Explanation explanation = indexSearcher.explain(q,0);
            System.out.println(explanation.toString());
        }
    }

}
