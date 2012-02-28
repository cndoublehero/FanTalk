package project.fantalk.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import net.sf.jsr107cache.Cache;
import net.sf.jsr107cache.CacheException;
import net.sf.jsr107cache.CacheManager;

import com.google.appengine.api.memcache.MemcacheService;
import com.google.appengine.api.memcache.MemcacheServiceFactory;
import com.google.appengine.api.memcache.stdimpl.GCacheFactory;
import com.google.appengine.api.xmpp.JID;
import com.google.common.collect.ImmutableMap;

/**
 * 数据和缓存处理类，读取和写入Datastore/Memcache
 * @author mcxiaoke
 */
public class Datastore {
	/** Memcache缓存Key后缀，用于区分同一个用户的不同缓存数据 */
//	private static final String CACHE_KEY_SUFFIX_MEMBER="_member";
	private static final String CACHE_KEY_SUFFIX_STATUS="_status";
	private static final String CACHE_KEY_SUFFIX_MESSAGE="_message";
	@SuppressWarnings("unused")
	private static final String CACHE_KEY_SUFFIX_CONFIG="_config";
    /** 本类的日志实例 */
    private static final Logger logger = Logger.getLogger(Datastore.class
            .getName());
    /** PersistenceManagerFactory实例 */
    private static final PersistenceManagerFactory PMF = JDOHelper
            .getPersistenceManagerFactory("transactions-optional");

    /** Memcache缓存过期时间：12小时 */
    private static final long EXPIRATION_SEC = 60 * 60 * 6L;

    // private static final long EXPIRATION_SEC_SHORT = 60 * 60L;

    /** 本类实例，单例模式 */
    private static Datastore instance;

    /** 缓存管理类实例 */
    private static Cache cache = null;

    // private static Cache shortCache = null;

    /** 数据存储管理器 */
    private ThreadLocal<PersistenceManager> manager = new ThreadLocal<PersistenceManager>();

    /** 获取缓存管理对象实例 */
    static {
        try {
            cache = CacheManager.getInstance().getCacheFactory().createCache(
                    ImmutableMap.of(GCacheFactory.EXPIRATION_DELTA,
                            EXPIRATION_SEC));
            // shortCache =
            // CacheManager.getInstance().getCacheFactory().createCache(
            // ImmutableMap.of(GCacheFactory.EXPIRATION_DELTA,
            // EXPIRATION_SEC));
        } catch (CacheException err) {
            logger.warning("Could not init Datastore cache");
        }
    }

    private Datastore() {}

    /**
     * 获取Datastore对象
     * @return 返回Datastore对象
     */
    public synchronized static Datastore getInstance() {
        if (instance == null) {
            instance = new Datastore();
        }
        return instance;
    }

    /** 下面两个一个在开始时调用，一个在结束时调用，[必须] */

    /**
     * 初始化存储管理
     */
    private void startOperation() {
        manager.set(PMF.getPersistenceManager());
    }

    /**
     * 清理资源，可以在finally里面调用
     */
    private void endOperation() {
        manager.get().close();
        manager.remove();
    }

    /**
     * 写入持久存储
     * @param o 写入对象
     */
    public void put(Object o) {
    	startOperation();
        manager.get().makePersistent(o);
        endOperation();
    }

    /**
     * 写入对象集合
     * @param objects 对象集合
     */
    @SuppressWarnings("unused")
	private void putAll(Collection<Object> objects) {
    	startOperation();
        manager.get().makePersistentAll(objects);
        endOperation();
    }

    /**
     * 从持久存储中删除对象
     * @param o
     */
    public void delete(String email) {
    	startOperation();
    	//参考http://www.jssay.com/blog/index.php/2010/04/25/transient-instances-cant-be-deleted/
    	//只有这样才能真正删除，要不然会出错:Transient instances cant be deleted.
        manager.get().deletePersistent(manager.get().getObjectById(Member.class,email));
        logger.warning("delete user "+email);
        endOperation();
    }

    /**
     * 清除所有缓存
     */
    public void clearCache() {
        MemcacheService memcacheService = MemcacheServiceFactory
                .getMemcacheService();
        memcacheService.clearAll();
    }

    /**
     * 从缓存中清除key对应的值
     * @param key 键
     */
    public void removeCache(String key) {
        if (cache == null)
            return;
        try {
            cache.remove(key);
        } catch (RuntimeException err) {
            logger.log(Level.SEVERE, "Could invalidate cache for " + key, err);
        }
    }

    /**
     * 清除缓存的Member对象
     * @param m Member对象
     */
    public void removeCache(Member m) {
    	removeCache(getKey(m));
    }

    /**
     * 添加缓存
     * @param key 键
     * @param o 缓存对象
     */
    public void addToCache(String key, Object o) {
        if (cache == null) {
            return;
        }
        if (!(o instanceof Serializable)) {
            logger.warning(key
                    + " does not implement Serializable, cannot be cached");
            return;

        }
        try {
            cache.put(key, o);
        } catch (RuntimeException err) {// MemcacheServiceException
            logger.log(Level.SEVERE, "Could add " + key + " to cache", err);
        }
    }

    /**
     * 从缓存中读取值
     * @param key 键
     * @return 返回缓存的对象
     */
    public Object getFromCache(String key) {
        if (cache == null)
            return null;
        try {
            return cache.get(key);
        } catch (RuntimeException err) {
            logger.log(Level.SEVERE, "Could get" + key + " from cache", err);
            return null;
        }
    }

    /**
     * 获取查询Key
     * @param cls 实体类
     * @param id 主键
     * @return 返回对应的Key
     */
    public String getKey(Class<?> cls, String id) {
        return cls.getCanonicalName() + "#" + id;
    }

    /**
     * 获取Member对象的Key
     * @param member Member对象
     * @return 返回Member对象的Key
     */
    public String getKey(Member member) {
        return getKey(Member.class, member.getEmail());
    }

    /**
     * 获取JID对应的Member,如果不存在就创建一个
     * @param jid JID参数
     * @return 返回对应的Member对象
     */
    public Member getMember(JID jid) {
        return getMember(JIDToEmail(jid));
    }

    /**
     * 获取Email对应的Member,如果不存在就创建一个
     * @param email Email参数
     * @return 返回对应的Member对象
     */
	public Member getMember(String email) {
		try {
			logger.warning(email);
			startOperation();
			Member member = manager.get().getObjectById(Member.class, email);
			endOperation();
			return member;
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}
    
    /**
     * 获取Member对象，先从cache里读取，如果不存在缓存，就从Datastore里读取
     * @param email 用户的email地址
     * @return 返回Member对象，如果没有则返回null
     */
	public Member getAndCacheMember(String email) {
		Member m = (Member) getFromCache(email);
		if (m != null) {
			return m;
		}
		m = getMember(email);
		if (m != null) {
			addToCache(email, m);
			return m;
		}
		m = createMember(email);
		put(m);
		addToCache(email, m);
		return m;

	}

    /**
     * 创建Member
     * @param email 用户的email
     * @return 返回创建的Member对象
     */
    public Member createMember(String email) {
        return new Member(email);
    }

    //获取用户主页消息缓存
    @SuppressWarnings("unchecked")
    public Map<Integer, String> getStatusesCache(String email) {
        return (Map<Integer, String>) getFromCache(email + CACHE_KEY_SUFFIX_STATUS);
    }

    public void addStatusesCache(String email, Object o) {
        removeCache(email + CACHE_KEY_SUFFIX_STATUS);
        addToCache(email + CACHE_KEY_SUFFIX_STATUS, o);
    }
    
	 //获取用户已查看私信消息
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getMessageCache(String email) {
		return (Map<Integer, String>) getFromCache(email + CACHE_KEY_SUFFIX_MESSAGE);
	}

	public void addMessageCache(String email, Object o) {
		removeCache(email + CACHE_KEY_SUFFIX_MESSAGE);
		addToCache(email + CACHE_KEY_SUFFIX_MESSAGE, o);
	}
    
    public void removeStatusesCache(String email) {
        removeCache(email + CACHE_KEY_SUFFIX_STATUS);
    }

    /**
     * 获取所有用户的Email地址，主键
     * @return 返回Email地址列表
     */

	public List<String> getAllEmails() {
		startOperation();
		Query query = manager.get().newQuery(
				"SELECT email FROM " + Member.class.getName());
		@SuppressWarnings("unchecked")
		List<String> result = (List<String>) query.execute();
		result.size();
		endOperation();
		return result;
	}	

	 /**
     * 获取指定页大小用户的Email地址列表
     * @param pageNo
     *            页数
     * @param pageSize
     *            页大小     
     * @return 返回Email地址列表
     */

	public List<String> getEmailsByPage(int pageNo, int pageSize) {
		startOperation();
		Query query = manager.get().newQuery(
				"SELECT email FROM " + Member.class.getName()
						+ " WHERE pushType != 'off'");
		query.setRange(pageNo * pageSize, (pageNo + 1) * pageSize);
		@SuppressWarnings("unchecked")
		List<String> result = (List<String>) query.execute();
		result.size();
		endOperation();
		return result;
	}
	
	public int getFanfouBindCount(){
		startOperation();
		Query query = manager.get().newQuery(
				"SELECT COUNT(1) FROM " + Member.class.getName()
						+ " WHERE fanfouUserName != ''");
		Integer count=(Integer) query.execute();
		endOperation();
		return count;
	}
	
	 /**
     * 获取所有用户数
     * @return 所有用户数
     */
	public int getAllEmailCount() {
		startOperation();
		Query query = manager.get().newQuery(
				"SELECT COUNT(1) FROM " + Member.class.getName());
		Integer result = (Integer) query.execute();
		endOperation();
		return result;
	}
	
    /**
     * 获取所有用户的JID列表
     * @return 返回JID列表
     */
    public List<JID> getAllJIDs() {
        List<String> emails = getAllEmails();
        List<JID> jids = new ArrayList<JID>();
        for (String email : emails) {
            jids.add(new JID(email));
        }
        return jids;
    }

    /**
     * 工具函数：JID转换为Email地址
     * @param jid JID参数
     * @return 返回对应的Email地址
     */
    public String JIDToEmail(JID jid) {
        return jid.getId().toLowerCase().split("/")[0];
    }

    // public static CacheStatistics getCacheStatistics() {
    // if (cache == null) {
    // return null;
    // }
    // return cache.getCacheStatistics();
    // }

}
