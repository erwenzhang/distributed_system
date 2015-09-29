package distributed_system;

import java.util.*;


public class DataBase {
	final int seats = 20; // there are 20 seats at the theater
	int seats_left = 20;
	boolean[] seats_open = new boolean[seats];
	//private ArrayList<Integer> seats_num=new ArrayList<Integer>();
	public Map <String,ArrayList<Integer>> table;
	
	
public DataBase(){
		table = new HashMap<String,ArrayList<Integer>>();
		
		for(int i = 0; i <20; i++) 
			{
			 seats_open[i]=true;
			}
	}

public	ArrayList<Integer> search(String customer_A){
		ArrayList<Integer> seats_num = new ArrayList<Integer>();
		if(table.containsKey(customer_A)){
			seats_num = table.get(customer_A);
			System.out.print(customer_A +" has reserved seats: "+seats_num.toString());
			return seats_num;
		}
		else{
			System.out.print("Failed: no reservation is made by "+customer_A); return null;
			}
		}
	
public ArrayList<Integer> reserve(String customer_A, int reserved_seats){
		 
	if (seats_left < reserved_seats){	  	
		System.out.print("Failed: only "+ seats_left +" seats left but "+reserved_seats+" are requested");
		return null;
		
	}
	else{
	 if(table.containsKey(customer_A)){
	    	System.out.print("Failed: " + customer_A + " has booked the following seats: "+ table.get(customer_A).toString());
	  		return null;
	      }
	  else{
	  
	    	  ArrayList<Integer> seats_num = new ArrayList<Integer>();
	    	  table.put(customer_A, seats_num);
//	    	  for(int i = seats - seats_left,j = 0 ; j < reserved_seats;j++,i++){
//	    		  table.get(customer_A).add(i);
//	    		  
//	    		  seatsAvailable.remove(j);
//	    		  
//	    		}
	    	  int count = 0;
	    	  for(int i=0; i<seats;i++ ){
	    		 if(seats_open[i] == true){
	    			 seats_open[i] = false;
	    			 table.get(customer_A).add(i);
	    			 count++;
	    		 }
	    		 if(count ==reserved_seats) break;
	    	  }
	    	  }
	       seats_left-= reserved_seats;
	       String output = table.get(customer_A).toString();
	       System.out.print("The seats have been reserved for "+customer_A+" : "+output); 
	       return table.get(customer_A);
	}
}
public int delete(String customer_A){
	int size=table.get(customer_A).size();
	if(table.containsKey(customer_A)){
		seats_left = seats_left + size;
		for(int i=0; i<size;i++)
		{
			seats_open[table.get(customer_A).get(i)] = true;
		}
		
    ArrayList<Integer> seatsAvailable = new ArrayList<Integer>();
	
    for(int k=0; k < seats;k++){
    	if(seats_open[k] == true){
    		seatsAvailable.add(k);
    		}
	    }
	    System.out.print(table.get(customer_A).toString() + " seats have been released "+ seatsAvailable.toString() +" are now available");
		table.remove(customer_A);
		return size;
		}
     
	else {
    	   System.out.print("Failed: no reservation is made by "+customer_A);
           return  0;
	      }
	}


public String seatsInfo(){
	 Set keySet= table.keySet();
	 String dataPackage = "";
     Iterator iterator = keySet.iterator();
     while(iterator.hasNext()) {
            Object key = iterator.next();
            dataPackage =  dataPackage + key + " ";
            ArrayList<Integer> value = table.get(key);
            for( int i=0; i<value.size(); i++){
                String numbersString = value.get(i).toString();
                dataPackage =  dataPackage  + numbersString + " ";
            }
         }
    return dataPackage;
 }


public void modifyData(String customer_A,ArrayList<Integer> outerList){
	 
	  table.put(customer_A, outerList);
	  int size = outerList.size();

	  for(int i=0; i<size;i++)
		{
			seats_open[outerList.get(i)] = false;
		}
	  
 seats_left-= size;
// String output = table.get(customer_A).toString();
 //System.out.print("The seats have been reserved for "+customer_A+" : "+output); 
	
}
}