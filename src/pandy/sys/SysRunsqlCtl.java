package pandy.sys;

import pandy.db.DBOject;

import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2006-8-5
 * Time: 10:20:56
 * To change this template use File | Settings | File Templates.
 */
public class SysRunsqlCtl
{
    public static String Runsql(Connection con, String sql)
    {
        DBOject obj = new DBOject();
        String res = "�ű�ִ�гɹ���";
        String mysql[] = sql.split(";");
        try
        {
            if (mysql.length == 1)
            {
                if (mysql[0].toLowerCase().startsWith("update")||mysql[0].toLowerCase().startsWith("delete"))
                {
                    System.out.println(mysql[0]);
                    int returnvalue = obj.executeupdate(con, mysql[0]);
                    if (returnvalue == 0)
                    {
                          res = "�ű�ִ��ʧ�ܣ���û�����ݱ����º�ɾ����";
                    }
                }
                else
                {
                    ResultSet rs = obj.getrollresultset(con, mysql[0]);
                    ResultSetMetaData rsmd = rs.getMetaData(); //ȡ��ResultSetMetaData����, ���ɸñ���ȡ��ResultSet�����е���λ
                    res = "";
                    for (int i = 1; i <= rsmd.getColumnCount(); i++)
                    {
                        res += rsmd.getColumnName(i) + "����";  //�г����е�����
                    }
                    res += "\n";
                    while (rs.next())
                    {
                        for (int i = 1; i <= rsmd.getColumnCount(); i++) //����forѭ��ȡ�ü�¼���и���λ������, Ȼ��ȡ����λֵ
                        {
                            res += rs.getString(i) + "����";
                        }
                        res += "\n";
                    }
                }

            }
            else
            {
                int dsql[] = obj.exectmoresql(con, mysql);
                for (int i = 0; i < dsql.length; i++)
                {
                    if (dsql[i] == 0)
                    {
                        res = "�ű�ִ��ʧ�ܣ�";
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        finally
        {
            obj.freecon(con);
        }
        return res;
    }
}
