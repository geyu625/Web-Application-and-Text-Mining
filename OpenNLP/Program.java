import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;





import java.util.Set;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;


public class Program {
	public static void main(String[] args) throws ClassNotFoundException {
		try {		
			//try one sentence
			String sentence = "Don was always knowledgeable and professional";
			String sentence2 = "Don has created the most positive and unique experience for my family and I.";
			String sentence3 = "Mrs. Jenkins was very knowledgeable of the area market of the residents we were selling.";
			String sentence4 = " LSU tests its emergency text messaging system twice a year.";
			OpenNLP.Parse(sentence2);
			// try one sentence
			
			Class.forName("com.mysql.jdbc.Driver");
		      // setup the connection with the DB.
		    Connection connect = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/HUDList", "root", "password");

		    PreparedStatement ps = connect.prepareStatement("insert into  HUDList.Review values (default, ?, ?, ?, ?)");
		    //PreparedStatement ps2 = connect.prepareStatement("insert into  HUDList.Review (Review) values (?)");
		    
			List<List<Parse[]>> parsesListsLists = new ArrayList<List<Parse[]>>();
			
			FileInputStream fis = new FileInputStream("/home/alex/Dropbox/WorkSpace/CrawlerDanwei/Reviews.txt");
			InputStreamReader isr = new InputStreamReader(fis, "UTF8");
			BufferedReader br = new BufferedReader(isr);
			
			String strRead = "";
			List<String> reviewraw = new ArrayList<String>();
            while ((strRead = br.readLine()) != null) {
            	//ps2.setString(1, strRead);
            	//ps2.addBatch();
            	reviewraw.add(strRead);
            	parsesListsLists.add(OpenNLP.Parsepa(strRead));
            	} 
            //ps2.executeBatch(); // insert remaining records
            //ps2.close();
            
            //List<List<String>> resultslistAdj = new ArrayList<List<String>>();
            List<String> resultsAdj = new ArrayList<String>();
            String resultAdj = null;
            //List<List<String>> resultslistVP = new ArrayList<List<String>>();
            List<String> resultsVP = new ArrayList<String>();
            String resultVP = null;
            String[] temp = null;
            
            List<String> triplets = new ArrayList<String>();
            String triplet = null;
            String temp2 = null;
    		Set<String> verbsCandidates = new HashSet<String>();
    		verbsCandidates.add("previewed");
    		verbsCandidates.add("provided");
    		verbsCandidates.add("assisted");
    		verbsCandidates.add("created");
    		verbsCandidates.add("exceeded");
    		verbsCandidates.add("helped");
    		verbsCandidates.add("protect");
    		verbsCandidates.add("utilized");
    		verbsCandidates.add("advised");
    		verbsCandidates.add("displayed");
    		verbsCandidates.add("kept");
    		verbsCandidates.add("contacted");
    		verbsCandidates.add("was");
    		verbsCandidates.add("gave");
            for (List<Parse[]> parsesLists : parsesListsLists) {
            	for (int i = 0; i < parsesLists.size(); i++) {
           		
//            		out put parse trees for each sentence
            		for (Parse p : parsesLists.get(i)) {
            			p.show();
            		}
            		
            		// print triplets;
            		temp2= ExtractTriplets.findTriplets(parsesLists.get(i));
            		if (i == 0) {
            			triplet = temp2;
            		}
            		else {
            			triplet += temp2;
            		}           		    		
            		temp = ExtractTriplets.findInfo(parsesLists.get(i), verbsCandidates);
            		if (i == 0) {
            			resultAdj = temp[0];
            			resultVP = temp[1];
            		}
            		else {
                		resultAdj += temp[0];
                		resultVP += temp[1];
            		}
            		if (i < parsesLists.size() - 1) {
            			if (temp[0] != "") {
            				resultAdj += ", "; 
            			}
            			if (temp[1] != "") {
            				resultVP += ", "; 
            			}
            		}
            	}
            	// used to print triplets;
            	System.out.println();
            	//System.out.println("resultAdj: " + resultAdj);
            	//System.out.println("resultVP: " + resultVP);
            	triplets.add(triplet);
            	resultsAdj.add(resultAdj);
            	resultsVP.add(resultVP);
            }
            System.out.println();
            System.out.println("Adj is: ");
            for (String result1 : resultsAdj) {
            	System.out.println(result1);
            }
            System.out.println();
            System.out.println("VP is: ");
            for (String result2 : resultsVP) {
            	System.out.println(result2);
            }
            
            // bulk insert
            final int batchSize = 1000;
            int count = 0;
            
            for (int i = 0; i < resultsAdj.size(); i++) {
            	ps.setString(1, reviewraw.get(i));
            	ps.setString(2, triplets.get(i));
            	ps.setString(3, resultsAdj.get(i));
            	ps.setString(4, resultsVP.get(i));
            	ps.addBatch();
            	
                if(++count % batchSize == 0) {
                    ps.executeBatch();
                }
            }
            ps.executeBatch(); // insert remaining records
            ps.close();
            connect.close();
            br.close();
            } catch(SQLException se){
                //Handle errors for JDBC
                se.printStackTrace();
            } catch (IOException e) {
      		  e.printStackTrace();
    		}
    		finally {
    		}
	}
}
