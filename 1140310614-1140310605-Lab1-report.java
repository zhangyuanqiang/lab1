package test1;
import java.text.SimpleDateFormat;
import java.util.*;

class Polynomial //����ʽ
{
	String old; //ԭʼ����ʽ
	int va; //���ش��������͵�ֵ
	int appear[]=new int[30]; //ָ��
	int coe; //ϵ��
	boolean flag,use,out; //�Ϸ����ϲ������
	
	public void Clear() //��ʼ��
	{
		va=0; 
		coe=1;
		flag=true;
		use=false;
		out=false;
		for (int i=0;i<=26;i++) appear[i]=0;
		return ;
	}
	
	public void Merge(String s) //�ֽ⣬����ϵ����ָ��
	{
		String ts[]=s.split("[*]");
		int l=ts.length;
		for (int i=0;i<l;i++)
		{
			if (ts[i].charAt(0)>='0' && ts[i].charAt(0)<='9') coe*=Num(ts[i]);
			else Stp(ts[i]);
		}
		return ;
	}
	
	public void Print(Command now,boolean be) //�������ʽ
	{
		va=0;
		out=false;
		boolean pos=false;
		char c;
		int ans[]=new int[30];
		int st=0;
		int ncoe=coe;
		
		for (int i=0;i<26;i++)
		{
			if (now.va[i]<0) ans[i]=appear[i];
			else if(now.kind==1) {ncoe*=pow(now.va[i],appear[i]);ans[i]=0;}
			else if(appear[i]>0) {ncoe*=appear[i];ans[i]=appear[i]-1;}
			if (ans[i]>0) pos=true;
		}
		
		if (!pos) {va=ncoe;return;}
		if (now.kind==2 && appear[now.de]==0) {va=0;return ;}
		if (be) System.out.print("+");
		if (ncoe>1) {System.out.print(ncoe);st=1;}
		for (int i=0;i<26;i++)
		{
			if (ans[i]>=1)
			{
				c=(char)('a'+i);
				if (st==1) System.out.print("*");
				st=1;
				System.out.print(c);
				if (ans[i]>1)	System.out.print("^"+ans[i]);
			}
		}
		out=true;
		return ;
	}
	
	private int Stn(String s) //���ַ���ת�������ֲ�����Ϸ��ԣ���λ�ж�
	{
		int tmp=0,l=s.length();
		char c;
		for (int i=0;i<l;i++)
		{
			c=s.charAt(i);
			if (c==' ') continue;
			if (c>='0' && c<='9') tmp=tmp*10+c-'0';
			else flag=false;
		}
		return tmp;
	}
	
	private int pow(int x, int y) //�η�����
	{
		int tmp=1;
		for (int i=1;i<=y;i++) tmp*=x;
		return tmp;
	}
	
	private int Num(String s) //���ִη�����
	{
		String ts[]=s.split("\\^");
		if (ts.length>2) flag=false;
		int x,y;
		x=Stn(ts[0]);
		if (ts.length>1) y=Stn(ts[1]);
		else y=1;
		return pow(x,y);
	}

	private void Stp(String s) //����ָ��
	{
		String ts[]=s.split("\\^");
		int l=ts.length,va,add,be=0;
		while (ts[0].charAt(be)==' ') be++;
		if (ts[0].length()-be>1 || l>2) flag=false;
		va=ts[0].charAt(be)-'a';
		if (l>1) add=Stn(ts[1]);
		else add=1;
		if (va>=0 && va<=25) appear[va]+=add; else flag=false;
		return ;
	}
}

class Command //ָ��
{
	boolean flag; //�Ϸ����
	int kind,de;  //�������󵼱���
	int va[]=new int [30]; //������ֵ
	
	public void Clear() //��ʼ��
	{
		de=0;
		kind=0;
		flag=true;
		for (int i=0;i<=26;i++) va[i]=-1;
		return ;
	}
	
	public void Sim(String s) //������ֵ
	{
		String ts[]=s.split(" ");
		kind=1;
		int l=ts.length;
		if (!ts[0].equals("!simplify")) {flag=false;return ;}
		for (int i=1;i<l;i++)	flag=Tr(ts[i]);
		
		return ;
		
	}
	
	private boolean Tr(String s) //���鸳ֵ�Ϸ���
	{
		int var,tmp=0;
		int l=s.length();
		if (l<3) return false;
		if (s.charAt(1)!='=') return false;
		var=s.charAt(0)-'a';
		if (var<0 || var>25) return false;
		for (int i=2;i<l;i++)
		{
			if (s.charAt(i)<'0' || s.charAt(i)>'9') return false;
			tmp=tmp*10+(s.charAt(i)-'0');
		}
		va[var]=tmp;
		return true;
	}
	
	public void Der(String s) //��
	{
		String ts[]=s.split(" ");
		kind=2;
		int l=ts.length;
		char c;
		if ((l!=2) || (!ts[0].equals("!d/d"))) {flag=false;return ;}
		c=ts[1].charAt(0);
		if (c>='a' || c<='z') {va[c-'a']=1;de=c-'a';} else flag=false;
		if (ts[1].length()>1) flag=false;
		return ;
	}
}

public class test1 {
	
	static Polynomial p[]=new Polynomial[1000];
	static Command co;
	static int sum; //��ʽ��
	static void Print_e() //���������Ϣ
	{
		System.out.println("Error");
		return ;
	}
	
	static void Print_p(Command now) //�������ʽ
	{
		int tmp=0;
		boolean st=false;
		for (int i=0;i<sum;i++)
		{
			if (p[i].use) continue;
			p[i].Print(now,st);
			tmp+=p[i].va;
			st=st|p[i].out;
		}
		if (tmp>0)
		{
			if (st) System.out.print("+");
			System.out.print(tmp);
		}
		System.out.println();
		return ;
	}
	
	static void Simplify(String s) //����
	{
		co.Clear();
		co.Sim(s);
		if (co.flag) Print_p(co);
		else Print_e();
		return ;
	}
	
	static boolean Judge(int de) //�жϱ��󵼵Ķ����Ƿ�����ڶ���ʽ��
	{
		for (int i=0;i<sum;i++) if (p[i].appear[de]>0) return true;
		return false;
	}
	
	static void Derivative(String s) //��
	{
		co.Clear();
		co.Der(s);
		if (co.flag && Judge(co.de)) Print_p(co);
		else Print_e();
		return ;
	}
	
	static boolean Equal(int x,int y) //�жϺϲ���
	{
		for (int i=0;i<25;i++)
			if (p[x].appear[i]!=p[y].appear[i]) return false;
		return true;
	}
	
	static boolean Expression(String s) //�������ʽ
	{
		String ts[]=s.split("[+]");
		boolean flag=true;
		sum=ts.length;
		for (int i=0;i<sum;i++)
		{
			p[i].Clear();
			p[i].old=ts[i];
			p[i].Merge(ts[i]);
			flag=flag&p[i].flag;
		}
		
		for (int i=0;i<sum-1;i++)
		{
			if (p[i].use) continue;
			for (int j=i+1;j<sum;j++)
			{
				if (p[j].use) continue;
				if (Equal(i,j))
				{
					p[i].coe+=p[j].coe;
					p[j].use=true;
				}
			}
		}
		return flag;	
	}
	
	static void Print_time() //ʱ��
	{
		long l = System.currentTimeMillis();
		Date date = new Date(l);
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(dateFormat.format(date));
		return ;
	}
	
	public static void main(String[] args)
	{
		Scanner in = new Scanner(System.in);
		String s;		
		boolean flag=false;
		Build();
		while (true)
		{
			s=in.nextLine();
			s.toLowerCase();
			long starttime = System.currentTimeMillis();
			//System.out.println("Start time:");
			//Print_time();
			if (s.length()==0) continue;
			if (s=="End") break;
			if (s.charAt(0)=='!')
			{
				if (s.length()==1 || !flag)  Print_e();
				else if (s.charAt(1)=='s')  Simplify(s);
				else  Derivative(s);
			}
			else
			{
				flag=Expression(s);
				co.Clear();
				if (flag) Print_p(co);
				else  Print_e();
			}
			long endtime = System.currentTimeMillis();
			//System.out.println("End time:");
			//Print_time();
			
			System.out.print("Run time:");
			System.out.printf("%d ms",endtime-starttime);
			System.out.println();
		}
		
	}
	
	static void Build()
	{
		co=new Command(); //�����µ��ڴ�ռ�
		for (int i=0;i<1000;i++) p[i]=new Polynomial(); //����
		return ;
	}
	
}
