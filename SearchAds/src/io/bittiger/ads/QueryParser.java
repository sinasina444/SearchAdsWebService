package io.bittiger.ads;
import java.util.List;


public class QueryParser {
	private static QueryParser instance = null;
	
	protected QueryParser() {
		
	}
	public static QueryParser getInstance() {
	      if(instance == null) {
	         instance = new QueryParser();
	      }
	      return instance;
    }
	public List<String> QueryUnderstand(String query) {
		List<String> tokens = Utility.cleanedTokenize(query);
		return tokens;
	}
}
