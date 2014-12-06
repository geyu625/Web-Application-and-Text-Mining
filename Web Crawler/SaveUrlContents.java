import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class SaveUrlContents {
	public static void saveUrlContent(String strUrl) {
		System.setProperty("http.proxyHost", "10.110.17.6");
		System.setProperty("http.proxyPort", "80");
		
		try {
			URL url = new URL(strUrl);
			InputStreamReader isr = new InputStreamReader(url.openStream(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			
			String strRead = "";
			// could try negative lookahead:http://stackoverflow.com/questions/1318279/c-sharp-regex-to-match-a-string-that-doesnt-contain-a-certain-string
            String regReview = "<div class=\\\"comment-body\\\">([^<>]+)</div>";
            
            GroupMethod gMethod = new GroupMethod();
 
            File file = new File("/home/alex/Dropbox/WorkSpace/CrawlerDanwei/Reviews.txt");
            //File file = new File("C:\\Users\\cl591alege\\Dropbox\\workspace\\CrawlerDanwei\\Reviews.txt");
            
            if (!file.exists()) {
            	file.createNewFile();
            }
            
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
          
            while ((strRead = br.readLine()) != null) {
            	List<String> listGet = gMethod.regularGroup(regReview, strRead);
            	for (String str : listGet) {
            		bw.write(str);
            		bw.newLine();
            		//bw.newLine();
            	}
            }
       
            bw.close();
			br.close(); 
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	public static List<String> getOtherUrlList(String strUrl) {
		System.setProperty("http.proxyHost", "10.110.17.6");
		System.setProperty("http.proxyPort", "80");
		
		try {
			URL url = new URL(strUrl);
			InputStreamReader isr = new InputStreamReader(url.openStream(), "utf-8");
			BufferedReader br = new BufferedReader(isr);
			
			String strRead = "";
			// could try negative lookahead:http://stackoverflow.com/questions/1318279/c-sharp-regex-to-match-a-string-that-doesnt-contain-a-certain-string
            String regUrl = "<a href=\"([^<>]+)\">\\d+</a>";
            
            GroupMethod gMethod = new GroupMethod();
            
            List<String> urlList = new ArrayList<String>();
           
            while ((strRead = br.readLine()) != null) {
            	List<String> listGet = gMethod.regularGroup(regUrl, strRead);
            	for (String str : listGet) {
            		urlList.add("http://www.zillow.com"+ str);
            	}
            }
            
            return urlList;
		} catch (IOException e){
			return new ArrayList<String>();
		}
	}
	
	public static void saveUrlContents(String strUrl) {
		SaveUrlContents.saveUrlContent(strUrl);
		
		List<String> urlList = new ArrayList<String>();
		urlList = SaveUrlContents.getOtherUrlList(strUrl);
		
		for(String url : urlList) {
			SaveUrlContents.saveUrlContent(url);
		}
	}
}
