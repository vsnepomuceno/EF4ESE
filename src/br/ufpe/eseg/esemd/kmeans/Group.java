package br.ufpe.eseg.esemd.kmeans;

import java.util.ArrayList;
import java.util.List;

import br.ufpe.eseg.esemd.database.Document;

public class Group {

	private Document centroid;
	private List<Document> group;
	
	public Group(Document centroid) {
		this.centroid = centroid;
		this.group = new ArrayList<Document>();
		this.group.add(centroid);
	}
	
	public Document getCentroid() {
		return centroid;
	}
	public void setCentroid(Document centroid) {
		this.centroid = centroid;
	}
	public List<Document> getGroup() {
		return group;
	}
	public void setGroup(List<Document> group) {
		this.group = group;
	}

	public void addDocument(Document doc) {
		this.group.add(doc);		
	}

	public void clear() {
		this.group.clear();
		this.group.add(this.centroid);
	}
	
	
	
}
