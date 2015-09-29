package distributed_system;
public class element {
    private String  request;
    private String customer_name;
    private int seats_num;
	private int timestamp;
	private int PID;
	private int c_index;
   

 public element(){
	   request = "";
	   customer_name = "";
	   seats_num = 0;
	   timestamp = 0;
	   PID = 0;
	   c_index = 0;
	  
   } 
 
 public element(String action,String name, int count,int time,int order,int client ){
	 	request = action;
	 	customer_name = name;
	 	seats_num = count;
	 	timestamp = time;
	 	PID = order;
	 	c_index = client;
	 	
    }
 public void updateTimestamp(int i){
	   timestamp = i;
	}  
 public int seats_num(){
	   return seats_num;
	}
 public int timestamp(){
	   return timestamp;
	}
 public String customer_name(){
	   return customer_name;
	}
 public int PID(){
	   return PID;
	} 
 
 public String request(){
	   return request;
    }
 
 public int c_index(){
	 return c_index;
 }
   
}
