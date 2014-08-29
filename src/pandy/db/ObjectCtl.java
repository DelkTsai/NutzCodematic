package pandy.db;

import pandy.util.SysUtil;

import java.util.ArrayList;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.lang.reflect.Constructor;

/**
 * Created by IntelliJ IDEA.
 * User: Admin12
 * Date: 2005-11-25
 * Time: 15:00:10
 * To change this template use File | Settings | File Templates.
 */
public class ObjectCtl
{

    /**
     * ��ҳ�г�sql���ĳ��Ľ��
     *
     * @param con
     * @param sql
     * @param curPage
     * @param pageSize
     * @param returnObj
     * @return
     */
    public synchronized static ArrayList listPage(Connection con, String sql, int curPage, int pageSize, Object returnObj)
    {
        ArrayList results = new ArrayList();
        DBOject obj = new DBOject();
        try
        {
            ResultSet rs = obj.getrollresultset(con, sql);
            rs = obj.fingerTo(rs, curPage, pageSize);
            int count = 0;
            while (rs != null && rs.next())
            {
                results.add(createObj(rs, returnObj));
                count++;
                rs = obj.checkRs(rs, count, pageSize);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            obj.freecon(con);
        }
        return results;
    }

    /**
     * ����sql�����ָ��һ����¼�Ķ���
     *
     * @param conn
     * @param sql
     * @param returnObj
     * @return
     */
    public static Object detail(Connection conn, String sql, Object returnObj)
    {
        DBOject obj = new DBOject();
        try
        {
            ResultSet rs = obj.getresultset(conn, sql);
            if (rs.next())
                return createObj(rs, returnObj);
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            obj.freecon(conn);
        }
        return null;
    }

    /**
     * ȡһ���ֶε�һ��ֵ�����ڶ���ֶλ���ֵ�������ô˷���
     *
     * @param conn
     * @param sql
     * @return
     */
    public static synchronized int getIntRowValue(Connection conn, String sql)
    {
        int value = 0;
        DBOject obj = new DBOject();
        try
        {
            ResultSet rs = obj.getrollresultset(conn, sql);
            if (rs != null && rs.next())
            {
                value = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            obj.freecon(conn);
        }
        return value;
    }

    /**
     * ȡһ���ֶε�һ��ֵ�����ڶ���ֶλ���ֵ�������ô˷���
     *
     * @param conn
     * @param sql
     * @return
     */
    public static synchronized String getStrRowValue(Connection conn, String sql)
    {
        String value = "";
        DBOject obj = new DBOject();
        try
        {
            ResultSet rs = obj.getrollresultset(conn, sql);
            if (rs != null && rs.next())
            {
                value = rs.getString(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            obj.freecon(conn);
        }
        return value;
    }

    /**
     * ����sql��������м�¼�ĸ��������ڶ���ѯ���������Ӳ�ѯ��ֻȡ���ı�������������
     *
     * @param conn
     * @param sql
     * @return
     */
    public static synchronized int getRowCount(Connection conn, String sql)
    {
        int count = 0;
        DBOject obj = new DBOject();
        try
        {
            String end = sql.substring(sql.toLowerCase().lastIndexOf(" from "));
            sql = new StringBuffer("select count(*) ").append(end).toString();

            ResultSet rs = obj.getrollresultset(conn, sql);
            if (rs != null && rs.next())
            {
                count = rs.getInt(1);
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            obj.freecon(conn);
        }
        return count;
    }

    /**
     * ����sql�����ȡ�õļ�¼������ҳ
     *
     * @param conn
     * @param sql
     * @param returnObj
     * @return
     */
    public synchronized static ArrayList list(Connection conn, String sql, Object returnObj)
    {
        ArrayList results = new ArrayList();
        DBOject obj = new DBOject();
        try
        {
            ResultSet rs = obj.getresultset(conn, sql);
            while (rs != null && rs.next())
            {
                results.add(createObj(rs, returnObj));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        } finally
        {
            obj.freecon(conn);
        }
        return results;
    }

    /**
     * ����Id������ɾ�����ݿ������¼
     *
     * @param conn
     * @param tableName
     * @param cloName
     * @param ids
     * @return
     */
    public synchronized static int deleteByIds(Connection conn, String tableName, String cloName, String[] ids)
    {
        if (ids == null || ids.length == 0) return 0;
        String sql = "delete from " + tableName + " where " + cloName + " in " + SysUtil.getIdsplit(ids);
        DBOject obj = new DBOject();
        try
        {
            return obj.executeupdate(conn, sql);
        } finally
        {
            obj.freecon(conn);
        }
    }

    /**
     * ����Id��ֵɾ�����ݿ�һ����¼��
     *
     * @param conn
     * @param tableName
     * @param cloName
     * @param id
     * @return
     */
    public synchronized static int deleteById(Connection conn, String tableName, String cloName, int id)
    {
        String sql = "delete from " + tableName + " where " + cloName + " = " + id;
        DBOject obj = new DBOject();
        try
        {
            return obj.executeupdate(conn, sql);
        } finally
        {
            obj.freecon(conn);
        }
    }

    public synchronized static boolean reSortRow(Connection conn, String tableName, String[] ids, String rowName)
    {
        try
        {
            Statement smt = conn.createStatement();
            for (int i = 0; i < ids.length; i++)
            {
                String sql = "update " + tableName + " set " + rowName + "=" + i + " where id=" + ids[i];
                smt.addBatch(sql);
            }
            smt.executeBatch();
            smt.close();
            return true;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        } finally
        {

        }
    }

    /**
     * �������ض���
     */
    static Constructor tempConstru;
    static Object tempObj;

    private synchronized static Object createObj(ResultSet rs, Object returnObj)
    {
        Object resultObj = null;
        String className = returnObj.getClass().getName();
        try
        {
            if (tempObj != null)
            {
                if (returnObj.equals(tempObj) || className.equals(tempObj.getClass().getName()))
                    return tempConstru.newInstance(new Object[]{rs});
            }
            Constructor[] constru = returnObj.getClass().getConstructors();
            for (int i = 0; i < constru.length; i++)
            {
                Constructor cons = constru[i];
                Class para[] = cons.getParameterTypes();
                if (para.length == 1)
                {
                    if (para[0].getName().indexOf("ResultSet") != -1)
                    {
                        resultObj = cons.newInstance(new Object[]{rs});
                        tempConstru = cons;
                        return resultObj;
                    }
                }
            }
            if (resultObj == null)
            {
                System.out.println("ERROR:no Constructor( ResultSet rs) in " + className);
            }
            return resultObj;
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        } finally
        {
            tempObj = returnObj;
            returnObj = null;
        }

    }
}

