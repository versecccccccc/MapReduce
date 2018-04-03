package WordCount;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.io.BufferedReader;

public class WordCount {
public static void main(String[] args) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader("SongCSV.csv"));
      String line = null;
      while ((line = br.readLine()) != null) {
    	     String[] st = line.split(",");
    	     if(!st[6].equals("") && !st[7].equals("") && !st[8].equals("")){
    	    	     int len5 = st[5].length();
             char[] ch5 = new char[len5];  
             st[5].getChars(2, len5 - 1, ch5, 0);
             String test5 = new String(ch5);
             int len7 = st[7].length();
             char[] ch7 = new char[len7];  
             st[7].getChars(2, len7 - 1, ch7, 0);
             String test7 = new String(ch7);
             String f = test5 + st[6] + test7 + ", " + st[8] + ", " + st[10] + ", " + st[11] + st[12] + ", " + st[13] + ", " + st[14] + ", " + st[15];
             System.out.println(f);
    	     }
    	 
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }finally {
    	    if(null != br) {
    	    	    try {
    	    	    	    br.close();
    	    	    }catch(IOException e) {
    	    	    	    e.printStackTrace();
    	    	    }
    	    }
    }
  }

private static String String(char[] ch5) {
	// TODO Auto-generated method stub
	return null;
}
}