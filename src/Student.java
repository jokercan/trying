import javax.xml.crypto.Data;
import java.util.Date;


//POJO对象，一个只有 基本数据 ， get和set函数 ， 自身构造函数的对象
public class Student {
    private int id;
    private String name;
    private boolean sex;
    private String phone;
    private Date birthday;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
