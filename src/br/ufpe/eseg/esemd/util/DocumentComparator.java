package br.ufpe.eseg.esemd.util;

import java.util.Comparator;

import br.ufpe.eseg.esemd.database.Document;

public class DocumentComparator implements Comparator<Document> {

	@Override
	public int compare(Document o1, Document o2) {
		return (o1.getTF_IDF().intValue() == o2.getTF_IDF().intValue() )?0:(o1.getTF_IDF().intValue() > o2.getTF_IDF().intValue()?-1:1);
	}

}
