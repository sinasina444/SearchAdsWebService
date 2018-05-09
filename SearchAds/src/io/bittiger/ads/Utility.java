package io.bittiger.ads;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.standard.StandardFilter; 
import org.apache.lucene.analysis.en.KStemFilter; 
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.util.Version;
import org.apache.lucene.analysis.util.CharArraySet;

public class Utility {
	private static final Version LUCENE_VERSION = Version.LUCENE_40; 
    private static String stopWords = "a,able,about,across,after,all,almost,also,am,among,an,and,any,are,as,at,be,because,been,but,by,can,cannot,could,dear,did,do,does,either,else,ever,every,for,from,get,got,had,has,have,he,her,hers,him,his,how,however,i,if,in,into,is,it,its,just,least,let,like,likely,may,me,might,most,must,my,neither,no,nor,not,of,off,often,on,only,or,other,our,own,rather,said,say,says,she,should,since,so,some,than,that,the,their,them,then,there,these,they,this,tis,to,too,twas,us,wants,was,we,were,what,when,where,which,while,who,whom,why,will,with,would,yet,you,your"; 
	private static CharArraySet getStopwords(String stopwords) { 
        List<String> stopwordsList = new ArrayList<String>(); 
        for (String stop : stopwords.split(",")) { 
            stopwordsList.add(stop.trim()); 
        } 
        return new CharArraySet(LUCENE_VERSION, stopwordsList, true); 
    } 
	
	public static String strJoin(List<String> aArr, String sSep) {
	    StringBuilder sbStr = new StringBuilder();
	    for (int i = 0, il = aArr.size(); i < il; i++) {
	        if (i > 0)
	            sbStr.append(sSep);
	        sbStr.append(aArr.get(i));
	    }
	    return sbStr.toString();
	}
	
	//remove stop word, tokenize, stem
	public static List<String> cleanedTokenize(String input) {
		List<String> tokens = new ArrayList<String>();
		StringReader reader = new StringReader(input.toLowerCase());
		Tokenizer tokenizer = new StandardTokenizer(LUCENE_VERSION, reader);
		TokenStream tokenStream = new StandardFilter(LUCENE_VERSION, tokenizer);
		tokenStream = new StopFilter(LUCENE_VERSION, tokenStream, getStopwords(stopWords));
		tokenStream = new KStemFilter(tokenStream);
        StringBuilder sb = new StringBuilder();
        CharTermAttribute charTermAttribute = tokenizer.addAttribute(CharTermAttribute.class);
        try {
        	tokenStream.reset();
	        while (tokenStream.incrementToken()) {
	            String term = charTermAttribute.toString();
	            
	            tokens.add(term);
	            sb.append(term + " ");
	        }
	        tokenStream.end();
	        tokenStream.close();

	        tokenizer.close();  
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("cleaned Tokens ="+ sb.toString());
		return tokens;
	}
}
