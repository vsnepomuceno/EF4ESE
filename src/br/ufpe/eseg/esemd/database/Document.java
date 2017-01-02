package br.ufpe.eseg.esemd.database;

import java.util.ArrayList;
import java.util.List;


public class Document {
	
	private Integer id;
	private String path;
	private Integer size;
	private double TF_IDF;
	private List<Term> terms;

	public Document(Integer id, String path) {
		this.id = id;
		this.path = path;
		this.terms = new ArrayList<Term>();
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}
	
	public Double getTF_IDF() {
		return TF_IDF;
	}
	
	public void setTF_IDF(double tF_IDF) {
		TF_IDF = tF_IDF;
	}

	public void addTerm(Term term) {
		if (!this.terms.contains(term)) {
			this.terms.add(term);
		}
	}
	
	public List<Term> getTerms() {
		return this.terms;
	}
}
