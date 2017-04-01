package com.jws.common.cache;

import com.opensymphony.oscache.base.NeedsRefreshException;
import com.opensymphony.oscache.general.GeneralCacheAdministrator;

public class BaseCache  extends GeneralCacheAdministrator {
	private static final long serialVersionUID = 6239736145696260016L;
	
	private int refreshPeriod;
	
	private String keyPrefix; 
	
	public BaseCache(String keyPrefix,int refreshPeriod){      
        super();      
        this.keyPrefix = keyPrefix;      
        this.refreshPeriod = refreshPeriod;      
    }  
    /**
     * 添加被缓存的对象
     * 
     * @param key
     * @param value
     */
    public void put(String key, Object value) {
        this.putInCache(this.keyPrefix + "_" + key, value);
    }

    /**
     * 删除被缓存的对象
     * 
     * @param key
     */
    public void remove(String key){
        this.removeEntry(this.keyPrefix + "_" + key);  
    }
    
    /**
     * 删除所有被缓存的对象
     */
    public void removeAll(){
        this.flushAll();
    }

    /**
     * 获取被缓存的对象
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public Object get(String key) throws NeedsRefreshException {
    	 try{      
             return this.getFromCache(this.keyPrefix+"_"+key,this.refreshPeriod);      
         } catch (NeedsRefreshException e) {      
             this.cancelUpdate(this.keyPrefix+"_"+key);      
             throw e;      
         } 
    }
    
    public void cancel(String key){
        this.cancelUpdate(key);
    }

}
