package br.ufpe.eseg.esemd.kmeans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.ufpe.eseg.esemd.database.Document;
import br.ufpe.eseg.esemd.database.Term;

public class KMeans {

	/*
	 * Initially choose k points that are likely to be in different clusters;
	 * Make these points the centroids of their clusters; FOR each remaining
	 * point p DO find the centroid to which p is closest; Add p to the cluster
	 * of that centroid; Adjust the centroid of that cluster to account for p;
	 * END;
	 */
	public static List<Group> kMeans(HashMap<Integer, Document> documents, int KGroups) {
		List<Group> clusters = new ArrayList<Group>();
		List<Integer> used = new ArrayList<Integer>();
		int i = 0;
		while (i < KGroups) {
			int random = (int) (Math.random() * documents.size());
			if (!used.contains(random)) {
				Document d = documents.get(random);
				clusters.add(new Group(d));
				used.add(random);
				i++;
			}
		}
		
		kmeans(documents, clusters);

		return clusters;
	}

	public static List<Group> kMeans(HashMap<Integer, Document> documents,
										List<Document> seeds) {
		List<Group> clusters = new ArrayList<Group>();
		// define centroids
		for (Document k : seeds) {
			clusters.add(new Group(k));				
		}
		kmeans(documents,clusters);

		return clusters;
	}

	private static void kmeans(HashMap<Integer, Document> documents, 
			List<Group> clusters) {
		
		allocDocumentsIntoGroups(documents,  clusters);
		// iterations
		boolean hasChangedGroup = true;
		Integer iteration = 0;
		while (hasChangedGroup) {
			System.out.println("Iteration = " + iteration );
			// redefine centroids
			hasChangedGroup = false;
			for (Group g: clusters) {
				if (calculateCentroid(g, documents.size())){
					hasChangedGroup = true;
				}
			}
			// Realloc documents to new centroids
			if (hasChangedGroup) {
				for (Group g : clusters) {
					g.clear();		
				}
				allocDocumentsIntoGroups(documents,clusters);
			}
			iteration++;
		}
	}

	private static void allocDocumentsIntoGroups(HashMap<Integer, Document> documents,
			List<Group> clusters) {
		for (Document doc : documents.values()) {
			if (!isCentroid(doc, clusters)) {
				Group destiny = null;
				Double minor = 0.0;
				for (Group g : clusters) {
					Double distance = documentDistance(doc, g.getCentroid(), documents.size());
					if ((minor.compareTo(0.0) == 0) || (distance.compareTo(minor) < 0)) {
						minor = distance;
						destiny = g;
					} 
				}
				destiny.addDocument(doc);
			}
		}
	}

	private static boolean isCentroid(Document doc, List<Group> clusters) {
		boolean isCentroid = false;
		for (Group g: clusters) {
			if (g.getCentroid().getId().equals(doc.getId())) {
				isCentroid = true;
			}
		}
		
		return isCentroid;
	}

	private static boolean calculateCentroid(Group g, int totalnumberOfDocuments) {
		boolean ret = false;
		Document newCentroid = null;
		Double minorSum = 0.0;
		
		double[][] distances = new double[totalnumberOfDocuments+1][totalnumberOfDocuments+1];
		
		for (Document doc : g.getGroup()) {
			Double sum = 0.0;
			for (Document doc2 : g.getGroup()) {
				if (!doc.getId().equals(doc2.getId())) {
					Double dist = 0.0;
					if ((distances[doc2.getId()-1][doc.getId()-1]) == 0.0) {
						dist = documentDistance(doc, doc2, totalnumberOfDocuments);
						distances[doc.getId()-1][doc2.getId()-1] = dist;
					}else {
						dist = distances[doc2.getId()-1][doc.getId()-1];
					}					
					sum += dist;					
				}
			}
			if ((minorSum.compareTo(0.0) == 0) || sum.compareTo(minorSum) < 0) {
		    	minorSum = sum;
		    	newCentroid = doc;
		    } 
		}
		
		// has changed centroid?
		if (!g.getCentroid().getId().equals(newCentroid.getId())) {
			ret = true;
			g.setCentroid(newCentroid);
		}
		
		return ret;
	}

	/*
	 * the distance is the inverse of the dot product of the TF-IDF values for
	 * each term present in both documents.
	 */
	private static Double documentDistance(Document d1, Document d2, int totalnumberOfDocuments) {
		Double similarity = 0.0;
		double distance = 0.0;
		if (d1.getTerms().size() < d2.getTerms().size()) {
			similarity = tfIdfDotProduct(d1, d2, totalnumberOfDocuments, similarity);
		} else {
			similarity = tfIdfDotProduct(d2, d1, totalnumberOfDocuments, similarity);
		}
		
		if (similarity.compareTo(0.0) == 0) {
			distance = 1.0;
		} else {
			distance = 1.0 / similarity;
		}

		return distance;
	}

	private static Double tfIdfDotProduct(Document d1, Document d2, int totalnumberOfDocuments, Double similarity) {
		for (Term term: d1.getTerms()) {			
			if (d2.getTerms().contains(term)) {
				Double tfD1 = term.termFrequencyBM25(d1.getId());
				Double tfD2 = term.termFrequencyBM25(d2.getId());
				Double idf = term.IDF(totalnumberOfDocuments);
				//Double tfIdfD1 = tfD1 * idf;
				//Double tfIdfD2 = tfD2 * idf;
				similarity += (tfD1 * tfD2 * idf);
			}
		}
		return similarity;
	}
}
