package br.ufpe.eseg.esemd.database;

import java.util.ArrayList;
import java.util.List;



public class Posting {	
	
	private Integer docId;
	private Integer termFreq;
	private List<Integer> positions;
	
	public Posting(Integer docId, Integer pos) {
		this.docId = docId;
		this.termFreq = 1;
		this.positions = new ArrayList<Integer>();
		this.positions.add(pos);
	}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getTermFreq() {
		return termFreq;
	}

	public void update(Integer pos) {
		this.termFreq++;
		this.positions.add(pos);
	}
	
	public List<Integer> getPositions() {
		return this.positions;
	}
}
