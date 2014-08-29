package pandy.sys;

import pandy.db.DBConnector;
import pandy.db.DBUtil;
import pandy.util.DateUtil;
import pandy.file.FileUtil;
import pandy.com.Globa;

import java.sql.*;

public class SysBuldCtl {
    public static String[] getClassCode(Connection con, String pagename, String tablename, String author, String modelname) {
        String pandy[] = new String[7];
        /**
         * pandy[0]Ϊ���ɵĻ����࣬pandy[1]Ϊ���ɵĿ�����
         */
        String topstr = "package " + pagename + ".bean;\n\nimport org.nutz.dao.entity.annotation.Column;\nimport org.nutz.dao.entity.annotation.Table;\n";
        try {
            int i = 0;
            int dbtype = 0;   //Ĭ��ΪSQLSERVER����
            int pk = -1;
            String pkname = "";
            Statement stmt = con.createStatement();
            System.out.println("drivername:" + con.getMetaData().getDriverName());
            if (con.getMetaData().getDriverName().toLowerCase().indexOf("oracle") != -1)  //�ж��ǲ���ORACLE
            {
                dbtype = 1;
            }
            DatabaseMetaData dbMeta = con.getMetaData();
            ResultSet pkRSet = dbMeta.getPrimaryKeys(null, null, tablename);
            if (pkRSet.next()) {
                pkname = ((String) pkRSet.getObject(4)).toLowerCase();
            }
            ResultSet result = stmt.executeQuery("select * from " + tablename);
            ResultSetMetaData rsmd = result.getMetaData();
            String listname[][] = new String[rsmd.getColumnCount()][2];     //���������������
            String rsname[] = new String[rsmd.getColumnCount()];
            String getset[][] = new String[rsmd.getColumnCount()][2];
            int type = 4;
            pandy[0] = tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " \n{\n\t";
            /**
             * �õ����е�����
             */
            for (i = 1; i <= rsmd.getColumnCount(); i++) {
                listname[i - 1][0] = rsmd.getColumnName(i).toLowerCase();
                type = rsmd.getColumnType(i);
                System.out.println(listname[i - 1][0] + ":" + type); //���ÿ�����ͱ��
                String temp = "";
                //��ɸ������͵��ж����û������GET��SET������
                if (type == 4 || type == 5 || type == 2|| type == -7) {
                    if (pkname.equals(listname[i - 1][0].toLowerCase())) {
                        pk = 1;
//                        if (dbtype == 1) {
                            temp = "@Id\n\t@Prev({\n\t\t@SQL(db = DB.ORACLE, value=\"SELECT " + tablename.toUpperCase() + "_S.nextval FROM dual\")\n\t})\n\t";
                            topstr += "import org.nutz.dao.entity.annotation.Id;\nimport org.nutz.dao.entity.annotation.Prev;\nimport org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;\n";
//                        } else {
//                            topstr += "import org.nutz.dao.entity.annotation.Id;\n";

//                        }
                    }
                    listname[i - 1][1] = "int ";
                    pandy[0] += "@Column\n\t" + temp + "private int " + listname[i - 1][0] + ";\n\t";
//                    rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "=rs.getInt(\"" + listname[i - 1][0] + "\");\n";
                    getset[i - 1][0] = "\tpublic int get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(int " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";
                } else if(type==-5){
                    if (pkname.equals(listname[i - 1][0].toLowerCase())) {
                        pk = 3;
                        temp = "@Id\n\t@Prev({\n\t\t@SQL(db = DB.ORACLE, value=\"SELECT " + tablename.toUpperCase() + "_S.nextval FROM dual\")\n\t})\n\t";
                        topstr += "import org.nutz.dao.entity.annotation.Id;\nimport org.nutz.dao.entity.annotation.Prev;\nimport org.nutz.dao.entity.annotation.SQL;import org.nutz.dao.DB;\n";

                    }
                    listname[i - 1][1] = "long ";
                    pandy[0] += "@Column\n\t" + temp + "private long " + listname[i - 1][0] + ";\n\t";
                    getset[i - 1][0] = "\tpublic long get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(long " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";

                }
                else if (type == 6||type == 7) //mssql float��
                {
                    listname[i - 1][1] = "double ";
                    pandy[0] += "@Column\n\tprivate double " + listname[i - 1][0] + ";\n\t";
//                    rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "=rs.getDouble(\"" + listname[i - 1][0] + "\");\n";
                    getset[i - 1][0] = "\tpublic double get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(double " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";

                } else if (type == 12) {

                    if (pkname.equals(listname[i - 1][0].toLowerCase())) {
                        pk = 2;
                        temp = "@Name\n\t";
                        topstr += "import org.nutz.dao.entity.annotation.Name;\n";
                    }
                    listname[i - 1][1] = "String ";
                    pandy[0] += "@Column\n\t" + temp + "private String " + listname[i - 1][0] + ";\n\t";
//                    rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "=rs.getString(\"" + listname[i - 1][0] + "\");\n";
                    getset[i - 1][0] = "\tpublic String get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(String " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";
                } else if (type == 19)   //MYSQL ��������
                {
                    listname[i - 1][1] = "java.sql.Date ";
                    pandy[0] += "@Column\n\tprivate java.sql.Date " + listname[i - 1][0] + ";\n\t";
//                    rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "=rs.getDate(\"" + listname[i - 1][0] + "\");\n";
                    getset[i - 1][0] = "\tpublic java.sql.Date get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(java.sql.Date " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";

                } else if (type == 93)   //ΪORACLEʱ����ĵĴ���
                {
                    listname[i - 1][1] = "java.sql.Date ";
                    pandy[0] += "@Column\n\tprivate java.sql.Date " + listname[i - 1][0] + ";\n\t";
//                    rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "=rs.getDate(\"" + listname[i - 1][0] + "\");\n";
                    getset[i - 1][0] = "\tpublic java.sql.Date get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(java.sql.Date " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";

                } else if (type == 2005)   //ORCALE���ֶδ���
                {
                    listname[i - 1][1] = "Clob ";
                    pandy[0] += "@Column\n\tprivate String " + listname[i - 1][0] + ";\n\t";
//                    if (dbtype == 1)
//                    {
//                        rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "= DBObject.getClobBody(rs,\"" + listname[i - 1][0] + "\");\n";
//                    }
//                    else
//                    {
//                        rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "= DBObject.getClobString(rs,\"" + listname[i - 1][0] + "\");\n";
//                    }
                    getset[i - 1][0] = "\tpublic String get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(String " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";
                } else {
                    listname[i - 1][1] = "String ";
                    pandy[0] += "@Column\n\tprivate String " + listname[i - 1][0] + ";\n\t";
//                    rsname[i - 1] = "\t\t\t" + listname[i - 1][0] + "=rs.getString(\"" + listname[i - 1][0] + "\");\n";
                    getset[i - 1][0] = "\tpublic String get" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "()\n\t{\n\t\treturn " + listname[i - 1][0] + ";\n\t}\n";
                    getset[i - 1][1] = "\tpublic void set" + listname[i - 1][0].toUpperCase().substring(0, 1) + listname[i - 1][0].toLowerCase().substring(1) + "(String " + listname[i - 1][0] + ")\n\t{\n\t\tthis." + listname[i - 1][0] + "=" + listname[i - 1][0] + ";\n\t}\n";
                }

            }
//            pandy[0] += "\n\tpublic " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "()\n\t{\n\t}\n\tpublic " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "(ResultSet rs)\n\t{\n\t\ttry\n\t\t{\n";

//            for (i = 0; i < listname.length; i++)
//            {
//                pandy[0] += rsname[i];
//            }
//            pandy[0] += "\t\t}\n\t\tcatch(Exception e)\n\t\t{\n\t\t\tGlobals.Logger(\"��������\" + e, 2);\n\t\t}\n\t}\n\n";

            topstr += "/**\n" +
                    "* @author "+author+"\n" +
                    "* @time   " + DateUtil.date2str(new java.util.Date()) + "\n" +
                    "*/\n@Table(\"" + tablename + "\")\npublic class ";
            for (i = 0; i < listname.length; i++) {
                pandy[0] += getset[i][0] + getset[i][1];
            }
            pandy[0] = topstr + pandy[0] + "\n}";
//
//            //���������ɴ���
//            String classinfo = tablename.toLowerCase();
//            String extS = "";
//            int cur = 0;
//            int clob = 0;
//            if (dbtype == 1)
//            {
//                pandy[1] = "package " + pagename + ";\n\nimport web.db.*;\nimport web.sys.Globals;\nimport java.sql.*;\nimport web.util.DateUtil;\nimport java.util.ArrayList;\nimport java.util.Vector;\n\n/**\n" + "* �����ˣ�Wizzer\n* ʱ�䣺" + DateUtil.date2str(new java.util.Date()) + "\n" + "*/\n\npublic class " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl extends ObjectCtl\n{\n\t/**\n\t* ���Ӽ�¼������ʾ����������ݿ��޸�sql��䣩\n\t**/\n\tpublic static int add(Connection con," + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " " + classinfo + ")\n\t{\n\t\tDBObject obj=new DBObject();\n\t\tobj.setAutoCommit(con,false);\n\t\ttry\n\t\t{\n\t\t\tString sql=\"begin insert into " + classinfo + " (";
//            }
//            else
//            {
//                pandy[1] = "package " + pagename + ";\n\nimport web.db.*;\nimport web.sys.Globals;\nimport java.sql.*;\nimport web.util.DateUtil;\nimport java.util.ArrayList;\nimport java.util.Vector;\n\n/**\n" + "* �����ˣ�Wizzer\n* ʱ�䣺" + DateUtil.date2str(new java.util.Date()) + "\n" + "*/\n\npublic class " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl extends ObjectCtl\n{\n\t/**\n\t* ���Ӽ�¼������ʾ����������ݿ��޸�sql��䣩\n\t**/\n\tpublic static int add(Connection con," + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " " + classinfo + ")\n\t{\n\t\tDBObject obj=new DBObject();\n\t\tobj.setAutoCommit(con,false);\n\t\ttry\n\t\t{\n\t\t\tString sql=\"insert into " + classinfo + " (";
//            }
//            for (i = 0; i < listname.length; i++)
//            {
//                if ((listname[i][0].toUpperCase()).equals("ID"))
//                {
//                    continue;
//                }
//                pandy[1] += listname[i][0] + ",";
//            }
//            pandy[1] = pandy[1].substring(0, pandy[1].length() - 1) + ") values (";
//            for (i = 0; i < listname.length; i++)
//            {
//                if ((listname[i][0].toUpperCase()).equals("ID"))
//                {
//                    continue;
//                }
//
//                cur = cur + 1;
//
//                if (dbtype == 0)
//                {
//                    if (listname[i][1].equals("int "))
//                    {
//                        // pandy[1] += "+" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setInt(" + cur + "," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "());\n\t\t\t";
//
//                    }
//                    else if (listname[i][1].equals("String "))
//                    {
//                        //pandy[1] += "+" + "DBUtil.getMssqlSaveString(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()));\n\t\t\t";
//
//                    }
//                    else if (listname[i][1].equals("double "))
//                    {
//                        //pandy[1] += "+" + "DBUtil.getMssqlSaveString(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\"\"));\n\t\t\t";
//
//                    }
//                    else if (listname[i][1].equals("java.sql.Date "))
//                    {
//                        pandy[1] += "?,";
//                        extS += "obj.setDate(" + cur + "," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "());\n\t\t\t";
//
//                    }
//                    else if (listname[i][1].equals("Clob "))
//                    {
//                        //pandy[1] += "+" + "DBUtil.getMssqlSaveString(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()));\n\t\t\t";
//                    }
//                    else
//                    {
//                        pandy[1] += "?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())\n\t\t\t";
//
//                    }
//                }
//                else
//                {
//                    if (listname[i][1].equals("int "))
//                    {
//                        //pandy[1] += "+" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setInt(" + cur + "," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "());\n\t\t\t";
//                    }
//                    else if (listname[i][1].equals("String "))
//                    {
//                        // pandy[1] += "+" + "DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()));\n\t\t\t";
//                    }
//                    else if (listname[i][1].equals("double "))
//                    {
//                        // pandy[1] += "+" + "DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\"\"));\n\t\t\t";
//                    }
//                    else if (listname[i][1].equals("java.sql.Date "))
//                    {
//                        pandy[1] += "\"+" + "DateUtil.oraDateFormat+\",";
//                        extS += "obj.setString(" + cur + ",DateUtil.date2str(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()));\n\t\t\t";
//
//                    }
//                    else if (listname[i][1].equals("Clob "))
//                    {
//                        pandy[1] += "\"+" + "DBObject.EMPTY_CLOB+\",";
//                        clob = i;
//                        // extS += "obj.setString(" + cur + ",DateUtil.date2str(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()));\n\t\t\t";
//                        cur = cur - 1;
//                    }
//                    else
//                    {
//                        // pandy[1] += "+" + "DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",\"";
//                        pandy[1] += "?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())\n\t\t\t";
//                    }
//                }
//            }
//            cur = cur + 1;
//            if (dbtype == 1)
//            {
//                pandy[1] = pandy[1].substring(0, pandy[1].length() - 1) + ")  returning " + listname[0][0] + " into ? ;end; \";\n\t\t\tobj.prepareCallStatement(con,sql);\n\t\t\t" + extS + "\n\t\t\t";
//
//                if (listname[0][1].equals("int "))
//                {
//                    pandy[1] += "obj.registerOutParameter(" + cur + ", Types.INTEGER);\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t" + classinfo + ".set" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(obj.getCst().getInt(" + cur + "));\n\t\t\t";
//                }
//                else if (listname[0][1].equals("String "))
//                {
//                    pandy[1] += "obj.registerOutParameter(" + cur + ", Types.VARCHAR);\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t" + classinfo + ".set" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(obj.getCst().getString(" + cur + "));\n\t\t\t";
//                }
//                else if (listname[i][1].equals("double "))
//                {
//                    pandy[1] += "obj.registerOutParameter(" + cur + ", Types.DOUBLE);\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t" + classinfo + ".set" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(obj.getCst().getDouble(" + cur + "));\n\t\t\t";
//                }
//                if (clob != 0)
//                {
//                    if (listname[0][1].equals("int "))
//                    {
//                        pandy[1] += "obj.setClobBody(con,\"SELECT note from " + classinfo + " WHERE " + listname[0][0].toUpperCase() + "=\" + " + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "() + \" FOR UPDATE\", \"" + listname[clob][0].toUpperCase() + "\"," + classinfo + ".get" + listname[clob][0].toUpperCase().substring(0, 1) + listname[clob][0].toLowerCase().substring(1) + "()); ";
//
//                    }
//                    else
//                    {
//                        pandy[1] += "obj.setClobBody(con,\"SELECT note from " + classinfo + " WHERE " + listname[0][0].toUpperCase() + "='\" + " + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "() + \"' FOR UPDATE\", \"" + listname[clob][0].toUpperCase() + "\"," + classinfo + ".get" + listname[clob][0].toUpperCase().substring(0, 1) + listname[clob][0].toLowerCase().substring(1) + "()); ";
//                    }
//
//                }
//            }
//            else
//            {
//                pandy[1] = pandy[1].substring(0, pandy[1].length() - 1) + ") \";\n\t\t\tobj.prepareStatement(con,sql);\n\t\t\t" + extS + "\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t";
//
//            }
//            pandy[1] += "\n\t\t\tobj.commitCon(con);\n\t\t\treturn returnvalue;\n\t\t} catch (Exception e)\n\t\t{\n\t\t\tGlobals.Logger(\"��������\" + e, 2);  //To change body of catch statement use File | Settings | File Templates.\n\t\t\treturn -1;\n\t\t} finally \n\t\t{\n\t\t\tobj.freecon();\n\t\t}\n\t}\n\n\t/**\n\t* ����һ����¼������ʾ����������ݿ��޸�sql��䣩\n\t**/\n\tpublic static int update(Connection con," + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " " + classinfo + ")\n\t{\n\t\tDBObject obj=new DBObject();\n\t\tobj.setAutoCommit(con,false);\n\t\ttry\n\t\t{\n\t\t\tString sql=\"update " + classinfo + "  set ";
//            cur = 0;
//            extS = "";
//            for (i = 0; i < listname.length; i++)
//            {
//                if ((listname[i][0].toUpperCase()).equals("ID") || listname[i][1].equals("Clob "))
//                {
//                    continue;
//                }
//
//                cur = cur + 1;
//
//                if (dbtype == 0)
//                {
//                    if (listname[i][1].equals("int "))
//                    {
//                        //pandy[1] += listname[i][0] + "=\"+" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\",";
//                        pandy[1] += listname[i][0] + "=?,";
//                        extS += "obj.setInt(" + cur + "," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "());\n\t\t\t";
//
//
//                    }
//                    else if (listname[i][1].equals("String ")||listname[i][1].equals("double "))
//                    {
//                        //   pandy[1] += listname[i][0] + "=\"+DBUtil.getMssqlSaveString(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",";
//
//                        pandy[1] += listname[i][0] + "=?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\"\"));\n\t\t\t";
//
//                    }
//                    else if (listname[i][1].equals("java.sql.Date "))
//                    {
//                        //  pandy[1] += listname[i][0] + "=\"+DateUtil.oraDateFormat+\",";
//                        pandy[1] += listname[i][0] + "=?,";
//                        extS += "obj.setDate(" + cur + "," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "());\n\t\t\t";
//
//
//                    }
//                    else
//                    {
//                        //   pandy[1] += listname[i][0] + "=\"+DBUtil.getMssqlSaveString(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",";
//                        pandy[1] += listname[i][0] + "=?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())\n\t\t\t";
//
//
//                    }
//                }
//                else
//                {
//                    if (listname[i][1].equals("int "))
//                    {
//                        //pandy[1] += listname[i][0] + "=\"+" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\",";
//                        pandy[1] += listname[i][0] + "=?,";
//                        extS += "obj.setInt(" + cur + "," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "());\n\t\t\t";
//                    }
//                    else if (listname[i][1].equals("String ")||listname[i][1].equals("double "))
//                    {
//                        //pandy[1] += listname[i][0] + "=\"+DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",";
//                        pandy[1] += listname[i][0] + "=?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()+\"\"));\n\t\t\t";
//                    }
//                    else if (listname[i][1].equals("java.sql.Date "))
//                    {
//                        //pandy[1] += listname[i][0]+ "=\"+DateUtil.oraDateFormat+\",";
//                        pandy[1] += listname[i][0] + "=\"+DateUtil.oraDateFormat+\",";
//                        extS += "obj.setString(" + cur + ",DateUtil.date2str(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()));\n\t\t\t";
//
//                    }
//                    else
//                    {
//                        //pandy[1] += listname[i][0] + "=\"+DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())+\",";
//                        pandy[1] += listname[i][0] + "=?,";
//                        extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "())\n\t\t\t";
//                    }
//                }
//            }
//            cur++;
//            if (listname[0][1].equals("int "))
//            {
//                if (dbtype == 0)
//                {
//                    pandy[1] = pandy[1].substring(0, pandy[1].length() - 1) + " where " + listname[0][0] + "=?\";";
//                    extS += "obj.setInt(" + cur + "," + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "());\n\t\t\t";
//
//                }
//                else
//                {
//                    pandy[1] = pandy[1].substring(0, pandy[1].length() - 1) + " where " + listname[0][0] + "=\"+" + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "();";
//                }
//            }
//            else
//            {
//                if (dbtype == 0)
//                {
//                    pandy[1] = pandy[1].substring(0, pandy[1].length() - 1) + " where " + listname[0][0] + "=?\";";
//                    extS += "obj.setString(" + cur + ",DBUtil.getSaveStringNoQuote(" + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "()));\n\t\t\t";
//
//                }
//                else
//                {
//                    pandy[1] = pandy[1].substring(0, pandy[1].length() - 1) + " where " + listname[0][0] + "=\"+DBUtil.getOracleSaveString(" + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "()," + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "());";
//                }
//            }
//            /**
//             * ��ɻ������������޸ġ�ɾ������
//             */
//            String clobstr = "";
//            if (clob != 0)
//            {
//                if (listname[0][1].equals("int "))
//                {
//                    clobstr += "obj.setClobBody(con,\"SELECT note from " + classinfo + " WHERE " + listname[0][0].toUpperCase() + "=\" + " + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "() + \" FOR UPDATE\", \"" + listname[clob][0].toUpperCase() + "\"," + classinfo + ".get" + listname[clob][0].toUpperCase().substring(0, 1) + listname[clob][0].toLowerCase().substring(1) + "());\n\t\t\t";
//                }
//                else
//                {
//                    clobstr += "obj.setClobBody(con,\"SELECT note from " + classinfo + " WHERE " + listname[0][0].toUpperCase() + "='\" + " + classinfo + ".get" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "() + \"' FOR UPDATE\", \"" + listname[clob][0].toUpperCase() + "\"," + classinfo + ".get" + listname[clob][0].toUpperCase().substring(0, 1) + listname[clob][0].toLowerCase().substring(1) + "());\n\t\t\t";
//                }
//            }
//            if (listname[0][1].equals("int "))
//            {
//                if (dbtype == 1)
//                {
//                    pandy[1] += "\n\t\t\tobj.prepareCallStatement(con,sql);\n\t\t\t" + extS + "\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t" + clobstr + "obj.commitCon(con);\n\t\t\treturn returnvalue;\n\t\t} catch (Exception e)\n\t\t{\n\t\t\tGlobals.Logger(\"��������\" + e, 2);  //To change body of catch statement use File | Settings | File Templates.\n\t\t\treturn -1;\n\t\t}\n\t\t finally \n\t\t{\n\t\t\tobj.freecon();\n\t\t}\n\t}\n\n\t/**\n\t* ɾ��һ����¼������ʾ����������ݿ��ֶ�����\n\t**/\n\tpublic static int deleteById(Connection con,int id)\n\t{\n\t\treturn ObjectCtl.deleteById(con,\"" + classinfo + "\",\"id\",id);\n\t}\n\n\t/**\n\t* ����ɾ����¼������ʾ����������ݿ��ֶ�����\n\t**/\n\tpublic static int deleteByIds(Connection con,String[] ids)\n\t{\n\t\treturn ObjectCtl.deleteByIds(con,\"" + classinfo + "\",\"id\",ids);\n\t}\n\n\t";
//                }
//                else
//                {
//                    pandy[1] += "\n\t\t\tobj.prepareStatement(con,sql);\n\t\t\t" + extS + "\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t" + clobstr + "obj.commitCon(con);\n\t\t\treturn returnvalue;\n\t\t} catch (Exception e)\n\t\t{\n\t\t\tGlobals.Logger(\"��������\" + e, 2);  //To change body of catch statement use File | Settings | File Templates.\n\t\t\treturn -1;\n\t\t}\n\t\t finally \n\t\t{\n\t\t\tobj.freecon();\n\t\t}\n\t}\n\n\t/**\n\t* ɾ��һ����¼������ʾ����������ݿ��ֶ�����\n\t**/\n\tpublic static int deleteById(Connection con,int id)\n\t{\n\t\treturn ObjectCtl.deleteById(con,\"" + classinfo + "\",\"id\",id);\n\t}\n\n\t/**\n\t* ����ɾ����¼������ʾ����������ݿ��ֶ�����\n\t**/\n\tpublic static int deleteByIds(Connection con,String[] ids)\n\t{\n\t\treturn ObjectCtl.deleteByIds(con,\"" + classinfo + "\",\"id\",ids);\n\t}\n\n\t";
//
//                }
//            }
//            else
//            {
//                if (dbtype == 1)
//                {
//                    pandy[1] += "\n\t\t\tobj.prepareCallStatement(con,sql);\n\t\t\t" + extS + "\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t" + clobstr + "obj.commitCon(con);\n\t\t\treturn returnvalue;\n\t\t} catch (Exception e)\n\t\t{\n\t\t\tGlobals.Logger(\"��������\" + e, 2);  //To change body of catch statement use File | Settings | File Templates.\n\t\t\treturn -1;\n\t\t}\n\t\t finally \n\t\t{\n\t\t\tobj.freecon();\n\t\t}\n\t}\n\n\t";
//                }
//                else
//                {
//                    pandy[1] += "\n\t\t\tobj.prepareStatement(con,sql);\n\t\t\t" + extS + "\n\t\t\tint returnvalue=obj.executeUpdate();\n\t\t\t" + clobstr + "obj.commitCon(con);\n\t\t\treturn returnvalue;\n\t\t} catch (Exception e)\n\t\t{\n\t\t\tGlobals.Logger(\"��������\" + e, 2);  //To change body of catch statement use File | Settings | File Templates.\n\t\t\treturn -1;\n\t\t}\n\t\t finally \n\t\t{\n\t\t\tobj.freecon();\n\t\t}\n\t}\n\n\t";
//
//                }
//            }
//            pandy[1] += "/**\n\t* �����ַ�������ɾ����¼������ʾ����������ݿ��ֶ�����\n\t**/\n\tpublic static int deleteByName(Connection con,String colname,String name)\n\t{\n\t\treturn ObjectCtl.deleteByName(con,\"" + classinfo + "\",colname,name);\n\t}\n\n\t/**\n\t* �����ַ�����������ɾ����¼ ����ʾ����������ݿ��ֶ�����\n\t**/\n\tpublic static int deleteByNames(Connection con,String colname,String[] names)\n\t{\n\t\treturn ObjectCtl.deleteByNames(con,\"" + classinfo + "\",colname,names);\n\t}\n\n\t";
//            /**
//             * ���ͨ��ID�õ���ϸ���ϴ���
//             */
//            if (listname[0][1].equals("String "))
//            {
//                pandy[1] += "/**\n\t* ͨ��" + listname[0][0] + "�õ���ϸ����\n\t**/\n\tpublic static " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " detailBy" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(Connection con,String name)\n\t{\n\t\tString sql=\"SELECT * FROM " + classinfo + " where " + listname[0][0] + "='\"+name+\"'\";\n\t\treturn (" + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + ")ObjectCtl.detail(con,sql,new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "());\n\t}\n\n";
//            }
//            else
//            {
//                pandy[1] += "/**\n\t* ͨ��" + listname[0][0] + "�õ���ϸ����\n\t**/\n\tpublic static " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " detailBy" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(Connection con,int id)\n\t{\n\t\tString sql=\"SELECT * FROM " + classinfo + " where " + listname[0][0] + "=\"+id;\n\t\treturn (" + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + ")ObjectCtl.detail(con,sql,new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "());\n\t}\n\n";
//            }
//
//            /**
//             * �Զ���SQL�������ɾ���Ĳ���
//             */
//            pandy[1] += "\t/**\n\t* �Զ���SQL�������ɾ���Ĳ���������ʾ�������SQL��䣩\n\t**/\n\tpublic static int executeBySql(Connection con,String sql)\n\t{\n\t\treturn ObjectCtl.executeUpdateBySql(con,sql);\n\t}\n\n";
//
//            /**
//             * ͨ���Զ���sql�õ���ϸ����
//             **/
//            pandy[1] += "\t/**\n\t* ͨ���Զ���sql�õ���ϸ����\n\t**/\n\tpublic static " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " detailBySql(Connection con,String sql)\n\t{\n\t\treturn (" + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + ")ObjectCtl.detail(con,sql,new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "());\n\t}\n\n";
//
//            /**
//             * ִ�ж���SQL���
//             **/
//            pandy[1] += "\t/**\n\t* ִ�ж���SQL���\n\t**/\n\tpublic static boolean executeMulSql(Connection con,String sql[])\n\t{\n\t\treturn ObjectCtl.executeMulSql(con,sql);\n\t}\n\n";
//
//            /**
//             * ִ�ж���SQL���
//             **/
//            pandy[1] += "\t/**\n\t* ִ�ж���SQL���\n\t**/\n\tpublic static boolean executeMulSql(Connection con,ArrayList sql)\n\t{\n\t\treturn ObjectCtl.executeMulSql(con,sql);\n\t}\n";
//
//            /**
//             * �б�
//             **/
//            pandy[1] += "\n\t/**\n\t* �б�\n\t**/\n\tpublic static ArrayList list(Connection con,String sql)\n\t{\n\t\treturn  ObjectCtl.list(con,sql,new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "());\n\t}\n";
//
//            /**
//             * �б��ҳ
//             **/
//            pandy[1] += "\n\t/**\n\t* �б��ҳ\n\t**/\n\tpublic static ArrayList listPage(Connection con,String sql,int curpage,int pagesize)\n\t{\n\t\treturn  ObjectCtl.listPage(con,sql,curpage,pagesize,new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "());\n\t}\n";
//
//
//            /**
//             * �б� , ���Ϊ���ж���
//             **/
//            pandy[1] += "\n\t/**\n\t* �б�, ���Ϊ���ж���\n\t**/\n\tpublic static ArrayList listMul(Connection con,String sql)\n\t{\n\t\treturn  ObjectCtl.getMulRowValue(con,sql);\n\t}\n";
//
//           /**
//             * �б��ҳ�����Ϊ���ж���
//             **/
//            pandy[1] += "\n\t/**\n\t* �б��ҳ�����Ϊ���ж���\n\t**/\n\tpublic static ArrayList listPageMul(Connection con,String sql,int curpage,int pagesize)\n\t{\n\t\treturn  ObjectCtl.getMulRowValuePage(con,sql,curpage,pagesize);\n\t}\n";
//
//            /**
//             * ��������б����
//             */
//
//            pandy[1] += "}";

            //Actionҳ���������
            pandy[2] = "";
            pandy[2] += "package " + pagename + ";";
            pandy[2] += "\nimport javax.servlet.http.HttpServletRequest;";
            pandy[2] += "\nimport javax.servlet.http.HttpSession;\nimport org.apache.commons.lang.StringUtils;\n";
            pandy[2] += "\nimport org.nutz.dao.*;";
            pandy[2] += "\nimport org.nutz.dao.sql.Criteria;";
            pandy[2] += "\nimport org.nutz.dao.util.cri.SqlExpressionGroup;";
            pandy[2] += "\nimport org.nutz.ioc.loader.annotation.Inject;";
            pandy[2] += "\nimport org.nutz.ioc.loader.annotation.IocBean;";
            pandy[2] += "\nimport org.nutz.mvc.annotation.At;";
            pandy[2] += "\nimport org.nutz.mvc.annotation.By;";
            pandy[2] += "\nimport org.nutz.mvc.annotation.Filters;";
            pandy[2] += "\nimport org.nutz.mvc.annotation.Ok;";
            pandy[2] += "\nimport org.nutz.mvc.annotation.Param; \n";
            pandy[2] += "\nimport cn.xuetang.common.action.BaseAction;";
            pandy[2] += "\nimport cn.xuetang.common.filter.GlobalsFilter;";
            pandy[2] += "\nimport cn.xuetang.common.filter.UserLoginFilter;\n";
            String tname = tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1);
            pandy[2] += "\nimport cn.xuetang.modules."+modelname.toLowerCase()+".bean." + tname+";";
            pandy[2] += "\n\n";
            pandy[2] += "/**\n * @author " + author + "\n * @time " + DateUtil.date2str(new java.util.Date()) + "\n * \n */";
            pandy[2] += "\n@IocBean";
            pandy[2] += "\n@At(\"/private/" + modelname.toLowerCase().replace(".","/") + "/" + tname.toLowerCase() + "\")";
            pandy[2] += "\n@Filters({ @By(type = GlobalsFilter.class), @By(type = UserLoginFilter.class) })";
            pandy[2] += "\npublic class " + tname + "Action extends BaseAction {";
            pandy[2] += "\n\t@Inject";
            pandy[2] += "\n\tprotected Dao dao;\n";
            pandy[2] += "\n\t@At(\"\")";
            pandy[2] += "\n\t@Ok(\"->:/private/" + modelname.toLowerCase().replace(".","/") + "/" + tname + ".html\")";
            pandy[2] += "\n\tpublic void index(@Param(\"sys_menu\") String sys_menu,HttpServletRequest req) {";
            pandy[2] += "\n\t\treq.setAttribute(\"sys_menu\",sys_menu);";
            pandy[2] += "\n\t}";
            pandy[2] += "\n\t";
            pandy[2] += "\n\t@At";
            pandy[2] += "\n\t@Ok(\"->:/private/" + modelname.toLowerCase().replace(".","/") + "/" + tname + "Add.html\")";
            pandy[2] += "\n\tpublic void toadd() {";
            pandy[2] += "\n\t";
            pandy[2] += "\n\t}";
            pandy[2] += "\n\t";
            pandy[2] += "\n\t@At";
            pandy[2] += "\n\t@Ok(\"raw\")";
            pandy[2] += "\n\tpublic boolean add(@Param(\"..\") " + tname + " " + tname.toLowerCase() + ") {";
            pandy[2] += "\n\t\treturn daoCtl.add(dao," + tname.toLowerCase() + ");";
            pandy[2] += "\n\t}";
            pandy[2] += "\n\t";


            if (pk == 1) {
                pandy[2] += "\n\t//@At";
                pandy[2] += "\n\t//@Ok(\"raw\")";
                pandy[2] += "\n\t//public int add(@Param(\"..\") " + tname + " " + tname.toLowerCase() + ") {";
                pandy[2] += "\n\t//\treturn daoCtl.addT(dao," + tname.toLowerCase() + ").get" + pkname.toUpperCase().substring(0, 1) + pkname.toLowerCase().substring(1) + "();";
                pandy[2] += "\n\t//}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\t@Ok(\"json\")";
                pandy[2] += "\n\tpublic " + tname + " view(@Param(\"" + pkname.toLowerCase() + "\") int " + pkname.toLowerCase() + ") {";
                pandy[2] += "\n\t\treturn daoCtl.detailById(dao," + tname + ".class, " + pkname.toLowerCase() + ");";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";

                pandy[2] += "\n\t@At";
                pandy[2] += "\n\t@Ok(\"->:/private/" + modelname.toLowerCase().replace(".","/") + "/" + tname + "Update.html\")";
                pandy[2] += "\n\tpublic "+tname+" toupdate(@Param(\"" + pkname.toLowerCase() + "\") int " + pkname.toLowerCase() + ", HttpServletRequest req) {";
                pandy[2] += "\n\t\treturn daoCtl.detailById(dao, " + tname + ".class, " + pkname.toLowerCase() + ");//html:obj";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
            } else if (pk == 2) {
                pandy[2] += "\n\t//@At";
                pandy[2] += "\n\t//@Ok(\"raw\")";
                pandy[2] += "\n\t//public String add(@Param(\"..\") " + tname + " " + tname.toLowerCase() + ") {";
                pandy[2] += "\n\t//\treturn daoCtl.addT(dao," + tname.toLowerCase() + ").get" + pkname.toUpperCase().substring(0, 1) + pkname.toLowerCase().substring(1) + "();";
                pandy[2] += "\n\t//}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\t@Ok(\"json\")";
                pandy[2] += "\n\tpublic " + tname + " view(@Param(\"" + pkname.toLowerCase() + "\") String " + pkname.toLowerCase() + ") {";
                pandy[2] += "\n\t\treturn daoCtl.detailByName(dao," + tname + ".class, " + pkname.toLowerCase() + ");";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";

                pandy[2] += "\n\t@At";
                pandy[2] += "\n\t@Ok(\"->:/private/" + modelname.toLowerCase().replace(".","/") + "/" + tname + "Update.html\")";
                pandy[2] += "\n\tpublic "+tname+" toupdate(@Param(\"" + pkname.toLowerCase() + "\") String " + pkname.toLowerCase() + ", HttpServletRequest req) {";
                pandy[2] += "\n\t\treturn daoCtl.detailByName(dao, " + tname + ".class, " + pkname.toLowerCase() + ");//html:obj";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
            }else  if (pk == 3) {
                pandy[2] += "\n\t//@At";
                pandy[2] += "\n\t//@Ok(\"raw\")";
                pandy[2] += "\n\t//public long add(@Param(\"..\") " + tname + " " + tname.toLowerCase() + ") {";
                pandy[2] += "\n\t//\treturn daoCtl.addT(dao," + tname.toLowerCase() + ").get" + pkname.toUpperCase().substring(0, 1) + pkname.toLowerCase().substring(1) + "();";
                pandy[2] += "\n\t//}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\t@Ok(\"json\")";
                pandy[2] += "\n\tpublic " + tname + " view(@Param(\"" + pkname.toLowerCase() + "\") long " + pkname.toLowerCase() + ") {";
                pandy[2] += "\n\t\treturn daoCtl.detailById(dao," + tname + ".class, " + pkname.toLowerCase() + ");";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";

                pandy[2] += "\n\t@At";
                pandy[2] += "\n\t@Ok(\"->:/private/" + modelname.toLowerCase().replace(".","/") + "/" + tname + "Update.html\")";
                pandy[2] += "\n\tpublic "+tname+" toupdate(@Param(\"" + pkname.toLowerCase() + "\") long " + pkname.toLowerCase() + ", HttpServletRequest req) {";
                pandy[2] += "\n\t\treturn daoCtl.detailById(dao, " + tname + ".class, " + pkname.toLowerCase() + ");//html:obj";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
            }else {
                pandy[2] += "\n\t//@At";
                pandy[2] += "\n\t//@Ok(\"raw\")";
                pandy[2] += "\n\t//public String add(@Param(\"..\") " + tname + " " + tname.toLowerCase() + ") {";
                pandy[2] += "\n\t//\treturn daoCtl.addT(dao," + tname.toLowerCase() + ").getId();";
                pandy[2] += "\n\t//}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t//@At";
                pandy[2] += "\n\t//@Ok(\"json\")";
                pandy[2] += "\n\t//public " + tname + " view(@Param(\"id\") String id) {";
                pandy[2] += "\n\t\t//return daoCtl.detailByName(dao," + tname + ".class, id);";
                pandy[2] += "\n\t//}";
                pandy[2] += "\n\t";

                pandy[2] += "\n\t//@At";
                pandy[2] += "\n\t//@Ok(\"->:/private/" + modelname.toLowerCase().replace(".","/") + "/" + tname + "Update.html\")";
                pandy[2] += "\n\t//public "+tname+" toupdate(@Param(\"id\") String id, HttpServletRequest req) {";
                pandy[2] += "\n\t\t//return daoCtl.detailByName(dao, " + tname + ".class, id);//html:obj";
                pandy[2] += "\n\t//}";
                pandy[2] += "\n\t";
            }
            pandy[2] += "\n\t@At";
            pandy[2] += "\n\tpublic boolean update(@Param(\"..\") " + tname + " " + tname.toLowerCase() + ") {";
            pandy[2] += "\n\t\treturn daoCtl.update(dao, " + tname.toLowerCase() + ");";
            pandy[2] += "\n\t}";
            pandy[2] += "\n\t";
            if (pk == 1) {
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\tpublic boolean delete(@Param(\"" + pkname.toLowerCase() + "\") int " + pkname.toLowerCase() + ") {";
                pandy[2] += "\n\t\treturn daoCtl.deleteById(dao, " + tname + ".class, " + pkname.toLowerCase() + ");";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\tpublic boolean deleteIds(@Param(\"ids\") Integer[] ids) {";
                pandy[2] += "\n\t\treturn daoCtl.delete(dao, "+tname+".class, Cnd.where(\"id\", \"in\", ids)) > 0;";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
            } else if (pk == 2) {
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\tpublic boolean delete(@Param(\"" + pkname.toLowerCase() + "\") String " + pkname.toLowerCase() + ") {";
                pandy[2] += "\n\t\treturn daoCtl.deleteByName(dao, " + tname + ".class, " + pkname.toLowerCase() + ");";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\tpublic boolean deleteIds(@Param(\"ids\") String[] ids) {";
                pandy[2] += "\n\t\treturn daoCtl.delete(dao, "+tname+".class, Cnd.where(\"id\", \"in\", ids)) > 0;";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
            } else if (pk == 3) {
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\tpublic boolean delete(@Param(\"" + pkname.toLowerCase() + "\") long " + pkname.toLowerCase() + ") {";
                pandy[2] += "\n\t\treturn daoCtl.deleteById(dao, " + tname + ".class, " + pkname.toLowerCase() + ");";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\tpublic boolean deleteIds(@Param(\"ids\") Long[] ids) {";
                pandy[2] += "\n\t\treturn daoCtl.delete(dao, "+tname+".class, Cnd.where(\"id\", \"in\", ids)) > 0;";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
            } else {
                pandy[2] += "\n\t//@At";
                pandy[2] += "\n\t//public boolean delete(@Param(\"id\") String id) {";
                pandy[2] += "\n\t\t//return daoCtl.deleteById(dao, " + tname + ".class, id);";
                pandy[2] += "\n\t//}";
                pandy[2] += "\n\t";
                pandy[2] += "\n\t@At";
                pandy[2] += "\n\tpublic boolean deleteIds(@Param(\"ids\") String[] ids) {";
                pandy[2] += "\n\t\treturn daoCtl.delete(dao, "+tname+".class, Cnd.where(\"id\", \"in\", ids)) > 0;";
                pandy[2] += "\n\t}";
                pandy[2] += "\n\t";
            }
            pandy[2] += "\n\t@At";
            pandy[2] += "\n\t@Ok(\"raw\")";
            pandy[2] += "\n\tpublic String list(@Param(\"page\") int curPage, @Param(\"rows\") int pageSize){";
            pandy[2] += "\n\t\tCriteria cri = Cnd.cri();";
            pandy[2] += "\n\t\tcri.where().and(\"1\",\"=\",1);";
            pandy[2] += "\n\t\tcri.getOrderBy().desc(\""+pkname.toLowerCase()+"\");";
            pandy[2] += "\n\t\treturn daoCtl.listPageJson(dao, " + tname + ".class, curPage, pageSize, cri);";
            pandy[2] += "\n\t}";
            pandy[2] += "\n\n";
            pandy[2] += "}";
            pandy[2] += "";


//            pandy[2] += "\n\n\t/**\n\t * ִ����������\n\t */\n\tprivate Page add(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\tint result = -1;\n\t\ttry\n\t\t{\n\t\t\tresult = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.add(con, getObject(form));\n\t\t\tif (result != -1)\n\t\t\t{\n\t\t\t\tGlobals.Update(\"" + classinfo + "\");//��̬��״̬����\n\t\t\t\tform.addResult(\"msg\", \"���ӳɹ�!\");\n\t\t\t}\n\t\t\telse\n\t\t\t{\n\t\t\t\tform.addResult(\"msg\", \"����ʧ��!\");\n\t\t\t}\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\treturn list(form,module,user);\n\t}";
//            if (listname[0][1].equals("String "))
//            {
//                pandy[2] += "\n\n\t/**\n\t * �޸�ҳ��\n\t */\n\tprivate Page toupdate(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\ttry\n\t\t{\n\t\t\t" + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " obj = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.detailBy" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(con, StringUtil.null2String(form.get(\"id\")));\n\t\t\tform.addResult(\"obj\", obj);\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\treturn module.findPage(\"update\");\n\t}";
//            }
//            else
//            {
//                pandy[2] += "\n\n\t/**\n\t * �޸�ҳ��\n\t */\n\tprivate Page toupdate(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\ttry\n\t\t{\n\t\t\t" + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " obj = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.detailBy" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(con, StringUtil.StringToInt(StringUtil.null2String(form.get(\"id\"))));\n\t\t\tform.addResult(\"obj\", obj);\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\treturn module.findPage(\"update\");\n\t}";
//
//            }
//            pandy[2] += "\n\n\t/**\n\t * ִ���޸�����\n\t */\n\tprivate Page update(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\tint result = -1;\n\t\ttry\n\t\t{\n\t\t\tresult = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.update(con, getObject(form));\n\t\t\tif (result != -1)\n\t\t\t{\n\t\t\t\tGlobals.Update(\"" + classinfo + "\");//��̬��״̬����\n\t\t\t\tform.addResult(\"msg\", \"�޸ĳɹ�!\");\n\t\t\t}\n\t\t\telse\n\t\t\t{\n\t\t\t\tform.addResult(\"msg\", \"�޸�ʧ��!\");\n\t\t\t}\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\treturn list(form,module,user);\n\t}";
//
//            pandy[2] += "\n\n\t/**\n\t * ִ��ɾ������\n\t */\n\tprivate Page delete(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\tint deltype = StringUtil.StringToInt(StringUtil.null2String(form.get(\"deltype\")));\n\t\tint result = -1;\n\t\tint dbtype=StringUtil.StringToInt(StringUtil.null2String(Globals.SYS_DB_TYPE.get(\"default\")));\n\t\tString sql=\"\";\n\t\ttry\n\t\t{\n\t\t\tswitch (deltype)\n\t\t\t{\n\t\t\t\t";
//
//            if (listname[0][1].equals("int "))   //�����IDʱ
//            {
//                pandy[2] += "case 0:   //ͨ������ID\n\t\t\t\t\tresult = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.deleteById(con, StringUtil.StringToInt(StringUtil.null2String(form.get(\"id\"))));\n\t\t\t\t\tbreak;\n\t\t\t\tcase 1:  //ͨ�����ID\n\t\t\t\t\tString[] ids = StringUtil.null2String(form.get(\"checkids\")).split(\",\");\n\t\t\t\t\tresult = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.deleteByIds(con, ids);\n\t\t\t\t\tbreak;\n\t\t\t\t";
//            }
//            pandy[2] += " case 2:  //ͨ����������,����ݾ���Ҫ���޸�colname\n\t\t\t\t\tresult = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.deleteByName(con, \"colname\", StringUtil.null2String(form.get(\"id\")));\n\t\t\t\t\tbreak;\n\t\t\t\tcase 3:  //ͨ���������,����ݾ���Ҫ���޸�colname\n\t\t\t\t\tString[] names = StringUtil.null2String(form.get(\"checkids\")).split(\",\");\n\t\t\t\t\tresult = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.deleteByNames(con, \"colname\", names);\n\t\t\t\t\tbreak;\n\t\t\t\tcase 4:   //ͨ���Զ���SQL,����ݾ���Ҫ���޸�sql���\n\t\t\t\t\tswitch (dbtype)\n\t\t\t\t\t{\n\t\t\t\t\t\tcase 1:\n\t\t\t\t\t\t\tsql=\"delete from " + classinfo + "\";   //orcaleʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 2:\n\t\t\t\t\t\t\tsql=\"delete from " + classinfo + "\"; //sqlserverʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 3:\n\t\t\t\t\t\t\tsql=\"delete from " + classinfo + "\";  //mysqlʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t}\n\t\t\t\t\tresult = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.executeBySql(con, sql);\n\t\t\t\t\tbreak;\n\t\t\t\t}";
//
//            pandy[2] += "\n\t\t\tif (result != -1)\n\t\t\t{\n\t\t\t\tGlobals.Update(\"" + classinfo + "\");//��̬��״̬����\n\t\t\t\tform.addResult(\"msg\", \"ɾ���ɹ�!\");\n\t\t\t}\n\t\t\telse\n\t\t\t{\n\t\t\t\tform.addResult(\"msg\", \"ɾ��ʧ��!\");\n\t\t\t}\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\treturn list(form,module,user);\n\t}";
//
//            if (listname[0][1].equals("String "))
//            {
//                pandy[2] += "\n\n\t/**\n\t * ������Ϣ���ҳ��\n\t */\n\tprivate Page detail(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\tint deltype = StringUtil.StringToInt(StringUtil.null2String(form.get(\"deltype\")));\n\t\t" + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " obj = new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "();\n\t\tint dbtype=StringUtil.StringToInt(StringUtil.null2String(Globals.SYS_DB_TYPE.get(\"default\")));\n\t\tString sql=\"\";\n\t\ttry\n\t\t{\n\t\t\tswitch (deltype)\n\t\t\t{\n\t\t\t\tcase 0:   //ͨ������" + listname[0][0] + "\n\t\t\t\t\tobj = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.detailBy" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(con, StringUtil.null2String(form.get(\"id\"))); \n\t\t\t\t\tbreak;\n\t\t\t\tcase 1:   //ͨ���Զ���SQL,����ݾ���Ҫ���޸�sql���\n\t\t\t\t\t" + "switch (dbtype)\n\t\t\t\t\t{\n\t\t\t\t\t\tcase 1:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";   //orcaleʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 2:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\"; //sqlserverʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 3:\n\t\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";  //mysqlʱ�� ���\n\t\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t}" + "\n\t\t\t\t\tobj = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.detailBySql(con, sql);\n\t\t\t\t\tbreak;\n\t\t\t}\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\tform.addResult(\"obj\", obj);\n\t\treturn module.findPage(\"detail\");\n\t}";
//            }
//            else
//            {
//                pandy[2] += "\n\n\t/**\n\t * ������Ϣ���ҳ��\n\t */\n\tprivate Page detail(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\tint deltype = StringUtil.StringToInt(StringUtil.null2String(form.get(\"deltype\")));\n\t\t" + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " obj = new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "();\n\t\tint dbtype=StringUtil.StringToInt(StringUtil.null2String(Globals.SYS_DB_TYPE.get(\"default\")));\n\t\tString sql=\"\";\n\t\ttry\n\t\t{\n\t\t\tswitch (deltype)\n\t\t\t{\n\t\t\t\tcase 0:   //ͨ������" + listname[0][0] + "\n\t\t\t\t\tobj = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.detailBy" + listname[0][0].toUpperCase().substring(0, 1) + listname[0][0].toLowerCase().substring(1) + "(con, StringUtil.StringToInt(StringUtil.null2String(form.get(\"id\")))); \n\t\t\t\t\tbreak;\n\t\t\t\tcase 1:   //ͨ���Զ���SQL,����ݾ���Ҫ���޸�sql���\n\t\t\t\t\t" + "switch (dbtype)\n\t\t\t\t\t{\n\t\t\t\t\t\tcase 1:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";   //orcaleʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 2:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\"; //sqlserverʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 3:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";  //mysqlʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t}" + "\n\t\t\t\t\tobj = " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "Ctl.detailBySql(con, sql);\n\t\t\t\t\tbreak;\n\t\t\t}\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\tform.addResult(\"obj\", obj);\n\t\treturn module.findPage(\"detail\");\n\t}";
//            }
//
//            pandy[2] += "\n\n\t/**\n\t * �б�ҳ��\n\t */\n\tprivate Page list(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\tint listtype = StringUtil.StringToInt(StringUtil.null2String(form.get(\"listtype\")));\n\t\tArrayList list = new ArrayList();\n\t\tString sql = \"select * from " + classinfo + "\";\n\t\tint dbtype=StringUtil.StringToInt(StringUtil.null2String(Globals.SYS_DB_TYPE.get(\"default\")));\n\t\ttry\n\t\t{\n\t\t\tswitch (listtype)\n\t\t\t{\n\t\t\t\tcase 0:   //ͨ��sql\n\t\t\t\t\t" + "switch (dbtype)\n\t\t\t\t\t{\n\t\t\t\t\t\tcase 1:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";   //orcaleʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 2:\n\t\t\t\t\t\t\tsql=\"select *  from " + classinfo + "\";  //sqlserverʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 3:\n\t\t\t\t\t\t\tsql=\"select *  from " + classinfo + "\";  //mysqlʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t}" + "\n\t\t\t\t\tlist = "+tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1)+"Ctl.list(con, sql);\n\t\t\t\t\tbreak;\n\t\t\t\tcase 1:   //ͨ���Զ���SQL,����ݾ���Ҫ���޸�sql���,���ж���\n\t\t\t\t\t" + "switch (dbtype)\n\t\t\t\t\t{\n\t\t\t\t\t\tcase 1:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";   //orcaleʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 2:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\"; //sqlserverʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 3:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";  //mysqlʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t}\n\t\t\t\t\tlist = "+tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1)+"Ctl.listMul(con, sql);\n\t\t\t\t\tbreak;\n\t\t\t}\n\t\t\tform.addResult(\"list\", list);\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\treturn module.findPage(\"list\");\n\t}";
//
//            pandy[2] += "\n\n\t/**\n\t * �б�ҳ��\n\t */\n\tprivate Page listpage(WebForm form, Module module, Sys_user user)\n\t{\n\n\t\tConnection con = DBConnector.getconecttion(); //ȡ��һ�����ݿ�����\n\t\tint listtype = StringUtil.StringToInt(StringUtil.null2String(form.get(\"listtype\")));\n\t\tArrayList list = new ArrayList();\n\t\tString sql = \"select * from " + classinfo + "\";\n\t\tint curpage=StringUtil.StringToInt(StringUtil.null2String(form.get(\"curpage\")));\n\t\tint pagesize=StringUtil.StringToInt(StringUtil.null2String(form.get(\"pagesize\")));\n\t\tint dbtype=StringUtil.StringToInt(StringUtil.null2String(Globals.SYS_DB_TYPE.get(\"default\")));\n\t\ttry\n\t\t{\n\t\t\tPageList pagelist=new PageList();\n\t\t\tswitch (listtype)\n\t\t\t{\n\t\t\t\tcase 0:   //ͨ���Զ���SQL,����ݾ���Ҫ���޸�sql���(��ҳ)\n\t\t\t\t\t" + "switch (dbtype)\n\t\t\t\t\t{\n\t\t\t\t\t\tcase 1:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";   //orcaleʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 2:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\"; //sqlserverʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 3:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";  //mysqlʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t}" + "\n\t\t\t\t\tpagelist.doList(con,pagesize,curpage,sql,new "+tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1)+"());\n\t\t\t\t\tbreak;\n\t\t\t\tcase 1:   //ͨ���Զ���SQL,����ݾ���Ҫ���޸�sql���(��ҳ) ,���ж���\n\t\t\t\t\t" + "switch (dbtype)\n\t\t\t\t\t{\n\t\t\t\t\t\tcase 1:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";   //orcaleʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 2:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\"; //sqlserverʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t\tcase 3:\n\t\t\t\t\t\t\tsql=\"select * from " + classinfo + "\";  //mysqlʱ�� ���\n\t\t\t\t\t\t\tbreak;\n\t\t\t\t\t}\n\t\t\t\t\tpagelist.doList(con,pagesize,curpage,sql);\n\t\t\t\t\tbreak;\n\t\t\t}\n\t\t\tform.addResult(\"curPage\", pagelist.getCurrentPage());\n\t\t\tform.addResult(\"pageSize\", pagelist.getPageSize());\n\t\t\tform.addResult(\"rowCount\", pagelist.getRowCount());\n\t\t\tform.addResult(\"pageCount\", pagelist.getPages());\n\t\t\tform.addResult(\"list\", pagelist.getResult());\n\t\t}\n\t\tfinally\n\t\t{\n\t\t\tDBConnector.freecon(con); //�ͷ����ݿ�����\n\t\t}\n\t\treturn module.findPage(\"listpage\");\n\t}";
//
//            pandy[2] += "\n\n\t/**\n\t * ����һ������\n\t */\n\tprivate " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " getObject(WebForm form)\n\t{\n\n\t\t";
//            pandy[2] += tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + " obj =new " + tablename.toUpperCase().substring(0, 1) + tablename.toLowerCase().substring(1) + "();\n\t\t";
//            for (i = 0; i < listname.length; i++)
//            {
//                if (listname[i][1].equals("int "))
//                {
//                    pandy[2] += "obj.set" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "(StringUtil.StringToInt(StringUtil.null2String(form.get(\"" + listname[i][0].toLowerCase() + "\"))));\n\t\t";
//                }
//                else if (listname[i][1].equals("String "))
//                {
//                    pandy[2] += "obj.set" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "(StringUtil.null2String(form.get(\"" + listname[i][0].toLowerCase() + "\")));\n\t\t";
//                }
//                else if (listname[i][1].equals("double "))
//                {
//                    pandy[2] += "obj.set" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "(StringUtil.StringToDouble(StringUtil.null2String(form.get(\"" + listname[i][0].toLowerCase() + "\")),0));\n\t\t";
//                }
//                else if (listname[i][1].equals("java.sql.Date "))
//                {
//                    pandy[2] += "try\n\t\t{\n\t\t\t";
//                    pandy[2] += "obj.set" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "(new java.sql.Date(DateUtil.str2date(StringUtil.null2String(form.get(\"" + listname[i][0].toLowerCase() + "\"))).getTime()));\n\t\t";
//                    pandy[2] += "}\n\t\tcatch (Exception e)\n\t\t{\n\t\t\tobj.set" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "(new Date(new java.util.Date().getTime()));\n\t\t}\n\t\t";
//
//                }
//                else
//                {
//                    pandy[2] += "obj.set" + listname[i][0].toUpperCase().substring(0, 1) + listname[i][0].toLowerCase().substring(1) + "(StringUtil.null2String(form.get(\"" + listname[i][0].toLowerCase() + "\")));\n\t\t";
//
//                }
//
//            }
//            pandy[2] += "\n\t\treturn obj;\n\t}";
//
//            pandy[2] += "\n}";

//            /**
//             * ����ҳ��
//             */
//            pandy[3] = FileUtil.getFileCnt("properties//addtemplete.htm");
//            String foreachstr = FileUtil.getflagnote(pandy[3], "foreach");
//            String allforeachstr = "";
//            String oneforeachstr = "";
//            for (i = 0; i < listname.length; i++)
//            {
//                oneforeachstr = foreachstr;
//                oneforeachstr = oneforeachstr.replaceAll("#sysvalue_colname#", listname[i][0]);
//                allforeachstr += oneforeachstr;
//            }
//            pandy[3] = FileUtil.getreplaceflagnote(pandy[3], "foreach", allforeachstr);
//
//            /**
//             * �޸�ҳ��
//             */
//
//            pandy[4] = FileUtil.getFileCnt("properties//updatetemplete.htm");
//            foreachstr = FileUtil.getflagnote(pandy[4], "foreach");
//            allforeachstr = "";
//            oneforeachstr = "";
//            pandy[4] = pandy[4].replaceAll("#sysvalue_firstcolname#", listname[0][0]);
//            for (i = 1; i < listname.length; i++)
//            {
//                oneforeachstr = foreachstr;
//                oneforeachstr = oneforeachstr.replaceAll("#sysvalue_colname#", listname[i][0]);
//                allforeachstr += oneforeachstr;
//            }
//            pandy[4] = FileUtil.getreplaceflagnote(pandy[4], "foreach", allforeachstr);
//
//            /**
//             * ���ҳ��
//             */
//
//            pandy[5] = FileUtil.getFileCnt("properties//viewtemplete.htm");
//            foreachstr = FileUtil.getflagnote(pandy[5], "foreach");
//            allforeachstr = "";
//            oneforeachstr = "";
//            for (i = 0; i < listname.length; i++)
//            {
//                oneforeachstr = foreachstr;
//                oneforeachstr = oneforeachstr.replaceAll("#sysvalue_colname#", listname[i][0]);
//                allforeachstr += oneforeachstr;
//            }
//            pandy[5] = FileUtil.getreplaceflagnote(pandy[5], "foreach", allforeachstr);
//
//            /**
//             * �б�ҳ��
//             */
//
//            pandy[6] = FileUtil.getFileCnt("properties//listtemplete.htm");
//            foreachstr = FileUtil.getflagnote(pandy[6], "titleforeach");
//            allforeachstr = "";
//            oneforeachstr = "";
//            for (i = 1; i < listname.length; i++)
//            {
//                oneforeachstr = foreachstr;
//                oneforeachstr = oneforeachstr.replaceAll("#sysvalue_colname#", listname[i][0]);
//                allforeachstr += oneforeachstr;
//            }
//            pandy[6] = FileUtil.getreplaceflagnote(pandy[6], "titleforeach", allforeachstr);
//
//            foreachstr = FileUtil.getflagnote(pandy[6], "foreach");
//            allforeachstr = "";
//            oneforeachstr = "";
//            for (i = 2; i < listname.length; i++)
//            {
//                oneforeachstr = foreachstr;
//                oneforeachstr = oneforeachstr.replaceAll("#sysvalue_colname#", listname[i][0]);
//                allforeachstr += oneforeachstr;
//            }
//            pandy[6] = FileUtil.getreplaceflagnote(pandy[6], "foreach", allforeachstr);
//            pandy[6] = pandy[6].replaceAll("#sysvalue_firstcolname#", listname[0][0]);
//            pandy[6] = pandy[6].replaceAll("#sysvalue_twocolname#", listname[1][0]);

            result.close();
            stmt.close();
        }
        catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return pandy;
    }
}
