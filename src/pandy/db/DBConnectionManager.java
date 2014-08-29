package pandy.db;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Driver;
import java.util.*;


public class DBConnectionManager
{
	static private DBConnectionManager instance;
	static private int clients;
    static String dbname="db";
    public static String user="";
	private Vector drivers = new Vector();
	private PrintWriter log;
	private Hashtable pools = new Hashtable();

	/**
	 * ����Ψһʵ��������ǵ�һ�ε��ô˷������򴴽�ʵ��
	 * * * @return DBConnectionManager Ψһʵ��
	 */
	static synchronized public DBConnectionManager getInstance()
	{
		if (instance == null)
		{
			instance = new DBConnectionManager();
		}
		clients++;
		return instance;
	}

    /**
     * �����µĹ����ļ�������������
     */
    static synchronized public  DBConnectionManager getnewInstance(String newdbname)
    {
        dbname=newdbname;
        instance = new DBConnectionManager();
        clients=0;
        return instance;
    }


	//����˽�к����Է�ֹ����������ñ���ʵ��
	private DBConnectionManager()
	{
		init();
	}

	/**
	 * �����Ӷ��󷵻ظ�������ָ�������ӳ�
	 * * * @param name �������ļ��ж�������ӳ�����
	 * * * @param con ���Ӷ���
	 */
	public void freeConnection(String name, Connection con)
	{
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null)
		{
			pool.freeConnection(con);
		}
	}

	/**
	 * ���һ�����ã����еģ����ӡ����û�п������ӣ�������������С��������������ƣ��򴴽�������������
	 * * * @param name �������ļ��ж�������ӳ�����
	 * * * @param Connecton �������ӻ�null
	 */
	public Connection getConnection(String name)
	{
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null)
		{
			return pool.getConnection();
		}
		return null;
	}

	/**
	 * ���һ�����ã����еģ����ӡ����û�п������ӣ�������������С��������������ƣ��򴴽������������ӡ�������ָ����ʱ���ڵȴ������߳��ͷ�����
	 * * * @param name �������ļ��ж�������ӳ�����
	 * * * @param time �Ժ���Ƶĵȴ�ʱ��
	 * * * @return Connection �������ӻ�null
	 */
	public Connection getConnection(String name, long Time)
	{
		DBConnectionPool pool = (DBConnectionPool) pools.get(name);
		if (pool != null)
		{
			return pool.getConnection(Time);
		}
		return null;
	}

	//�չ��������ӣ��������������ע��
	public synchronized void release()
	{
		//�ȴ�ֱ�����һ�������ͻ��������
		if (--clients != 0)
		{
			return;
		}
		Enumeration allPools = pools.elements();
		while (allPools.hasMoreElements())
		{
			DBConnectionPool pool = (DBConnectionPool) allPools.nextElement();
			pool.release();
		}
		Enumeration allDrivers = drivers.elements();
		while (allDrivers.hasMoreElements())
		{
			Driver driver = (Driver) allDrivers.nextElement();
			try
			{
				DriverManager.deregisterDriver(driver);
				System.out.println("����JDBC�����̵�ע��" + driver.getClass().getName() + "��ע��");
			}
			catch (Exception e)
			{
				System.out.println("�޷���������JDBC���������ע��" + driver.getClass().getName());
			}
		}
	}

	/**
	 * ����ָ�����Դ������ӳ�ʵ��
	 * * * @param progs ���ӳ�����
	 */
	private void createPools(Properties props)
	{

		Enumeration propNames=props.propertyNames();
		while(propNames.hasMoreElements())
		{
			String name=(String)propNames.nextElement();
			if(name.endsWith(".url"))
			{
				String poolName=name.substring(0,name.lastIndexOf("."));
				String URL=props.getProperty(poolName+".url");
				if(URL==null)
				{
					log("û��Ϊ���ӳ�"+poolName+"ָ��URL");
					continue;
				}
				user=props.getProperty(poolName+".user");
				String password=props.getProperty(poolName+".password");
				String maxconn=props.getProperty(poolName+".maxconn","0");
				int max;
				try
				{
					max=Integer.valueOf(maxconn).intValue();
				}
				catch(NumberFormatException e)
				{
					log("�����������������ƣ�"+maxconn+"�����ӳأ�"+poolName);
					max=0;
				}
				DBConnectionPool pool=new DBConnectionPool(poolName,URL,user,password,max);
				pools.put(poolName,pool);
			}
		}


	}

	//��ȡ������ɳ�ʼ��
	private void init()
	{
		Properties dbProps=new Properties();
		try
		{   File file = new File("properties/"+dbname+".properties");
            InputStream is=new FileInputStream(file.getAbsolutePath());
			dbProps.load(is);
		}
		catch(Exception e)
		{
			System.err.println("���ܶ�ȡ�����ļ���"+"��ȷ��db.properties��classpathָ����·����");
			return;
		}
		String logFile=dbProps.getProperty("logfile","log.txt");
		try
		{
			log=new PrintWriter(new FileWriter(logFile,true),true);
		}
		catch(IOException e)
		{
			System.err.println("�޷�����־�ļ���"+logFile);
			log=new PrintWriter(System.err);
		}
        String user=dbProps.getProperty("sqlserverbase.user");
		loadDrivers(dbProps);
		createPools(dbProps);
	}

	/**
	 * ���غ�ע������JDBC��������
	 * * * @param progs ���ӳ�����
	 */
	private void loadDrivers(Properties Props)
	{
		String driverClasses=Props.getProperty("driver");
		StringTokenizer st=new StringTokenizer(driverClasses);
		while(st.hasMoreElements())
		{
			String driverClassName=st.nextToken().trim();
			try
			{
                Driver Driver =(Driver)	Class.forName(driverClassName).newInstance();
                DriverManager.registerDriver(Driver);
                drivers.addElement(Driver);
				log("�ɹ�ע��JDBC��������"+driverClassName);
			}
			catch(Exception e)
			{
				log("�޷�ע��JDBC��������"+driverClassName+",����"+e);
			}
		}

	}

	//���ı���Ϣд����־�ļ�
	private void log(String msg)
	{
		System.out.println(new Date() + ":" + msg);
	}

	//���ı���Ϣ���쳣д����־�ļ�
	private void log(Throwable e, String msg)
	{
		System.out.println(new Date() + ":����ԭ��" + e + ",������Ϣ��" + msg);
	}

	// ���ڲ��ඨ����һ�����ӳء����ܹ�����Ҫ�󴴽������ӣ�ֱ��Ԥ�������������Ϊֹ���ڷ������Ӹ��ͻ�����֮ǰ�����ܹ���֤���ӵ���Ч��
	class DBConnectionPool
	{
		/**
		 * �����µ����ӳ�
		 *
		 * @param name ���ӳ�����
		 * @param URL ���ݿ��JDBC��URL
		 * @param user ���ݿ��˺ţ���null
		 * @param password ���룬��null
		 * @param maxConn �����ӳ������������������
		 */
		private int checkedOut;
		private Stack freeConnections = new Stack();
		private int maxConn;
		private int Conncount = 0;
		private String name;
		private String password;
		private String URL;
		private String user;
		private String databaseUrl;

		public DBConnectionPool(String name, String url, String user, String password, int maxConn)
		{
			this.name = name;
			this.URL = url;
			this.user = user;
			this.password = password;
			this.maxConn = maxConn;
			for (int i = 0; i < maxConn; i++)
			{
				Connection con = newConnection();
				checkedOut++;
				freeConnection(con);
			}
		}

		/**
		 * ������ʹ�õ����ӷ��ظ����ӳ�
		 * * * @param con �ͻ������ͷŵ�����
		 */
		public synchronized void freeConnection(Connection con)
		{
			freeConnections.push(con);
			checkedOut--;
			notifyAll();
			System.out.println("�ɹ��ͷ�һ�����ӣ����ӳ��������С��" + Conncount + ",���У�" + freeConnections.size() + "�����ã�" + checkedOut);
		}

		//�����ӳػ��һ���������ӡ���û�п��е������ҵ�ǰ������С��������������ƣ��򴴽������ӡ���ԭ���Ǽ�Ϊ���õ����Ӳ�����Ч�����������ɾ����Ȼ��ݹ�����Լ��Գ����µĿ������ӡ�
		public synchronized Connection getConnection()
		{
			Connection con = null;
			if (freeConnections.size() > 0)
			{
				//��ȡ�����еĵ�һ����������
				con = (Connection) freeConnections.pop();
				try
				{
					if (con.isClosed())
					{
						log("�����ӳ�" + name + "ɾ��һ����Ч����");
						//�ݹ�����Լ��������ٴλ�ȡ��������
						con = getConnection();
					}
				}
				catch (Exception e)
				{
					log("�����ӳ�" + name + "ɾ��һ����Ч����");
					//�ݹ�����Լ��������ٴλ�ȡ��������
					con = getConnection();

				}
				log("�����ӳ�" + name + "�ɹ���ȡһ������");
			}
			else if (maxConn == 0 || checkedOut < maxConn)
			{
				con = newConnection();
			}
			else
			{
				try
				{
					log("max using connect: [max:" + maxConn + "/all:" + Conncount + "/free:" + freeConnections.size() + "],wait ...");
					wait(1000 * 10);
					return getConnection();
				}
				catch (InterruptedException ie)
				{
				}
			}
			if (con != null)
			{
				checkedOut++;
			}
			return con;
		}

		//�����ӳػ�ȡ�������ӡ�����ָ���ͻ������ܹ��ȴ����ʱ��
		//@param timeout���Ժ���Ƶĵȴ�ʱ������
		public synchronized Connection getConnection(long timeout)
		{
			long startTime = new Date().getTime();
			Connection con;
			while ((con = getConnection()) == null)
			{
				try
				{
					wait(timeout);
				}
				catch (InterruptedException e)
				{
				}
				if ((new Date().getTime() - startTime) >= timeout)
				{
					return null;
				}
			}
			return con;
		}

		//�ر���������
		public synchronized void release()
		{
			Enumeration allConnections = freeConnections.elements();
			while (allConnections.hasMoreElements())
			{
				Connection con = (Connection) allConnections.nextElement();
				try
				{
					con.close();
					log("�ر����ӳ�" + name + "�е�һ������");
				}
				catch (Exception e)
				{
					log(e, "�޷��ر����ӳ�" + name + "�е�һ������");
				}
			}
			freeConnections.removeAllElements();
		}

		//�����µ�����
		private Connection newConnection()
		{
			Connection con = null;
			try
			{
				if (user == null)
				{
					con = DriverManager.getConnection(URL);
				}
				else
				{
					con = DriverManager.getConnection(URL, user, password);
				}
				log("���ӳ�" + name + "����һ���µ�����");
				Conncount++;
			}
			catch (Exception e)
			{
				log(e, "�޷���������URL������" + URL + "�û�����" + user + "���룺" + password);
				return null;
			}
			return con;
		}
	}
}

				
		
	
	
