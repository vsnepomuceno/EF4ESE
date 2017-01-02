package br.ufpe.eseg.esemd.database;

import java.util.HashMap;

public class Dictionary {

	private HashMap<String, Term> dictionary;
	private HashMap<Integer, Document> documents;

	public Dictionary() {
		this.dictionary = new HashMap<String, Term>();
		this.documents = new HashMap<Integer, Document>();
	}
	
	public void addTerm(String term, Document d, Integer position) {
		Term t = null;
		if (this.dictionary.containsKey(term)) {
			t = this.dictionary.get(term);
			t.update(d.getId(), position);
			this.dictionary.replace(term, t);
		} else {
			t = new Term(term, d.getId(), position);
			this.dictionary.put(term, t);
		}
		d.addTerm(t);
	}
	
	public HashMap<String, Term> getDictionary() {
		return dictionary;
	} 
	
	public Term getTerm(String term) {
		return this.dictionary.get(term);
	}

	public void addDocument(Integer docId, Document d) {
		this.documents.put(docId, d);
	}
	
	public Document getDocument(Integer docId) {
		return this.documents.get(docId);
	}
	
	public Integer totalNumberOfDocuments(){
		return this.documents.size();
	}

	public HashMap<Integer, Document> getDocuments() {
		return documents;
	}
	
	public Double avarageDocumentsSize(){
		double avg = 0.0;
		for (Document d: this.documents.values()) {
			avg += d.getSize();
		}	
		
		return avg/(double)this.documents.size();
	}
	
	public double documentSizeNormalization(Integer docId) {
		Document d = this.documents.get(docId);
		
		double b = 0.1;
		
		return 1.0 - b + b*(d.getSize()/this.avarageDocumentsSize());
	}
}
