package distributed_system;

import java.util .*;
import java.io.*;
import java.net.*;

public class Server{
	DataBase data;
	Queue operate_queue;
	PrintWriter[] dataOut;
	BufferedReader[] dataIn;
	
	ArrayList<PrintWriter> dOut;
	ArrayList<BufferedReader> dIn;
	Socket[] link;
	int[] port;
	boolean[] port_open;
	int myId,servers_num;
	Connector connector;
	LamportClock l_clock;
	ArrayList<Integer> ack;
	boolean enter_cs = false;	
	private static String cp ="/Users/apple/Documents/workspace/distributed system/src/distributed_system/server_port.txt";
	
	public Server(int id,int num){
		data = new DataBase();
		operate_queue = new Queue();
		l_clock = new LamportClock();
		ack = new ArrayList<Integer>();
		
		dIn = new ArrayList<BufferedReader>();
		dOut = new ArrayList<PrintWriter>();
		
		myId = id;
		servers_num = num;
		
		dataIn = new BufferedReader[servers_num];
		dataOut = new PrintWriter[servers_num];
		link = new Socket[servers_num];
		port_open = new boolean[servers_num];
		port_open[myId] = true;
		
		port = new int[servers_num];
		File file = new File(cp);
		BufferedReader reader = null;
		try{
			reader = new BufferedReader(new FileReader(file));
			String tmpInt;
			int port_num = 0;
			while((tmpInt = reader.readLine())!=null){
				port[port_num] = Integer.parseInt(tmpInt);
				port_num ++;
			}
			
		reader.close();
		}catch (IOException e){
		System.out.println("MY_error :" + e);
		}
	

		
	
	}

	//make sure the identity of data at each server
	public void initiate_DataBase(){
		boolean open_port = false;
		int i;
		for(i = 0; i < servers_num; i++)
			if(i != myId) 
				if(!port_open[i]){open_port = false;}
				else{
					open_port = true;
					break;
					
				}
		if(open_port){
			dataOut[i].println("INIT "+ new Integer(this.l_clock.getValue()).toString());
			dataOut[i].flush();		
			this.l_clock.sendAction();
		}
	
	}
	
	public void sendReq(String req,int c_index){
		
		for(int i = 0; i < servers_num; i++){
				if(i != myId&&port_open[i]){
					try{
						System.out.println("send req to "+i);
						dataOut[i].println("REQ " + new Integer(this.l_clock.getValue()).toString()+" "+new Integer(c_index).toString()+" "+req);
						dataOut[i].flush();
						link[i].setSoTimeout(5*1000);
						this.l_clock.sendAction();
						System.out.println("CLOCK 3 " + this.l_clock.getValue());
					}catch (Exception e1) {   
		                e1.printStackTrace();  
		            }  
					
				}
		}
	}
	
	public void release(int c_index){
		for(int i = 0; i < servers_num; i++)
			if(port_open[i] &&i != myId){
				dataOut[i].println("RELEASE "+ new Integer(this.l_clock.getValue()).toString()+" "+ new Integer(c_index).toString());
				dataOut[i].flush();
				this.l_clock.sendAction();
			}
				
	}
	
	public void modify_cs(element e){
		String operation = e.request();
		String customer_name = e.customer_name();
		int seats_num = e.seats_num();
		
		if(operation.equals("RESERVE")){
			data.reserve(customer_name,seats_num);
                      
        } 
        if(operation.equals("DELETE")){
        	
            data.delete(customer_name);
        } 
  
}
		
	

	public void Operation_to_CS(element e){
		String operation = e.request();
		String customer_name = e.customer_name();
		int c_index = e.c_index();
		int seats_num = e.seats_num();
		if(operation.equals("RESERVE")){
                   	ArrayList<Integer> seats = data.reserve(customer_name,seats_num);
         
                    if(seats == null){
                    	if(data.table.containsKey(customer_name)){
                    	dOut.get(c_index).println("Failed:" + customer_name+" has booked the following seats: "+ data.table.get(customer_name).toString());
                    	dOut.get(c_index).flush();
                    	}else{
                        dOut.get(c_index).println("Failed: only "+ data.seats_left +" seats left. " + seats_num + " seats are requested.");
                        dOut.get(c_index).flush();
                        }
                    }
                    else{
                     
                    	 String tmp = "";
                    	for(int i = 0; i < seats.size(); i ++){
                    		 tmp = seats.get(i).toString() + " "+ tmp;
                        }
                    	 dOut.get(c_index).println("The seats you have reserved are " + tmp);
                    	  dOut.get(c_index).flush();
                    }
                    
                       
                } 
                if(operation.equals("DELETE")){
                	int seats_delete = data.delete(customer_name);
                    if(seats_delete!=0){
                	dOut.get(c_index).println(seats_delete + " seats are released. " + data.seats_left + " seats are available.");
                    dOut.get(c_index).flush();
                    }
                    else{
                    dOut.get(c_index).println("Failed: You haven't reserved seats in our movie theater.");
                    dOut.get(c_index).flush();
                    }

                } 
                if(operation.equals("SEARCH")){
                  	ArrayList<Integer> seats = data.search(customer_name);
                
                    if(seats ==  null){
                        dOut.get(c_index).println("Failed: You haven't reserved seats in our movie theater.");
                        dOut.get(c_index).flush();
                    }
                    else{
                    	String tmp = "";
                    	for(int i = 0; i < seats.size(); i ++){
                   		 tmp = seats.get(i).toString() + " "+ tmp;
                       }
                        dOut.get(c_index).println("The seats you have reserved is " + tmp);
                        dOut.get(c_index).flush();
                     
                    }
                } 
	}

	public boolean enter_critical_section(int c_index){
		int count = 0;
		for(int i = 0; i< port_open.length;i++)
			if (port_open[i] == true&&(i!= myId))
				count ++;
		System.out.println(count);
		System.out.println(ack);
	
		return (operate_queue.isOK(myId,c_index)&&ack.get(c_index) == count);
	}
	
	public static void main(String[] args){

		int num  = Integer.parseInt(args[0]);
		int num_1 = Integer.parseInt(args[1]);
		Server server_A = new Server(num,num_1);
		Connector connector = new Connector(server_A);  // not sure!!!!!!
		connector.Connect_server(server_A.myId, server_A.servers_num, server_A.dataIn, server_A.dataOut, server_A.port,server_A.link);
		server_A.initiate_DataBase();
		ServerSocket listener = null;
		Socket req_socket  = null;
		System.out.println("Server " + server_A.myId +" started :"); 
		System.out.println("Port :" + server_A.port[server_A.myId]); 
		int c_index = 0;
		try{
			
			listener = new ServerSocket(server_A.port[server_A.myId]);
			while(true){ 
			
				System.out.println("Connection accept req_connect :");
				req_socket = listener.accept();  
               
                System.out.println("Connection accept req_connect :" + req_socket);
                BufferedReader live_data = new BufferedReader(new InputStreamReader(req_socket.getInputStream()));
                String live_string = live_data.readLine();
                StringTokenizer live_st = new StringTokenizer(live_string);
                String live_tag = live_st.nextToken();   
               
                System.out.println(live_tag);
               
               if(live_tag.equals("CUSTOMER")){

	                server_A.dOut.add(new PrintWriter(new BufferedWriter(new OutputStreamWriter(req_socket.getOutputStream())),true)) ;
	                server_A.dIn.add(live_data);  
	                new serveOne(req_socket,server_A,c_index);
	                c_index ++;
	              
            	}
            	else{
            		
            		int remote_id = Integer.parseInt(live_st.nextToken());
            		server_A.dataIn[remote_id] = live_data;
                    server_A.dataOut[remote_id] = new PrintWriter(new BufferedWriter(new OutputStreamWriter(req_socket.getOutputStream())),true);
            		server_A.link[remote_id] = req_socket;
                    server_A.port_open[remote_id] = true;
                    new server_thread(req_socket,server_A,remote_id);
            		
            	}
            	             
            }  

		}catch (Exception e) {  
            try { 
            	//if(req_socket!=null)
                req_socket.close();  
            } catch (IOException e1) {   
                e1.printStackTrace();  
            }  
        }finally{  
            try {  
            	//if(listener!=null)
            	listener.close();  
            } catch (IOException e) {   
                e.printStackTrace();  
            }
      
        }
	}
}

