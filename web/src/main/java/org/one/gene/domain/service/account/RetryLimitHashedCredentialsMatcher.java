package org.one.gene.domain.service.account;

import com.google.common.cache.CacheBuilder;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;

import java.util.concurrent.atomic.AtomicInteger;


public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher {

  private CacheManager cacheManager;

  private static final String RETRY_CACHE_NAME = "passwordRetryCache";

  //private Cache<String, AtomicInteger> passwordRetryCache;


//  RetryLimitHashedCredentialsMatcher(Cache passwordRetryCache,String hashAlgorithmName){
//    super(hashAlgorithmName);
//    this.passwordRetryCache = passwordRetryCache;
//  }

  @Override
  public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
    Cache<String, AtomicInteger> passwordRetryCache = cacheManager.getCache(RETRY_CACHE_NAME);
    String username = (String)token.getPrincipal();
    AtomicInteger retryCount = passwordRetryCache.get(username);
    if(retryCount == null){
      passwordRetryCache.put(username, new AtomicInteger(1));
    }
    if(retryCount.incrementAndGet() > 1) {
      throw new ExcessiveAttemptsException();
    }

    boolean matches = super.doCredentialsMatch(token, info);
    if(matches) {
      //clear retry count
      passwordRetryCache.remove(username);
    }
    return matches;
  }

  public void setCacheManager(CacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }
}
