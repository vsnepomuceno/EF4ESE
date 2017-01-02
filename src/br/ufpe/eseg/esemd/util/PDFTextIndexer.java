package br.ufpe.eseg.esemd.util;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import br.ufpe.eseg.esemd.database.Dictionary;
import br.ufpe.eseg.esemd.database.Document;

public class PDFTextIndexer {

	private static List<String> stopWords = new ArrayList<>();

	public static void indexer(PDFContent pdf, Dictionary dictionary, Document document) {

		StringTokenizer tokenizer = new StringTokenizer(pdf.getContent(), " ");

		int position = 1;
		while (tokenizer.hasMoreTokens()) {
			String token = tokenizer.nextToken().toLowerCase().trim();
			token = removeNonCharacteres(token);
			if (!isStopWord(token) && !isNumber(token) && !token.isEmpty()) {
				dictionary.addTerm(token, document, position);
				position++;
			}
		}
		document.setSize(position - 1);
	}

	private static boolean isNumber(String token) {	
		String ret = token.replaceAll("[\\-]","");
		return ret.matches("-?\\d+(\\.\\d+)?");
	}

	private static String removeNonCharacteres(String token) {
		String ret = token;		
		ret = ret.replaceAll("[\\+\\.\\^\\*:,()$%#&@]","");
		return ret.trim();
	}

	private static boolean isStopWord(String token) {
		return stopWords.contains(token);
	}

	/*
	 * List of stop words
	 * 
	 * a about above after again against all am an and any are aren't as at be
	 * because been before being below between both but by can't cannot could
	 * couldn't did didn't do does doesn't doing don't down during each few for
	 * from further had hadn't has hasn't have haven't having he he'd he'll he's
	 * her here here's hers herself him himself his how how's i i'd i'll i'm
	 * i've if in into is isn't it it's its itself let's me more most mustn't my
	 * myself no nor not of off on once only or other ought our ours ourselves
	 * out over own same shan't she she'd she'll she's should shouldn't so some
	 * such than that that's the their theirs them themselves then there there's
	 * these they they'd they'll they're they've this those through to too under
	 * until up very was wasn't we we'd we'll we're we've were weren't what
	 * what's when when's where where's which while who who's whom why why's
	 * with won't would wouldn't you you'd you'll you're you've your yours
	 * yourself yourselves
	 */
	public static void registerStopWords() {
		stopWords.add("a");
		stopWords.add("about");
		stopWords.add("above");
		stopWords.add("after");
		stopWords.add("again");
		stopWords.add("against");
		stopWords.add("all");
		stopWords.add("am");
		stopWords.add("an");
		stopWords.add("and");
		stopWords.add("any");
		stopWords.add("are");
		stopWords.add("aren't");
		stopWords.add("as");
		stopWords.add("at");
		stopWords.add("be");
		stopWords.add("because");
		stopWords.add("been");
		stopWords.add("before");
		stopWords.add("being");
		stopWords.add("below");
		stopWords.add("between");
		stopWords.add("both");
		stopWords.add("but");
		stopWords.add("by");
		stopWords.add("can't");
		stopWords.add("cannot");
		stopWords.add("could");
		stopWords.add("couldn't");
		stopWords.add("did");
		stopWords.add("didn't");
		stopWords.add("do");
		stopWords.add("does");
		stopWords.add("doesn't");
		stopWords.add("doing");
		stopWords.add("don't");
		stopWords.add("down");
		stopWords.add("during");
		stopWords.add("each");
		stopWords.add("few");
		stopWords.add("for");
		stopWords.add("from");
		stopWords.add("further");
		stopWords.add("had");
		stopWords.add("hadn't");
		stopWords.add("has");
		stopWords.add("hasn't");
		stopWords.add("have");
		stopWords.add("haven't");
		stopWords.add("having");
		stopWords.add("he");
		stopWords.add("he'd");
		stopWords.add("he'll");
		stopWords.add("he's");
		stopWords.add("her");
		stopWords.add("here");
		stopWords.add("here's");
		stopWords.add("hers");
		stopWords.add("herself");
		stopWords.add("him");
		stopWords.add("himself");
		stopWords.add("his");
		stopWords.add("how");
		stopWords.add("how's");
		stopWords.add("i");
		stopWords.add("i'd");
		stopWords.add("i'll");
		stopWords.add("i'm");
		stopWords.add("i've");
		stopWords.add("if");
		stopWords.add("in");
		stopWords.add("into");
		stopWords.add("is");
		stopWords.add("isn't");
		stopWords.add("it");
		stopWords.add("it's");
		stopWords.add("its");
		stopWords.add("itself");
		stopWords.add("let's");
		stopWords.add("me");
		stopWords.add("more");
		stopWords.add("most");
		stopWords.add("mustn't");
		stopWords.add("my");
		stopWords.add("myself");
		stopWords.add("no");
		stopWords.add("nor");
		stopWords.add("not");
		stopWords.add("of");
		stopWords.add("off");
		stopWords.add("on");
		stopWords.add("once");
		stopWords.add("only");
		stopWords.add("or");
		stopWords.add("other");
		stopWords.add("ought");
		stopWords.add("our");
		stopWords.add("ours");
		stopWords.add("ourselves");
		stopWords.add("out");
		stopWords.add("over");
		stopWords.add("own");
		stopWords.add("same");
		stopWords.add("shan't");
		stopWords.add("she");
		stopWords.add("she'd");
		stopWords.add("she'll");
		stopWords.add("she's");
		stopWords.add("should");
		stopWords.add("shouldn't");
		stopWords.add("so");
		stopWords.add("some");
		stopWords.add("such");
		stopWords.add("than");
		stopWords.add("that");
		stopWords.add("that's");
		stopWords.add("the");
		stopWords.add("their");
		stopWords.add("theirs");
		stopWords.add("them");
		stopWords.add("themselves");
		stopWords.add("then");
		stopWords.add("there");
		stopWords.add("there's");
		stopWords.add("these");
		stopWords.add("they");
		stopWords.add("they'd");
		stopWords.add("they'll");
		stopWords.add("they're");
		stopWords.add("they've");
		stopWords.add("this");
		stopWords.add("those");
		stopWords.add("through");
		stopWords.add("to");
		stopWords.add("too");
		stopWords.add("under");
		stopWords.add("until");
		stopWords.add("up");
		stopWords.add("very");
		stopWords.add("was");
		stopWords.add("wasn't");
		stopWords.add("we");
		stopWords.add("we'd");
		stopWords.add("we'll");
		stopWords.add("we're");
		stopWords.add("we've");
		stopWords.add("were");
		stopWords.add("weren't");
		stopWords.add("what");
		stopWords.add("what's");
		stopWords.add("when");
		stopWords.add("when's");
		stopWords.add("where");
		stopWords.add("where's");
		stopWords.add("which");
		stopWords.add("while");
		stopWords.add("who");
		stopWords.add("who's");
		stopWords.add("whom");
		stopWords.add("why");
		stopWords.add("why's");
		stopWords.add("with");
		stopWords.add("won't");
		stopWords.add("would");
		stopWords.add("wouldn't");
		stopWords.add("you");
		stopWords.add("you'd");
		stopWords.add("you'll");
		stopWords.add("you're");
		stopWords.add("you've");
		stopWords.add("your");
		stopWords.add("yours");
		stopWords.add("yourself");
		stopWords.add("yourselves");
	}
}
