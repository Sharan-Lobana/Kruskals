import java.util.*;

import javax.swing.JFrame;

public class MST_Kruskal {
	
	public static final int MAX=10005;
	public static Node nodes[] = new Node[MAX];
	public static int parent[] = new int[MAX];
	public static int size[] = new int [MAX];
	public static Edge edges[] = new Edge[MAX*MAX/2];
	
	public static long dist(int a,int b){
		return (1l*(nodes[a].x-nodes[b].x)*(nodes[a].x-nodes[b].x)+1l*(nodes[a].y-nodes[b].y)*(nodes[a].y-nodes[b].y));
	}
	
	public static void main(String[] args) {
		int n,i,j,cnt_edges=0;
		Scanner input = new Scanner(System.in);
		Random rand = new Random();
		n=input.nextInt();
		for(i=1;i<=n;i++){
			nodes[i] = new Node();
			nodes[i].x=rand.nextInt(n)+1;
			nodes[i].y=rand.nextInt(n)+1;
			nodes[i].index=i;
			parent[i]=i;
			size[i]=1;
		}
		for(i=1;i<n;i++)for(j=i+1;j<=n;j++){
			++cnt_edges;
			edges[cnt_edges] = new Edge();
			edges[cnt_edges].start_node = nodes[i];
			edges[cnt_edges].end_node = nodes[j];
			edges[cnt_edges].weight = dist(i,j);
		}

		long start_time=System.currentTimeMillis();
		// Sort according to edge weights
		Arrays.sort(edges,1,1+cnt_edges,new Comparator<Edge>(){
			public int compare(Edge a,Edge b){
				return Long.compare(a.weight, b.weight);
			}
		});

		for(i=1;i<=cnt_edges;i++){
			int a=edges[i].start_node.index;
			int b=edges[i].end_node.index;
			if(find(a)!=find(b)){
				union(a,b);
				edges[i].present_in_MST=true;
			}
		}
		long end_time=System.currentTimeMillis();
		System.out.println("Time taken = "+(end_time-start_time));
		DrawGraph mainPanel = new DrawGraph(edges,nodes,n,cnt_edges);
		JFrame frame = new JFrame("DrawGraph");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.getContentPane().add(mainPanel);
	    frame.pack();
	    frame.setLocationByPlatform(true);
	    frame.setVisible(true);
		
	}
	
	public static int find(int node){
		while(node!=parent[node]){
			parent[node]=parent[parent[node]];
			node=parent[node];
		}
		return node;
	}

	public static void union(int node1,int node2){
		int root1=find(node1);
		int root2=find(node2);
		if(root1!=root2){
			if(size[root1]<size[root2]){
				int temp=root1;
				root1=root2;
				root2=temp;
			}
			size[root1]+=size[root2];
			parent[root2]=root1;
		}
	}

}
