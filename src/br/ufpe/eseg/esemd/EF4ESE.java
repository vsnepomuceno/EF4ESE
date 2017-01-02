package br.ufpe.eseg.esemd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import br.ufpe.eseg.esemd.database.Dictionary;
import br.ufpe.eseg.esemd.database.Document;
import br.ufpe.eseg.esemd.database.Term;
import br.ufpe.eseg.esemd.kmeans.Group;
import br.ufpe.eseg.esemd.kmeans.KMeans;
import br.ufpe.eseg.esemd.query.Query;
import br.ufpe.eseg.esemd.util.DocumentComparator;
import br.ufpe.eseg.esemd.util.PDFContent;
import br.ufpe.eseg.esemd.util.PDFTextExtractor;
import br.ufpe.eseg.esemd.util.PDFTextIndexer;

public class EF4ESE {

	public static void main(String[] args) {
		EF4ESE.runESEMD("resources/Alex2/");
	}
	
	public static void runESEMD(String dir) {
		File pdfDir = new File(dir);

		if (pdfDir.exists()) {
			try {
				File[] files = pdfDir.listFiles();
				List<Document> centroids = new ArrayList<Document>();
				long timeBegin = Calendar.getInstance().getTimeInMillis();
				PDFTextIndexer.registerStopWords();
				Dictionary dictionary = EF4ESE.indexerFiles(files);
				System.out.println("Dictionary Size: " + dictionary.getDictionary().size());
								
				long timeEnd = Calendar.getInstance().getTimeInMillis();

				System.out.println(timeEnd - timeBegin);

				
				/*String query = "experiment experimental experimentation experimentally experiments "
						+ "subject subjects " + "replication replications replicate " + "Threat Threats validity "
						+ "planning design " + "Hypotheses Hypothesis " + "treatment variable variables factor factors "
						+ "sample samples population evaluate evaluation empirically empirical "
						+ "controlled independent dependent setup external internal "
						+ "Wohlin juristo moreno";*/
				
				String query = "experiment quasi-experiment experimental experimentation "
						+ "experimentally experiments quasi-experiments "
						+ "subject subjects " + "replication replications replicate " 
						+ "treatment variable variables factor factors "
						+ "sampling sample samples population evaluate evaluation empirically empirical "
						+ "controlled independent dependent external internal "
						+ "Wohlin juristo moreno";
				
				centroids.add(rankDocuments(dictionary, query, "resources/experiment.txt").get(0));
				
				query = "case study context real-life phenomenon qualitative interview interviews "
						+ "observation unit units behaviour exploratory confirmatory";
				centroids.add(rankDocuments(dictionary, query, "resources/case_study.txt").get(0));
				
				/*query = "survey surveys Questionnaires Questionnaire question questions "
						+ "hypothesis hypotheses response rates population sampling sample "
						+ "representativeness respondents respondent instrument checklist "
						+ "Cross sectional Longitudinal interviews "
						+ "Fowler Fink";	*/
				query = "survey surveys Questionnaires Questionnaire question questions "
						+ "response rates population sampling sample samples "
						+ "representativeness respondents respondent instrument checklist "
						+ "Cross sectional Longitudinal interviews interview "
						+ "Fowler Fink";
				centroids.add(rankDocuments(dictionary, query, "resources/survey.txt").get(0));
				
				/*query = "systematic literature review mapping search study studies criteria criterion "
						+ "inclusion exclusion protocol sources quality assessment evidence primary "
						+ "Kitchenham report";*/
				query = "systematic literature review reviews mapping mappings "
						+ "study studies criteria criterion "
						+ "inclusion exclusion protocol quality assessment evidence primary "
						+ "Kitchenham report";
				centroids.add(rankDocuments(dictionary, query, "resources/SLR.txt").get(0));
				
				query = "ethnography Ethnographic Ethnographically field "
						+ "theme themes immersion immerse observation observations observer "
						+ "observe context cultural social behavior";				
				centroids.add(rankDocuments(dictionary, query, "resources/ethno.txt").get(0));
				
				query = "action research modification reality act local inside practice context "
						+ "qualitative take part practitioner active";				
				centroids.add(rankDocuments(dictionary, query, "resources/action.txt").get(0));
				
				//timeBegin = Calendar.getInstance().getTimeInMillis();
				/*query = "experiment quasi-experiment experimental experimentation "
						+ "experimentally experiments quasi-experiments "
						+ "subject subjects " + "replication replications replicate " 
						+ "treatment variable variables factor factors "
						+ "sampling sample samples population evaluate evaluation empirically empirical "
						+ "controlled independent dependent external internal "
						+ "Wohlin juristo moreno";*/
				//runLucene(files, query);
				//timeEnd = Calendar.getInstance().getTimeInMillis();
				//System.out.println(timeEnd - timeBegin);
								
				timeBegin = Calendar.getInstance().getTimeInMillis();
				PrintStream ps = new PrintStream("resources/clustering.txt");
				List<Group> clusters = KMeans.kMeans(dictionary.getDocuments(), centroids);
				for (Group g: clusters) {
					System.out.println("CENTROID: " + g.getCentroid().getPath());
					ps.println("CENTROID: " + g.getCentroid().getPath());
					for (Document d: g.getGroup()) {
						//ps.println("Doc: " + d.getPath());
						String t = d.getPath().replace("PS (", "");
						t = t.replace(").pdf", "");
						ps.println(t);
					}					
				}
				ps.flush();
				ps.close();
				timeEnd = Calendar.getInstance().getTimeInMillis();
				System.out.println("K-means = " + (timeEnd - timeBegin));
				
				timeBegin = Calendar.getInstance().getTimeInMillis();
				for (int i = 0; i < 3; i++) {
					ps = new PrintStream("resources/clusteringRandom"+i+".txt");
					clusters = KMeans.kMeans(dictionary.getDocuments(), 6);
					for (Group g: clusters) {
						System.out.println("CENTROID: " + g.getCentroid().getPath());
						ps.println("CENTROID: " + g.getCentroid().getPath());
						for (Document d: g.getGroup()) {
							//ps.println("Doc: " + d.getPath());
							String t = d.getPath().replace("PS (", "");
							t = d.getPath().replace(").pdf", "");
							ps.println(t);
						}					
					}
					ps.flush();
					ps.close();			
				}
				timeEnd = Calendar.getInstance().getTimeInMillis();
				
				System.out.println("K-means = " + (timeEnd - timeBegin));
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static List<Document> rankDocuments(Dictionary dictionary, String query, String outFile) 
			throws IOException {
		long timeBegin;
		long timeEnd;
		timeBegin = Calendar.getInstance().getTimeInMillis();
		
		for (Document d : dictionary.getDocuments().values()) {
			d.setTF_IDF(0);
		}

		File f = new File(outFile);
		f.createNewFile();
		PrintStream ps = new PrintStream(new FileOutputStream(f));
		Query q = new Query(query);

		for (String s : q.getWords()) {
			Term t = dictionary.getTerm(s);

			if (t != null) {
				System.out.println(t.getTerm());

				ps.println(t.getTerm());
				ps.println(t.getNumberOfDocuments());
				ps.println(t.getTotalFrequency());
				Double idf = t.IDF(dictionary.totalNumberOfDocuments());
				ps.println(idf);
				for (Document d : dictionary.getDocuments().values()) {
					double tf = t.termFrequencyBM25PlusDocumentLengthNormalization(d.getId(),
							dictionary.documentSizeNormalization(d.getId()));
					Double TF_IDF = tf * idf;
					d.setTF_IDF(d.getTF_IDF() + TF_IDF);
				}
			} else {
				ps.println("Termo não encontrado: " + s);
			}
		}

		Collection<Document> documents = dictionary.getDocuments().values();
		ArrayList<Document> ds = new ArrayList<Document>();
		ds.addAll(documents);
		ds.sort(new DocumentComparator());
		
		for (Document d : ds) {
			ps.println("DOC " + d.getPath() + ": " + d.getTF_IDF().intValue());
		}
		
		ps.flush();
		ps.close();
		timeEnd = Calendar.getInstance().getTimeInMillis();

		System.out.println(timeEnd - timeBegin);
		
		return ds;
	}

	private static Dictionary indexerFiles(File[] files) throws IOException {
		Dictionary dictionary = new Dictionary();
		for (int i = 0; i < files.length; i++) {
			PDFContent pdf = PDFTextExtractor.getPDF(files[i]);
			if (pdf != null) {
				Document d = new Document(i + 1, files[i].getName());
				dictionary.addDocument(i + 1, d);
				PDFTextIndexer.indexer(pdf, dictionary, d);
				System.out.println("FILE INDEXED (" + (i + 1) + "): " + files[i].getName());
			}
		}

		removeHighDimensionalTerms(dictionary, files.length);
		
		return dictionary;
	}

	private static void removeHighDimensionalTerms(Dictionary dictionary, int length) {
		List<Term> terms = new ArrayList<Term>();
		
		for (Term t: dictionary.getDictionary().values()) {
			// remove terms that is in more than x% of the documents
			if (t.getNumberOfDocuments() > (length*0.5)) {
				terms.add(t);
			}
		}		
		
		for (Term t : terms) {
			dictionary.getDictionary().remove(t.getTerm());
		}
		
		for (Document d : dictionary.getDocuments().values()) {
			for(Term t : terms) {
				if (d.getTerms().contains(t)) {
					d.getTerms().remove(t);
				}
			}
		}
	}

	private static void runLucene(File[] files, String queryStr) {

		StandardAnalyzer analyzer = new StandardAnalyzer();
		Directory index = new RAMDirectory();

		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		try {
			IndexWriter w = new IndexWriter(index, config);
			for (File file : files) {
				PDFContent pdf = PDFTextExtractor.getPDF(file);
				if (pdf != null) {
					addDoc(w, pdf.getContent(), pdf.getTitle());
				}
			}
			w.close();

			org.apache.lucene.search.Query q = new QueryParser("body", analyzer).parse(queryStr);

			// Search and show results
			int hitsPerPage = 1099;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopDocs docs = searcher.search(q, hitsPerPage);
			ScoreDoc[] hits = docs.scoreDocs;

			// 4. display results
			System.out.println("Found " + hits.length + " hits.");
			for (int i = 0; i < hits.length; ++i) {
				int docId = hits[i].doc;
				org.apache.lucene.document.Document d = searcher.doc(docId);
				System.out.println((i + 1) + ". " + d.get("path"));
			}

			// reader can only be closed when there
			// is no need to access the documents any more.
			reader.close();
			//

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private static void addDoc(IndexWriter w, String body, String path) throws IOException {
		org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
		doc.add(new TextField("body", body, Field.Store.YES));
		doc.add(new TextField("path", path, Field.Store.YES));
		w.addDocument(doc);
	}
}
