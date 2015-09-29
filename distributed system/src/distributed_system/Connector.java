package distributed_system;

import java.io.*;
import java.net.*;

public class Connector{
	ServerSocket listener;
	Server server_A;
	public Connector(Server s){
		server_A = s;
		
	}
	public void Connect_server(int myId, int servers_num, BufferedReader[] dataIn, PrintWriter[] dataOut,
		int[] port,	Socket[] link){
//		listener = new ServerSocket(port[myId]);
		for(int i = 0; i < port.length; i++){
			if(i != myId){
				try{
					link[i] = new Socket("127.0.0.1",port[i]);
					//link[i].setSoTimeout(5*1000);
					server_A.port_open[i] = true;
					dataOut[i] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(link[i].getOutputStream())),true);
					dataIn[i] = new BufferedReader(new InputStreamReader(link[i].getInputStream()));
					dataOut[i].println("SERVER " + myId + " say hello");
					 System.out.println("Server " + i + " started.");
					dataOut[i].flush();
					new server_thread(link[i],server_A,i);
				}catch (Exception e) {
                e.printStackTrace();
                System.out.println("Server " + i + " has not started.");
                
                	//try{
	               // link[i].close();
	               // dataOut[i].close();
	              //  dataIn[i].close();
	              //  }catch(IOException e1){
	               // 	e1.printStackTrace();
	               // 	 System.out.println(2);
	              //  }
            	}

			}
			
		}

	}

}