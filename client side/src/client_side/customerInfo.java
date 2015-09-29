package client_side;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;


public class customerInfo {
	 String name;
	 String seats;
	
public customerInfo(){
	name = "";
	seats= "";
}

public String insertFrame() throws IOException{
	BufferedReader reader = null;
    String Datagram = "";
    String tmpInt;
    boolean correct = true;
while(correct)  { 
    reader = new BufferedReader(new InputStreamReader(System.in));
	System.out.println("PLese input your request like CUSTOMER RESERVE/DELETE/SEARCH YOUR_NAME NUMBER_OF_SEATS:");
	if((tmpInt = reader.readLine())!=null){
	  
	    StringTokenizer st = new StringTokenizer(tmpInt);
	    Datagram = st.nextToken();
	   // System.out.println(Datagram);
	    if(Datagram.equals("CUSTOMER")) {
	    
	    tmpInt = st.nextToken();
	   // System.out.println(tmpInt);
	    if(tmpInt.equals("RESERVE")){
	    name = st.nextToken();
	    seats =  st.nextToken();
	    Datagram = Datagram+" "+"RESERVE"+" "+name+" "+seats;break;
	    }else if(tmpInt.equals("DELETE")){
	    name = st.nextToken();
	    Datagram = Datagram+" "+"DELETE"+" "+name+" "+seats;break;
	    }else if (tmpInt.equals("SEARCH")){
	    name = st.nextToken();
		Datagram = Datagram+" "+"SEARCH"+" "+name+" "+seats;break;}
	    else{
	    	System.err.print("INPUT COMMAND ERROR\n");
	    	 }
	    }
	    else System.err.print("INPUT CUSTOMER ERROR\n");
	    }
	}    
	//System.out.println(Datagram);
	
	return Datagram;
	}

}

