import java.sql.*;
import java.util.List;


public class Jdbc {
    //查询数据
    public static void testConnect() throws Exception{
        // 注册MySQL驱动 (可以省略这一步)

        Class.forName("com.mysql.jdbc.Driver");

// 连接MySQL服务器

        String username= "root";

        String password = "root";

        String connectionUrl = "jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=UTF-8";

        Connection conn = DriverManager.getConnection(connectionUrl, username, password);

        System.out.println("连接成功!");

        ///////////////////////////////////////
        //数据库查询，Statement语句 ResultSet结果集
        Statement stmt = conn.createStatement();


        //显示结果集的数据
        //可以用String 来取结果集里面的任何数据

        /*ResultSet rs = stmt.executeQuery("SELECT * FROM student");
        while(rs.next()){
            int id = rs.getInt("id");
            int sex = rs.getInt("sex");
            String name = rs.getString("name");
            String phone = rs.getString("phone");
            Date birthday = rs.getDate("birthday");

            System.out.println(id + "\t" + name + "\t" + phone + "\t" + birthday + "\t" + sex );
        }
        */

        String sql = "SELECT id AS 学号 , name AS 姓名 , birthday FROM student ";
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()){
            String id = rs.getString("学号");  //此时选的列为学号，因为之前的sql语句将id 编程了 学号
            String name = rs.getString("姓名");
            String birthday = rs.getString("birthday");

           /*       1,2,3表示第一列，第二列，第三列。与上面的相对应
            String id = rs.getString(1);
            String name = rs.getString(2);
            String birthday = rs.getString(3);
            */

           ////////////////////////////////////////////////////////////////////
            //显示元数据信息
            ResultSetMetaData rsmd = rs.getMetaData();
            int numColums = rsmd.getColumnCount(); //有多少列
            for(int i = 1 ; i <= numColums ; i++){
                String name1 = rsmd.getColumnName(i); //原始列名
                String label = rsmd.getColumnLabel(i); //别名
                int type = rsmd.getColumnType(i);       // 类型
                String typeName = rsmd.getColumnTypeName(i); //类型名称

                System.out.println("第" + i + "列: " + name + ", " + label + ", " + typeName + "(" + type + ")");
            }
            /////////////////////////////////////////////////////////////////////

            System.out.println(id + "\t" + name + "\t" + birthday);
        }


        ///////////////////////////////////////////////////////////////////////////

        conn.close();

        System.out.println("关闭连接!");
    }

    //插入数据
    public static void testInsert() throws Exception{
        String username= "root";

        String password = "root";

        String connectionUrl = "jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=UTF-8";



        Connection conn = DriverManager.getConnection(connectionUrl, username, password);

        System.out.println("连接成功!");

        //////////////////////////////////////////////////////////
        String sql = "INSERT INTO student(`id` , `name` , `birthday`)" + "VALUES('20181200' , '韩' , '1997-4-19')";
        System.out.println("SQL" + sql);

        Statement stmt = conn.createStatement();
        stmt.execute(sql);
        int count = stmt.getUpdateCount();
        System.out.println("受影响的行为数: " + count);
        ////////////////////////////////////////////////////////////////////////////////

        conn.close();
        System.out.println("关闭连接");
    }

    //插入数据（有自增键的情况）
    // /*当主键设置为自增的时候：
    //    1.插入数据时，不写该字段
    //    2.执行时指定 RETURN.GENERATED_KEYS
    //    3.去除返回的自增主键
    //    */
    public static void testInsert2() throws Exception{
        String username= "root";

        String password = "root";

        String connectionUrl = "jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=UTF-8";



        Connection conn = DriverManager.getConnection(connectionUrl, username, password);

        System.out.println("连接成功!");

        ///////////////////////////////////////////////////////////////////////////
        //插入有自增的表   插入的时候不用管自增那一列的情况，写别的行就可以
        String sql = "INSERT INTO leave_event(`stuId` , `daysFrom` , `daysTo` , `type` , `reason`)" +
                "VALUES('20180004' ,  '2018-03-24' , '2018-03-30' ,'2' , '出国')";
        System.out.println("SQL: " + sql);

        Statement stmt = conn.createStatement();
        stmt.execute(sql , Statement.RETURN_GENERATED_KEYS);   // ,后面的语句代表返回那个自增得到的对应值

        //取得自动生成的主键的值
        ResultSet rs = stmt.getGeneratedKeys();
        while(rs.next()){
            int id = rs.getInt(1);
            System.out.println("产生的主键是： " + id);
        }
        /////////////////////////////////////////////////////////////////
        conn.close();
        System.out.println("关闭链接");
    }


    //预处理查询
    //相比于之前有一定好处，在插入多个数据时提高速度，
    //因为之前的要解析语句再插入
    //使用预处理查询，就只有一步解析语句
    public static void testQuery() throws Exception{
        String username= "root";
        String password = "root";
        String connectionUrl = "jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=UTF-8";

        Connection conn = DriverManager.getConnection(connectionUrl, username, password);
        System.out.println("连接成功!");

        ////////////////////////////////////////////
        //1.构造一个SQL，参数值用?代替，称为占位符
        String sql = "INSERT INTO student (id , name , birthday) VALUES (? , ? , ?)";

        //2.创建PreparedStatement 对象（与MySQL产生一次交互）
        PreparedStatement ptmt = conn.prepareStatement(sql);

        //3.设置参数值
        ptmt.setInt(1,20183001);
        ptmt.setString(2,"小新");
        ptmt.setString(3,"1993-3-10");

        //4.执行查询
        ptmt.execute();

        ////////////////
        conn.close();
        System.out.println("关闭链接");
    }

    //POJO
    public static void testPojo() throws Exception{
        String username= "root";

        String password = "root";

        String connectionUrl = "jdbc:mysql://127.0.0.1:3306/af_school?useUnicode=true&characterEncoding=UTF-8";

        Connection conn = DriverManager.getConnection(connectionUrl, username, password);

        System.out.println("连接成功!");

        Statement stmt = conn.createStatement();

        ///////////////////////////////////////////////
        //SQL查询并自动映射为POJO
        String s1 = "SELECT id , name , sex , phone , birthday FROM student";
        ResultSet resultSet = stmt.executeQuery(s1);
       // List<Student> students = resultSet
    }

    public static void main(String[] args) throws Exception {
        //testConnect();
        //testInsert();
       //testInsert2();
        //testQuery();
    }
}
