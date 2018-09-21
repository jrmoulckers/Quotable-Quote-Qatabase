import java.util.ArrayList;
import java.util.HashSet;

public class Quote {

	private String author;
	private String quote;
	private String date;
	private String topic;
	private HashSet<String> keywords;
	
	public Quote(String a, String q, String d, String t) {		
		if(a.length() > 30){
			String aTemp = "";
			String[] c = a.split(" ");
			
			for(int y = 0; y < c.length; y++) {
				if(y != 0 && y < (c.length - 1) && y%2 == 0)
					aTemp += "\n";
				aTemp += c[y] + " "; 
			}
			
			a = aTemp.substring(0, aTemp.length() - 1);
		}
		
		if(q.length() > 80) {
			String qTemp = "";
			String[] b = q.split(" ");
			
			for(int x = 0; x < b.length; x++) {
				if(x != 0 && x%8 == 0 && x < (b.length - 1))
					qTemp += "\n";
				qTemp += b[x] + " ";
			}
			
			q = qTemp.substring(0, qTemp.length() - 1);
		}		
		
		author = a;
		quote = q;
		date = d;
		topic = t;
		
		keywords = new HashSet<>();
		keywords.add(a);
		keywords.add(q);
		keywords.add(d);
		keywords.add(t);
		
	} 
	
	public String getAuthor() {
		return author;
	}
	
	public String getQuote() {
		return quote;
	}
	
	public String getDate() {
		return date;
	}
	
	public String getTopic() {
		return topic;
	}
	
	public HashSet<String> getKeywords() {
		return keywords;
	}
	
	public String toString() {
		return author + "\t" + quote + "\t" + date + "\t" + topic;
	}
	
	public void setQuoteBack() {
		String[] s = quote.split("\n");
		quote = "";
		for(String x : s){
			quote += x;
		}
		
		String[] a = author.split("\n");
		author = "";
		for(String y : a){
			author += y;
		}
	}
}