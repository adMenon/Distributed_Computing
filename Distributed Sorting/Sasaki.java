import java.util.Arrays;
import java.util.Scanner;
import java.util.Random;
class Point{
	int val;
	boolean p;
}
class Node { 
    Point l = new Point();
    Point r = new Point();
    int area;
    public Node(int x, int y, int a) {
        this.l.val = x;
        this.r.val = y;
        this.area = a;
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
    public void orderEle() {
    		if(l.val>r.val)
    		{
    			Point temp = l;
    			l = r;
    			r = temp;
    		}
    }
    public int l() { return l.val; }
    public int r() { return r.val; }
    public String toString() {
        return "[" + l.val + ", " + r.val + "," + area+"]";
    }

}
class Sort extends Thread {
	Node a, b;
	public Sort(Node a, Node b) {
		this.a = a;
		this.b = b;
	}
	public void swap(Node a, Node b) {
		int lval = a.r();
		int rval = b.l();
		if(lval>rval) {
			Point temp = a.r;
			a.r = b.l;
			b.l = temp;
			if(a.r.p) {
				b.area+=1;
			}
			if(b.l.p) {
				b.area-=1;
			}
		}
		
	}
	public void run() {
		try {
			swap(a,b);
//			System.out.println("thread "+Thread.currentThread().getId());
//			System.out.println(a.toString()+b.toString());
		}
		catch(Exception e) {
			throw(e);
		}
	}
}
public class Sasaki {
	private static Scanner sc;
	public static void main(String[] args) throws InterruptedException {
		sc = new Scanner(System.in);
		// int length = sc.nextInt();
		int length = Integer.parseInt(args[0]);
		System.out.println("Input length is "+length);
		Node[] arr = new Node [length];
		for(int i = 0;i<length;i++) {
			// int val = sc.nextInt();
			int val = (new Random()).nextInt(10000);
			if(i==0)
				arr[i]=new Node(Integer.MIN_VALUE,val,-1);
			else if(i==length - 1)
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
		for(int i = 0 ; i < length-1; i++) {
			//System.out.println();
			//System.out.println("round "+(i+1));
			Sort[] s = new Sort[length-1];
			for(int j = 0;j<length-1;j++) {
				s[j] = new Sort(arr[j],arr[j+1]);
				s[j].start();
				//s[j].join();
			}
			for(int j = 0 ;j<length-1;j++) {
				s[j].join();
			}
			for(int j = 0 ;j<length;j++)
				arr[j].orderEle();
			//for(int k = 0;k<length;k++) {
			//	System.out.print(arr[k].toString()+" ");
			//}
			//System.out.println();
			
		}
		System.out.println("end");
		int[] sortedArray = new int[length];
		for(int i = 0 ; i<length ; i++) {
			if(arr[i].area==-1)
				sortedArray[i]=arr[i].r();
			else
				sortedArray[i]=arr[i].l();
		}
		System.out.println(Arrays.toString(sortedArray));
		
	}
}
