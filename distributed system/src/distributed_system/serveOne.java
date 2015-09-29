package distributed_system;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class serveOne extends Thread{  
	  
    private Socket customer_socket = null;  
    private BufferedReader dIn = null;  
    private PrintWriter dOut = null; 
    private int server_id; 
    private Server server_A;
    private int c_index;
      
    public serveOne(Socket s, Server server, int index){  
        customer_socket = s; 
        server_id = server.myId;
        server_A = server;
        c_index = index;
      
            dIn = server_A.dIn.get(c_index);
            dOut = server_A.dOut.get(c_index);
            System.out.println("a customer connects to you ! ");
            dOut.println("request approved...");  
            dOut.flush();
            start();  
      
    }  
        
    public void run() {  
        while(true){  
            String str;  
            try {  
                str = dIn.readLine();  
                StringTokenizer stoken = new StringTokenizer(str);
                String tag = stoken.nextToken();
                String operation = stoken.nextToken(); 
                element customer_req;
                int seats_num = 0;
                String customer_name = stoken.nextToken();

                if(operation.equals("END")){  
                    dIn.close();  
                    dOut.close();  
                    customer_socket.close();  
                    break;  
                } 
                if(operation.equals("RESERVE")){
                	
                seats_num = Integer.parseInt(stoken.nextToken()); }
                customer_req = new element(operation,customer_name,seats_num,server_A.l_clock.getValue(),server_id,c_index);
                String req_str = operation+ " "+customer_name+ " "+ new Integer(seats_num).toString()+" " + new Integer(server_A.l_clock.getValue()).toString() + " "+new Integer(server_id).toString()+new Integer(c_index).toString();
                server_A.operate_queue.insertElement(customer_req); 
                server_A.ack.add(0);
                server_A.l_clock.tick(); 
                server_A.sendReq(req_str,c_index);
           //     boolean flag = false;
               // System.out.println(server_A.operate_queue.sendQueue());
            //	System.out.println("enter CS "+server_A.enter_critical_section());
              //  for(int i = 0;i<c_index+1;i++)
                while(true)
                if(server_A.enter_critical_section(c_index)){
                	System.out.println("enter CS "+server_A.enter_critical_section(c_index));
            		element e = server_A.operate_queue.getElement(server_A.myId,c_index);
            		server_A.Operation_to_CS(e);
            		//server_A.release(c_index);
            		server_A.operate_queue.deleteElement(e);
            	//	if(i == c_index){
            	//		flag = true;
            	//	}
            		break;
            		
            		//System.out.println(server_A.operate_queue.sendQueue());
            	}
                System.out.println("CLOCK 1 "+server_A.l_clock.getValue());
               
          
            
                System.out.println("CLOCK 2 "+server_A.l_clock.getValue());

            } catch (Exception e) {  
                try {  
                    dIn.close();  
                    dOut.close();  
                    customer_socket.close(); 
                    break;
                } catch (IOException e1) {  
                    // TODO Auto-generated catch block  
                    e1.printStackTrace();  
                }  
            }
        }  
    }  
      
      
      
}  


