package client_side;

import java.io.*;
import java.net.*;
import java.util.*;

public class client {
	final int MAX_SIZE_PORT = 100;
    String[] serverIP = new String[MAX_SIZE_PORT];
    int[] serverPort = new int[MAX_SIZE_PORT];
    Socket clientSoc = null;
    BufferedReader client_Req = null;
    PrintWriter req_Out = null;
    int PORT_COUNT = 0;
    customerInfo customer = new customerInfo();
    
    private static String cp ="/Users/apple/Documents/workspace/client side/bin/client_side/server_port.txt";
    
public void readAddr(){
    File file = new File(cp);
	BufferedReader reader = null;
	try{
	    reader = new BufferedReader(new FileReader(file));
	    String tmpInt;
	    
	    while((tmpInt = reader.readLine())!=null){
	     //System.out.println("ern" + tmpInt);   
	    	StringTokenizer st = new StringTokenizer(tmpInt);
	        serverIP[PORT_COUNT] = st.nextToken();
	        serverPort[PORT_COUNT] = Integer.parseInt(st.nextToken());
	        PORT_COUNT++;
	    }
	 reader.close();
	} catch (IOException e){
	System.out.println("FileReader_error :" + e);
	}
}


 public void connection(){

    boolean find_port = false;
    boolean repetition = false;
    int port_num = 0;
    int[] trash = new int[PORT_COUNT];
    int count = 0;
    String received_message = "";
	while(!find_port){
	
	   port_num = (int)(Math.random() * 5);
	   System.out.println("port num :" + serverPort[port_num]);
	   for(int i = 0; i < count; i++){
	        if(serverPort[port_num] == trash[i]) {repetition = true; break;}  
	         }
	   if(repetition){
		   repetition = false;
		   continue;
		   
	   }
		
	     
	        try {
				clientSoc = new Socket ("127.0.0.1",serverPort[port_num]); // note
				client_Req = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
				req_Out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSoc.getOutputStream())));
				req_Out.println("CUSTOMER");
			    req_Out.flush(); 
			    received_message = client_Req.readLine();
			    if (received_message.equals("request approved..."))
	            find_port = true; 
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				trash[count] = serverPort[port_num];
	            count++;
			} /*localHost address: 127.0.0.1*/
	       
	      
	      
		
	       // System.out.println(received_message);
	            

	       
	         
	        
    }
    
   
 }


//public Socket getSocket() throws UnknownHostException{
//	Socket server = null;
//    try {
//		server = new Socket("127.0.0.1",connection());
//		
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	 return server;
//}

public  String datapack(){
	String  cusInput="";
	try {
		cusInput = customer.insertFrame();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return cusInput;	
}

public void received_msg() throws IOException{
	String received_message = "";
	received_message = client_Req.readLine();
	System.out.print(received_message);
	
	
}

public static void main(String[] args)
{
	client client_side = new client();
	client_side.readAddr();
	System.out.println("read address successfully");
	client_side.connection();
	System.out.println("connect successfully");
	while(true){
	String send_msg = client_side.datapack();

	client_side.req_Out.println(send_msg);
	client_side.req_Out.flush(); 
	try {
		client_side.received_msg();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
}

}
