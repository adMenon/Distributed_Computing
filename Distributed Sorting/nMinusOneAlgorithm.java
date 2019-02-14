import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
class Node { 
    int val;
    int mod3Val;

	public Node(int val, int l) {
		this.val = val;
        this.mod3Val = l;
	}
	public String toString() {
        return "[" + val + ", " + mod3Val +"]";
    }
}
class Sort extends Thread {
	Node a, b, c;
	public Sort(Node a, Node b, Node c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}
	public Sort(Node a, Node b) {
		this.a = a;
		this.b = b;
		c=null;
	}
	public void swap(Node a, Node b) {
		int min = Math.min(b.val, a.val);
		int max = Math.max(b.val, a.val);
		a.val = min;
//		a.mod3Val=(a.mod3Val+1)%3;
		b.val = max;
//		b.mod3Val=(b.mod3Val+1)%3;
	}
	public void swap(Node a, Node b, Node c) {
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
	public void run() {
		try {
			if(c!=null)
				swap(a,b,c);
			else
				swap(a,b);
//			System.out.println("thread "+Thread.currentThread().getId());
//			System.out.println(a.toString()+b.toString());
		}
		catch(Exception e) {
			throw(e);
		}
	}
}
public class nMinusOneAlgorithm {
	private static Scanner sc;
	public static void main(String[] args) throws InterruptedException {
		sc = new Scanner(System.in);
		// int length = sc.nextInt();
		int length = Integer.parseInt(args[0]);
		System.out.println("Input length is "+length);
		Node[] arr = new Node [length];
		for(int i = 0;i<length;i++) {
			//int val = sc.nextInt();
			int val = (new Random()).nextInt(10000);
			arr[i]=new Node(val,(i%3));
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
		for(int i = 0 ; i < length-1; i++) {
			System.out.println();
			System.out.println("round "+(i+1));
			Sort[] s = new Sort[length/2];
			int k = 0;
			for(int j = 0;j<length;j++) {
				if(arr[j].mod3Val==1 && j>0 && j<length-1) {
					s[k] = new Sort(arr[j-1],arr[j],arr[j+1]);
					s[k++].start();
				}
				else if(arr[j].mod3Val == 1 && j>0) {
					s[k] = new Sort(arr[j-1],arr[j]);
					s[k++].start();
				}
				else if(arr[j].mod3Val == 1) {
					s[k] = new Sort(arr[j],arr[j+1]);
					s[k++].start();
				}
//				else {
//					System.out.println(arr[j].val+"err");
//				}
				
			}
			for(int j = 0;j<k;j++) {
				s[j].join();
			}
			
			
			for(int j = 0;j<length;j++) {
				arr[j].mod3Val=(arr[j].mod3Val+2)%3;
			}
			
			for(int l = 0;l<length;l++) {
				System.out.print(arr[l].toString());
			}
			System.out.println();
			
		}
		System.out.println("end");
		int[] sortedArray = new int[length];
		for(int i = 0 ; i<length ; i++) {
				sortedArray[i]=arr[i].val;
		}
		System.out.println(Arrays.toString(sortedArray));
		
	}
}
