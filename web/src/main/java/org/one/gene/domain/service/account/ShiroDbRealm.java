/*******************************************************************************
 * Copyright (c) 2005, 2014 springside.github.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *******************************************************************************/
package org.one.gene.domain.service.account;

import com.google.common.base.Objects;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.one.gene.domain.entity.Menu;
import org.one.gene.domain.entity.User;
import org.one.gene.utils.Encodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

public class ShiroDbRealm extends AuthorizingRealm {

  private static final Logger logger = LoggerFactory.getLogger(ShiroDbRealm.class);

  protected AccountService accountService;

  /**
   * 认证回调函数,登录时调用.
   */
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
      throws AuthenticationException {
    UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
    User user = accountService.findUserByLoginName(token.getUsername());
    
    if (user != null) {
        if(!user.isValidate()){
            throw new LockedAccountException(); 
        }
  	 
      this.setMenuID(user);
  		
      byte[] salt = Encodes.decodeHex(user.getSalt());
      return new SimpleAuthenticationInfo(
          new ShiroUser(user),
          user.getPassword(), ByteSource.Util.bytes(salt), getName());
    } else {
      return null;
    }
  }

  /**
   * 授权查询回调函数, 进行鉴权但缓存中无用户的授权信息时调用.
   */
  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
    ShiroUser shiroUser = (ShiroUser) principals.getPrimaryPrincipal();
    User user = accountService.findUserByLoginName(shiroUser.loginName);
    SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
    //TODO@(chengQ) need get roles
    //info.addRoles(user.getRoleList());
    info.addRole("admin");
    return info;
  }
  
  //放入菜单id
  public void setMenuID(User user) {
	  List<Menu> menus = accountService.getAllMenu();
	  String menuid = user.getMenuId();
	  if(menuid !=null){
		  String[] menuids = menuid.split(",");
		  for (Menu menu : menus) {
			  for (int i = 0; i < menuids.length; i++) {
				  if (menuids[i].equals(menu.getId() + "")) {
					  user.getMenuMap().put(menu.getFirstId(), menu);
					  user.getMenuMap().put(menu.getSecondId(), menu);
				  }
			  }
		  }
	  }
  }

  public void setAccountService(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * 自定义Authentication对象，使得Subject除了携带用户的登录名外还可以携带更多信息.
   */
  public static class ShiroUser implements Serializable {

    private static final long serialVersionUID = -1373760761780840081L;

    private final User user;
    public Long id;
    public String loginName;
    public String name;

    public ShiroUser(User user) {
      this.id = user.getId();
      this.loginName = user.getCode();
      this.name = user.getName();
      this.user = user;
    }

    public String getName() {
      return name;
    }

    public final User getUser(){
      return this.user;
    }

    /**
     * 本函数输出将作为默认的<shiro:principal/>输出.
     */
    @Override
    public String toString() {
      return loginName;
    }

    /**
     * 重载hashCode,只计算loginName;
     */
    @Override
    public int hashCode() {
      return Objects.hashCode(loginName);
    }

    /**
     * 重载equals,只计算loginName;
     */
    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (getClass() != obj.getClass()) {
        return false;
      }
      ShiroUser other = (ShiroUser) obj;
      if (loginName == null) {
        if (other.loginName != null) {
          return false;
        }
      } else if (!loginName.equals(other.loginName)) {
        return false;
      }
      return true;
    }
  }
}
