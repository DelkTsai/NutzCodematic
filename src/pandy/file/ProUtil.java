package pandy.file;
import java.util.*;
import java.io.*;
public class ProUtil 
{
	private static ProUtil instance;
	private static Properties pro=new Properties();
	/**
	 *���Ψһʵ��.
	 */
	public static ProUtil getInstance()throws Exception
	{
		if(instance==null)
		{
			instance=new ProUtil(getRelativePath());
		}
		return instance;
	}
	/**
	 *˽�л����캯��,�����Ӧ�ļ�������.
	 *relativePathΪ���classpath��·��ֵ.
	 */
	private ProUtil(String relativePath)throws Exception
	{
		loadProperties(getFile(relativePath));
	}
	/**
	 *������·��ΪrelativePathֵ��File����.
	 */
	public File getFile(String relativePath)throws Exception
	{
		String absolutePath=null;
		try 
		{
			absolutePath=getClass().getClassLoader().getResource(relativePath).getPath();//��þ���·��
		}
		catch(Exception e)
		{
			throw new Exception("�����ļ�·������!�뽫�ļ�������classpath·����:"+relativePath+e);
		}
		File f=new File(absolutePath);
		if(!f.exists())
		{
			throw new Exception("����·��Ϊ:"+absolutePath+" ���ļ�������.ע��·��ֵ���ܰ����������ַ�.");
		}
		if(!f.canWrite())
		{
			throw new Exception("�ļ��������ô���,�뽫�ļ���������Ϊ�ɶ�.");
		}
		return f;
	}
	/**
	 *�������ļ��е�������ֵ.
	 */
	public void loadProperties(File f)throws Exception
	{
		FileInputStream fin=null;
		try
		{
			fin=new FileInputStream(f); 
			pro.load(fin);//���ļ��е�����ֵ����static������
			fin.close();
		}
		catch(Exception e)
		{
			throw new Exception("�������ļ�����:"+e);
		}
		finally
		{
			if(fin!=null)
			{
				try
				{
					fin.close();
				}
				catch(Exception e)
				{
					throw new Exception("��������ʱ,�ر��ļ�����:"+e);
				}
			}
		}
	}
	/**
	 *��������pro.
	 */
	public static Properties getPro()throws Exception
	{
		/**
		 *��ʵ��instanceΪ��,������ʵ����.
		 */
		getInstance();
		return pro;
	}
	public static String getValue(String key,String defaultValue)throws Exception
	{
		getInstance();
		return pro.getProperty(key,defaultValue); 
	}
	public static String getValue(String key)throws Exception
	{
		getInstance();
		return pro.getProperty(key); 
	}

	/**
	 *�趨�����ļ�·��ֵ,��������ֵ.
	 */
	public static String getRelativePath()
	{
		String relativePath="file.properties";
		return relativePath;
	}
}
