/*
Author - Aditya Menon
Dept. of Computer Science
Indian Institute of Information Technology Sri City AP
Date - 14 - 02 - 2019
Problem Statement - Implement an alternative(n-1) distributed sorting algorithm
					as a simulation for an input n which is the total number of elements
*/
import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
/*
	The following structure describes a "process". So we maintain an array of these process' to simulate the sorting algorithm.
*/
class Node { 
    int val;	//value within a node
    int mod3Val;	//maintain a counter which keeps the value of mod3

	public Node(int val, int l) {
		this.val = val;
        this.mod3Val = l;
	}
	/* method for printing the values and area*/
	public String toString() {
        return "[" + val + ", " + mod3Val +"]";
    }
}
/* for maintaining a level of paralellization threads are used*/
class ThreadProcess extends Thread {
	Node a, b, c;
	/* Since a thread handles two and three nodes at different cases constructor overloading is used*/
	public ThreadProcess(Node a, Node b, Node c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public ThreadProcess(Node a, Node b) {
		this.a = a;
		this.b = b;
		c=null;
	}
	/* the following method deals with sending,receiving and internal comparison between nodes*/
	public void sendAndReceiveHandler(Node a, Node b) {
		int min = Math.min(b.val, a.val);
		int max = Math.max(b.val, a.val);
		a.val = min;
//		a.mod3Val=(a.mod3Val+1)%3;
		b.val = max;
//		b.mod3Val=(b.mod3Val+1)%3;
	}
	/* method overloading for sendReceiveHandler*/
	public void sendAndReceiveHandler(Node a, Node b, Node c) {
		int min = Math.min(a.val, Math.min(b.val, c.val));
		int max = Math.max(a.val, Math.max(b.val, c.val));
		int med = (a.val+b.val+c.val)-min-max;
		a.val = min;
//		a.mod3Val=(a.mod3Val+1)%3;
		b.val = med;
//		b.mod3Val=(b.mod3Val+1)%3;
		c.val = max;
//		c.mod3Val=(c.mod3Val+1)%3;
	}
	/* overriding the default run method of threads*/
	public void run() {
		try {
			if(c!=null)
				sendAndReceiveHandler(a,b,c);
			else
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
public class nMinusOneAlgorithm {
	private static Scanner sc;
	public static void main(String[] args) throws InterruptedException {
		sc = new Scanner(System.in);
		// int length = sc.nextInt();
		
		/* the length should be passed in command line*/
		int length = Integer.parseInt(args[0]);
		System.out.println("Input length is "+length);
		Node[] arr = new Node [length];
		for(int i = 0;i<length;i++) {
			//int val = sc.nextInt();
			int val = (new Random()).nextInt(10000);	// random values from 0 to 99999 is chosen
			arr[i]=new Node(val,(i%3));		//mod values
		}
		System.out.print("RANDOM SEQUENCE: ");
		for(int k = 0;k<length;k++) {
			System.out.print(arr[k].val+" ");
		}
		System.out.println();
		System.out.println("round 0:");
		for(int k = 0;k<length;k++) {
			System.out.print(arr[k].toString());
		}
		System.out.println();
		/* running the simulation for n-1 rounds*/
		for(int i = 0 ; i < length-1; i++) {
			System.out.println();
			System.out.println("round "+(i+1));
			ThreadProcess[] s = new ThreadProcess[length-1];	//invoking the thread objects
			int k = 0;	//counter for keeping track of number of threads
			for(int j = 0;j<length;j++) {
				//Here three nodes centered with mod value 1 is assigned to a thread process.
				if(arr[j].mod3Val==1 && j>0 && j<length-1) {
					s[k] = new ThreadProcess(arr[j-1],arr[j],arr[j+1]);
					s[k++].start();
				}
				// in the following cases a thread is assigned with two nodes
				else if(arr[j].mod3Val == 1 && j>0) {
					s[k] = new ThreadProcess(arr[j-1],arr[j]);
					s[k++].start();
				}
				else if(arr[j].mod3Val == 1) {
					s[k] = new ThreadProcess(arr[j],arr[j+1]);
					s[k++].start();
				}
//				else {
//					System.out.println(arr[j].val+"err");
//				}
				
			}
			/* waits for all threads to finish their tasks*/
			for(int j = 0;j<k;j++) {
				s[j].join();
			}
			
			/* update the modvalue as modvalue = (modvalue+2)%3*/
			for(int j = 0;j<length;j++) {
				arr[j].mod3Val=(arr[j].mod3Val+2)%3;
			}
			/* printing the nodes after every round*/
			for(int l = 0;l<length;l++) {
				System.out.print(arr[l].toString());
			}
			System.out.println();
			
		}
		System.out.println("end");

		int[] sortedArray = new int[length];	//Array to store the sorted values
		for(int i = 0 ; i<length ; i++) {
				sortedArray[i]=arr[i].val;
		}
		System.out.println(Arrays.toString(sortedArray));	//prints the sorted array
		
	}
}
