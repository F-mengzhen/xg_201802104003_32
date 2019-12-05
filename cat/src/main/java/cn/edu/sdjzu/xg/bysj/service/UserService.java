package cn.edu.sdjzu.xg.bysj.service;
import cn.edu.sdjzu.xg.bysj.dao.UserDao;
import cn.edu.sdjzu.xg.bysj.domain.User;

import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();

	public UserService() {
	}

	public static UserService getInstance(){
		return UserService.userService;
	}

	public Collection<User> getUsers()throws SQLException {
		return userDao.findAll();
	}

	public User getUser(Integer id)throws SQLException{
		return userDao.find(id);
	}

	public boolean changePassword(User user)throws SQLException{
	  return UserDao.getInstance().changPassword(user);
	}

	public boolean addUser(User user)throws SQLException{
		return userDao.add(user);
	}

	public boolean deleteUser(Integer id)throws SQLException{
		User user = this.getUser(id);
		return this.deleteUser(user);
	}

	public boolean deleteUser(User user)throws SQLException{
		return userDao.delete(user);
	}


	public User login(String username, String password)throws SQLException {
    return UserDao.getInstance().login(username,password);
	}
}
