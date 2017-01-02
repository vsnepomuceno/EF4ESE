package br.ufpe.eseg.esemd.query;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


public class Query {
	
	private String query;
	private List<String> words;
	
	public Query(String query) {
		this.query = query;
		
		this.words = new ArrayList<String>();
		StringTokenizer tokens = new StringTokenizer(query, " ");
		while(tokens.hasMoreTokens()) {
			String token = tokens.nextToken();
			if (token != null && !token.trim().equals("")) {
				this.words.add(token.toLowerCase());
			}
		}
	}

	public List<String> getWords() {
		return this.words;
	}
	
	public String getQuery() {
		return this.query;
	}
}
