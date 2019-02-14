/*
Author - Aditya Menon
		 Dept. of Computer Science
		 Indian Institute of Information Technology Sri City AP
Date - 14 - 02 - 2019
Problem Statement - Implement Sasaki's (n-1) distributed sorting algorithm
					as a simulation for an input n which is the total number of elements
*/
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
/*
	Since sasaki's algorithm has unique markers a data structure is made specifically for the purpose
*/
class Point{
	int val;	//value within a node
	boolean p;	//marker to show if its unique(corner elements)
}
/*
	The following structure describes a "process". So we maintain an array of these process' to simulate
	the sorting algorithm.
*/
class Node { 
    Point l = new Point(); //the left half of the node
    Point r = new Point(); //the right half of the node
    int area;			   //variable for the area
    // the following is the constructor
	public Node(int x, int y, int a) {
        this.l.val = x;
        this.r.val = y;
        this.area = a;
		/*
			Here we mark the extreme nodes as unique.
			Note- left half of the node of the left extreme is given a value of Integer.MIN_VALUE
				and right half of the node of the right extreme is given a value of Integer.MAX_VALUE
		*/
        if(l.val ==Integer.MIN_VALUE) {
        		l.p = false;
        		r.p = true;
        }
        else if(r.val ==Integer.MAX_VALUE) {
    		r.p = false;
    		l.p = true;
        }
        else {
        	r.p = false;
    		l.p = false;
        }
    }
	/* the following is the method for internal ordering of elements*/
    public void internalOrdering() {
    		if(l.val>r.val)
    		{
    			Point temp = l;
    			l = r;
    			r = temp;
    		}
    }
	
    public int sendLeft() { return l.val; }			//method to send the left half node
    public int sendRight() { return r.val; }		//method to send the right half node
    /* method for printing the values and area*/
	public String toString() {
        return "[" + l.val + ", " + r.val + "," + area+"]";
    }

	public int l() { return l.val; }			//for display purpose
    public int r() { return r.val; }			//for display purpose

}
/* for maintaining a level of paralellization threads are used*/
class ThreadProcess extends Thread {
	Node a, b;
	/* a thread is assigned with two nodes*/
	public ThreadProcess(Node a, Node b) {
		this.a = a;
		this.b = b;
	}
	/* the following method deals with sending,receiving and internal comparison between nodes*/
	public void sendAndReceiveHandler(Node a, Node b) {
		int lval = a.sendRight();		//lval receives from the node on right
		int rval = b.sendLeft();		//rval receives from the node on right
		// internal comparison takes place as shown below
		if(lval>rval) {
			Point temp = a.r;
			a.r = b.l;
			b.l = temp;
			/* incase of unique elements the area is updated accordingly*/
			if(a.r.p) {
				b.area+=1;
			}
			if(b.l.p) {
				b.area-=1;
			}
		}
		
	}
	/* overriding the default run method of threads*/
	public void run() {
		try {
			sendAndReceiveHandler(a,b);
//			System.out.println("thread "+Thread.currentThread().getId());
//			System.out.println(a.toString()+b.toString());
		}
		catch(Exception e) {
			throw(e);
		}
	}
}
/* the implementation of algorithm starts here*/
public class Sasaki {
	private static Scanner sc;
	public static void main(String[] args) throws InterruptedException {
		sc = new Scanner(System.in);
		// int length = sc.nextInt();
		
		/* the length should be passed in command line*/
		int length = Integer.parseInt(args[0]);
		System.out.println("Input length is "+length);
		Node[] arr = new Node [length];
		for(int i = 0;i<length;i++) {
			// int val = sc.nextInt();
			int val = (new Random()).nextInt(10000); // random values from 0 to 99999 is chosen
			if(i==0)								//for left extreme node
				arr[i]=new Node(Integer.MIN_VALUE,val,-1);
			else if(i==length - 1)					//for right extreme node
				arr[i]=new Node(val,Integer.MAX_VALUE,0);
			else
				arr[i]=new Node(val,val,0);
		}
		System.out.print("RANDOM SEQUENCE: ");
		System.out.print(arr[0].r()+" ");
		for(int k = 1;k<length;k++) {
			System.out.print(arr[k].l()+" ");
		}
		System.out.println();
		System.out.println("round 0:");
		for(int k = 0;k<length;k++) {
			System.out.print(arr[k].toString()+" ");
		}
		System.out.println();
		/* running the simulation for n-1 rounds*/
		for(int i = 0 ; i < length-1; i++) {
			System.out.println();
			System.out.println("round "+(i+1));
			ThreadProcess[] s = new ThreadProcess[length-1];	//invoking the thread objects	
			for(int j = 0;j<length-1;j++) {
				/* 
					Here two adjacent nodes are assigned to a thread
					that means for n process/nodes we need n-1 threads
				*/
				s[j] = new ThreadProcess(arr[j],arr[j+1]);	
				s[j].start();	//starts the tread executions
				//s[j].join();
			}
			/* waits for all threads to finish their tasks*/
			for(int j = 0 ;j<length-1;j++) {
				s[j].join();
			}
			/* invoking the internal ordering*/
			for(int j = 0 ;j<length;j++)
				arr[j].internalOrdering();

			/* printing the nodes after every round*/
			for(int k = 0;k<length;k++) {
				System.out.print(arr[k].toString()+" ");
			}
			System.out.println();
			
		}
		System.out.println("end");
		int[] sortedArray = new int[length];		//Array to store the sorted values
		for(int i = 0 ; i<length ; i++) {
			if(arr[i].area==-1)
				sortedArray[i]=arr[i].r();
			else
				sortedArray[i]=arr[i].l();
		}
		System.out.println(Arrays.toString(sortedArray)); 	//prints the sorted array
		
	}
}
