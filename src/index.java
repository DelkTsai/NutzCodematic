import javax.swing.*;
/**
 * Created by IntelliJ IDEA.
 * User: Admin12
 * Date: 2006-1-12
 * Time: 9:04:26
 * To change this template use File | Settings | File Templates.
 */
public class index
{
	public static void main(String args[])
	{
		/**
		 * ��������̣߳�һ��������ʾ�������棬һ����������������
		 */
        try
        {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception exc)
        {
        }
		Thread start = new Thread(new startup());
		Thread load = new Thread(new loadmain());
		start.start();

		load.start();
	}
}

/**
 * ��������������ʾ
 */
class startup implements Runnable
{
	public void run()
	{
		new splash();
	}
}

/**
 * ������ļ���
 */
class loadmain implements Runnable
{
	public void run()
	{
		try
		{
			Thread.sleep(2000);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		}
		new pandy();
	}
}
