package org.qqbot.utils;

import net.diyigemt.miraiboot.annotation.AutoInit;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.qqbot.mapper.BaseMapper;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author diyigemt
 * mybatis工具类
 */
@AutoInit
public class MybatisUtil {
	// 全局mybatis工厂 官方文档推荐只有一个
	private static SqlSessionFactory factory;

	private static final MybatisUtil INSTANCE = new MybatisUtil();

	/**
	 * 根据配置文件 初始化工厂
	 */
	public static void init() {
		Reader reader = null;
		try {
			reader = Resources.getResourceAsReader("mybatis-config.xml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		factory = new SqlSessionFactoryBuilder().build(reader);
	}

	public static MybatisUtil getInstance() {
		return INSTANCE;
	}

	/**
	 * 获取一个sqlsession
	 * 官方推荐执行完事务后需要释放
	 * @return 一个session
	 */
	public SqlSession getSqlSession() {
		if (factory == null) {
			init();
		}
		return factory.openSession();
	}

	public <T extends BaseMapper, K> K getSingleData(Class<T> mapperClass, Class<K> resClass, String methodName, Object... args) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		K res = null;
		Class<?>[] classes = getClasses(args);
		int	len = args.length;
		try {
			if (len == 0) {
				Method method = mapperClass.getMethod(methodName);
				res = (K) method.invoke(mapper);
			} else {
				Method method = mapperClass.getMethod(methodName, classes);
				res = (K) method.invoke(mapper, args);
			}
		} catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassCastException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> List<K> getListData(Class<T> mapperClass, Class<K> resClass, String methodName, Object... args) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		List<K> res = null;
		Class<?>[] classes = getClasses(args);
		int	len = args.length;
		try {
			if (len == 0) {
				Method method = mapperClass.getMethod(methodName);
				res = (List<K>) method.invoke(mapper);
			} else {
				Method method = mapperClass.getMethod(methodName, classes);
				res = (List<K>) method.invoke(mapper, args);
			}
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> int insetData(Class<T> mapperClass, Class<K> resClass, String methodName, Object... args) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		int res = -1;
		Class<?>[] classes = getClasses(args);
		int	len = args.length;
		try {
			Object o = null;
			if (len == 0) {
				Method method = mapperClass.getMethod(methodName);
				o = method.invoke(mapper);
			} else {
				Method method = mapperClass.getMethod(methodName, classes);
				o = method.invoke(mapper, args);
			}
			if (o != null) res = Integer.parseInt(o.toString());
			sqlSession.commit();
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper, K> int updateData(Class<T> mapperClass, Class<K> resClass, String methodName, Object... args) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		int res = -1;
		Class<?>[] classes = getClasses(args);
		int	len = args.length;
		try {
			Object o = null;
			if (len == 0) {
				Method method = mapperClass.getMethod(methodName);
				o = method.invoke(mapper);
			} else {
				Method method = mapperClass.getMethod(methodName, classes);
				o = method.invoke(mapper, args);
			}
			if (o != null) {
				res = Integer.parseInt(o.toString());
			} else {
				res = 0;
			}
			sqlSession.commit();
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
		return res;
	}

	public <T extends BaseMapper> void deleteData(Class<T> mapperClass, String methodName, Object... args) {
		SqlSession sqlSession = MybatisUtil.getInstance().getSqlSession();
		T mapper = sqlSession.getMapper(mapperClass);
		Class<?>[] classes = getClasses(args);
		int	len = args.length;
		try {
			Method method = null;
			if (len == 0) {
				method = mapperClass.getMethod(methodName);
			} else {
				method = mapperClass.getMethod(methodName, classes);
			}
			method.invoke(mapper, args);
			sqlSession.commit();
		} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		} finally {
			sqlSession.close();
		}
	}

	private Class<?>[] getClasses(Object... args) {
		int len = args.length;
		if (len == 0) return null;
		Class<?>[] classes = new Class[len];
		classes = new Class[len];
		for (int i = 0; i < len; i++) {
			Object o = args[i];
			classes[i] = o.getClass();
		}
		return classes;
	}
}