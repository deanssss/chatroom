import com.benputao.model.User;
import com.benputao.utils.sqlbulider.SQLBulider;

public class Test {
	public static void main(String[] args) {
		User model=new User("123",null,null);
		String sql1=SQLBulider.buildInsert(model);
		String sql2="CREATE TABLE `"+model.getUsername()
				   +"friend`(`username`  varchar(10) NOT NULL ,"
				   +"`marks`  varchar(20) NULL ,"
				   +"`betime`  datetime NULL ON UPDATE CURRENT_TIMESTAMP ,"
				   +"`kind`  varchar(10) NULL DEFAULT 'user' ,"
				   +"FOREIGN KEY (`username`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE)";
		String sql3="CREATE TABLE `"+model.getUsername()
				   +"message`(`senduser` varchar(10) NOT NULL,"
				   +"`recvuser` varchar(10) NOT NULL,"
				   +"`sendtime` datetime NULL ON UPDATE CURRENT_TIMESTAMP ,"
				   +"`message` varchar(255) NULL,"
				   +"`isread` bit(1) NULL DEFAULT b'0',"
				   +"FOREIGN KEY (`senduser`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,"
				   +"FOREIGN KEY (`recvuser`) REFERENCES `users` (`username`) ON DELETE CASCADE ON UPDATE CASCADE)";
		System.out.println(sql1);
		System.out.println(sql2);
		System.out.println(sql3);
	}
	
}
