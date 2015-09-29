package distributed_system;

import java.util.ArrayList;


public class Queue {
	ArrayList<element> reqList ;

public Queue(){
		 reqList = new ArrayList<element>();
}

public void insertElement(element outer){
    int size = reqList.size();
    boolean findPID = false;
  
    for(int i =0; i < size; i++){
    if(reqList.get(i).PID() ==outer.PID()){
    	findPID = true;
    	if (reqList.get(i).timestamp() < outer.timestamp())
    			reqList.get(i).updateTimestamp(outer.timestamp());
    		for(int j = i+1; j<size;j++){
    			if(reqList.get(j).timestamp() > outer.timestamp()){
    				reqList.add(j,reqList.get(i));
    				reqList.remove(i);
    				break;
    			}else if(reqList.get(j).timestamp() == outer.timestamp()){
    				if(reqList.get(j).PID() > outer.PID()){
    					reqList.add(j,reqList.get(i));
    					reqList.remove(i);
    					break;
    				}
    				if(reqList.get(j).PID() < outer.PID()){
    					reqList.add(j+1,reqList.get(i));
    					reqList.remove(i);
    					break;
    				}
    			}
    				
    		}
    	break;
      }
    }
    int count =0;
    if (!findPID) {
    	for(; count < size; count++){
    		if (reqList.get(count).timestamp() > outer.timestamp()){
    			reqList.add(count,outer);
    			break;
    		}else if (reqList.get(count).timestamp() == outer.timestamp()){
    			    if(reqList.get(count).PID()>outer.PID()) {
    				reqList.add(count,outer);
    				break;
    			}
    			if(reqList.get(count).PID() < outer.PID()){
    				reqList.add(count+1,outer);
    				break;
    			}
    		}
    	}
    	if(count == size) reqList.add(outer);
    }
}


public boolean isOK(int myID,int client_index){
	boolean priority = true;
	/*first in reqList*/
	if (reqList.get(0).PID()==myID && reqList.get(0).c_index()==client_index){
		priority = true;}
	else{
    int position = -1; /*get myID's position in reqList*/
    	for(int i = 1; i< reqList.size(); i++){
		if(reqList.get(i).PID() == myID && reqList.get(0).c_index()==client_index){
			position = i;
		    break;
		}
     	}
    if(position == -1) {
    	priority = false;
    	System.out.println("System error : Cannot find this request in out waiting list 2");
    }
   
    String keyWord = reqList.get(position).request();
    /*write access*/
    if(keyWord == "DELETE" ||keyWord == "RESERVE"){
     	 priority = false;
      }
    
    /*read access*/
    else if(keyWord == "SEARCH"){
    	for(int i= 0;i< position; i++){
    		if(reqList.get(i).request() == "DELETE" ||reqList.get(i).request() == "RESERVE"){
    			priority = false;break;
    		}
    		else priority = true;
    	}
     }
	}
    return priority;
	}
	

public element getElement(int myID, int c_index){
	int position = -1; /*get myID's position in reqList*/
	for(int i = 0; i< reqList.size(); i++){
		
	if(reqList.get(i).PID() == myID && reqList.get(i).c_index()==c_index){
		position = i;
	    break;
	 }
	}
 if(position == -1) {
	System.out.println("System error : Cannot find this request in out waiting list 1");
    return null;
    }
    return reqList.get(position);
}

public void addElement(element outer){
	 reqList.add(outer);
}


public String sendQueue(){
	int size = reqList.size();
	String dataPackage = "";
	 for( int i=0; i<size; i++){

         String timetampString = Integer.toString(reqList.get(i).timestamp());
         String seatsString = Integer.toString(reqList.get(i).seats_num());
         String pidString = Integer.toString(reqList.get(i).PID());
         String requestString = reqList.get(i).request();
         String cusNameString = reqList.get(i).customer_name();
         dataPackage =  dataPackage  + requestString + " "+ cusNameString +" "+seatsString +" "+ timetampString
        		 +" "+ pidString +" ";
     }
	return dataPackage;
}
public void deleteElement(element trash){
	boolean findPID = false;
	int size = reqList.size();
	for(int i =0; i < size; i++){
		if(reqList.get(i).PID() ==trash.PID() && reqList.get(i).c_index() ==trash.c_index()){
			findPID = true;
			reqList.remove(i);
			break;
		}
	}
	if(!findPID) System.err.print("Sytem Error: Request has been deleted\n");
 }
}