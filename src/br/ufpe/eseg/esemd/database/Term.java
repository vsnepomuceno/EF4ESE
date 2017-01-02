package br.ufpe.eseg.esemd.database;

import java.util.HashMap;
import java.util.Map;

public class Term {

	private String term;
	private Map<Integer, Posting> postings;
	private Integer numberOfDocuments;
	private Integer totalFrequency;
	
	public Term (String term, Integer docId, Integer position) {
		this.term = term;
		this.numberOfDocuments = 1;
		this.totalFrequency = 1;
		this.postings = new HashMap<Integer, Posting>();
		this.postings.put(docId, new Posting(docId, position));
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public Map<Integer, Posting> getPostings() {
		return postings;
	}

	public Integer getNumberOfDocuments() {
		return numberOfDocuments;
	}

	public void setNumberOfDocuments(Integer numberOfDocuments) {
		this.numberOfDocuments = numberOfDocuments;
	}

	public Integer getTotalFrequency() {
		return totalFrequency;
	}

	public void setTotalFrequency(Integer totalFrequency) {
		this.totalFrequency = totalFrequency;
	}

	public void update(Integer docId, Integer position) {
		
		if (this.postings.containsKey(docId)) {
			Posting p = this.postings.get(docId);
			p.update(position);
			this.postings.replace(docId,p);
		} else {
			this.postings.put(docId, new Posting(docId, position));
			this.numberOfDocuments++;
		}
		this.totalFrequency++;
	}	
	
	public Integer termFrequency(Integer docId) {
		Integer ret = 0;
		if (this.postings.get(docId) != null) {
			ret = this.postings.get(docId).getTermFreq();
		}
		return ret;
	}
	
	public Double termFrequencyBM25(Integer docId) {
		double ret = 0.0;
		double k = 30.0;
		if (this.postings.get(docId) != null) {
			ret = (double)this.postings.get(docId).getTermFreq();
		}		
		ret = ((k+1)*ret)/(ret+k);		
		return ret;
	}
	
	public Double termFrequencyBM25DocumentLengthNormalization(Integer docId, double lengthNormalization) {
		double ret = 0.0;
		double k = 9.0;
		if (this.postings.get(docId) != null) {
			ret = (double)this.postings.get(docId).getTermFreq();
		}		
		ret = ((k+1)*ret)/(ret+(k*lengthNormalization));		
		return ret;
	}
	
	public Double termFrequencyBM25PlusDocumentLengthNormalization(Integer docId, double lengthNormalization) {
		double ret = 0.0;
		double k = 9.0;
		double a = 1.0;
		if (this.postings.get(docId) != null) {
			ret = (double)this.postings.get(docId).getTermFreq();
		}		
		ret = (((k+1)*ret)/(ret+(k*lengthNormalization))) + a;		
		return ret;
	}
	
	public double IDF(Integer totalNumberOfDocuments) {
		double ret = 0.0;
		if (this.numberOfDocuments != 0) {
			double value = ((double)totalNumberOfDocuments+1)/((double)this.numberOfDocuments);
			ret = Math.log(value);
		}
		return ret;
	}
}
