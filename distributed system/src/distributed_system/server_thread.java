package distributed_system;

import java.net.*;
import java.util .*;
import java.io.*;

public class server_thread extends Thread{
	private Socket server_socket = null;  
    private BufferedReader dIn = null;  
    private PrintWriter dOut = null; 
    private int server_id; 
    private Server server_A;
    private int remote_id;
	
	public server_thread(Socket s,Server server, int id){
		server_socket = s;
		server_A = server;
		server_id = server.myId;
		remote_id = id;
		try {  
            dIn = server_A.dataIn[remote_id];
            dOut = server_A.dataOut[remote_id];
            System.out.println("a server connects to you ! ");
            //dOut.println("Connecting to server " + server_id+" request approved...");  
           // dOut.flush();
            start();  
        } catch (Exception e) {  
              e.printStackTrace();  
        }  


		
	}

	public void run(){
		while(true){
			String str;
			try{
			
				
					str = dIn.readLine(); 
					System.out.println(" before receiving this info ");
					 StringTokenizer stoken = new StringTokenizer(str);
					 String req = stoken.nextToken(); 
		             element server_req;
		             int sentClock = Integer.parseInt(stoken.nextToken());
					server_A.l_clock.receiveAction(sentClock);
					System.out.println("receive this info " + str);
					System.out.println(server_A.l_clock.getValue());
					if(req.equals("ACK")){
						
						int c_index = Integer.parseInt(stoken.nextToken());
						int ack_num = server_A.ack.get(c_index)+1;
						server_A.ack.set(c_index, ack_num);
						System.out.println("ack :" + server_A.ack);
					//	if(server_A.enter_critical_section(c_index)){
		            	//	element e = server_A.operate_queue.getElement(server_A.myId,c_index);
		            	//	server_A.Operation_to_CS(e);
		            	//	server_A.release(c_index);
		            	//	server_A.operate_queue.deleteElement(e);
		            		// server_A.ack = 0;
		            	//}
						try{
							server_socket.setSoTimeout(0);
						}catch (Exception e1) {   
			                e1.printStackTrace();  
			            }  
					}
					else if(req.equals("RELEASE")){
						int c_index = Integer.parseInt(stoken.nextToken());
						server_req = server_A.operate_queue.getElement(remote_id,c_index);
						server_A.operate_queue.deleteElement(server_req);
						server_A.modify_cs(server_req);
						
					}
					
					else if(req.equals("INIT")){
						dOut.println("DATA "+new Integer(server_A.l_clock.getValue()).toString()+" " + server_A.data.seatsInfo() + "QUEUE " + server_A.operate_queue.sendQueue());
						dOut.flush();	
						server_A.l_clock.sendAction();
					}
					else if(req.equals("DATA")){
						ArrayList<Integer> seats_num = new ArrayList<Integer>();
						element tmp_element;
			        	String tmp;
			        	String name ="";
			
			        	while(!((tmp=stoken.nextToken()).equals("QUEUE")))
			        	{
			        		
			        		
				        		if(tmp.charAt(0) >='A'&& tmp.charAt(0) <= 'z')
				        		{
				     	
				        			if(seats_num.size()!= 0){
				        				ArrayList<Integer> tmp_seats_num = new ArrayList<Integer>();
				        				for(int i = 0;i<seats_num.size();i++){
				        					tmp_seats_num.add(seats_num.get(i));
				        				}
				        				server_A.data.modifyData(name, tmp_seats_num);
				        				seats_num.clear();
				        			
				        			}
				        			name = tmp;	
				        		}
				        		else
				        			seats_num.add(Integer.parseInt(tmp));
				        		//tmp = stoken.nextToken();
				        
				        }
			        	server_A.data.modifyData(name, seats_num);
	        			
			        	while(stoken.hasMoreElements()){
			        		//System.out.println("tmp: "+tmp);
			        			
			        	
			        			String action = stoken.nextToken();
			        			String customer_name = stoken.nextToken();
			        			int customer_seats = Integer.parseInt(stoken.nextToken());
			        			int time_stamp = Integer.parseInt(stoken.nextToken());
			        			int pid = Integer.parseInt(stoken.nextToken());
			        			int c_index = Integer.parseInt(stoken.nextToken());
			        			tmp_element = new element(action,customer_name,customer_seats,time_stamp,pid,c_index);
			        			server_A.operate_queue.addElement(tmp_element);
			        			
			        			
			        			
			        		}	
			        		
			        		
			        	
			        						
					}
					else {		
						
						String c_index = stoken.nextToken();
						String action = stoken.nextToken();
						String customer_name = stoken.nextToken();
						int seats_num = Integer.parseInt(stoken.nextToken());
						int time_stamp = Integer.parseInt(stoken.nextToken());
						int pid = Integer.parseInt(stoken.nextToken());
						server_req = new element(action,customer_name,seats_num, time_stamp,pid,Integer.parseInt(c_index));
						server_A.operate_queue.insertElement(server_req);
						dOut.println("ACK "+new Integer(server_A.l_clock.getValue()).toString()+" "+c_index);
						dOut.flush();
						
						
					}
			
			}catch(SocketTimeoutException e){
				e.printStackTrace();
				server_A.port_open[remote_id] = false;
			}
			catch (Exception e) {  
				e.printStackTrace();
				//try {  
               //     dIn.close();  
              //      dOut.close();  
              //      server_socket.close(); 
				server_A.port_open[remote_id] = false;
                    break;
             //   } catch (IOException e1) {  
                    // TODO Auto-generated catch block  
              //      e1.printStackTrace();  
              // } 
            }
		
	/*		finally{
				try {  
                    dIn.close();  
                    dOut.close();  
                    server_socket.close();  
                } catch (IOException e1) {  
                    // TODO Auto-generated catch block  
                    e1.printStackTrace();  
                } 
			}*/

		}
	}
}
