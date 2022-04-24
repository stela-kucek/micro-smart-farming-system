/**
 * 
 */
package com.ase0401.msfsdemo.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.ase0401.msfsdemo.controller.form.UserForm;
import com.ase0401.msfsdemo.repository.UserRepository;

import msfs_0401.User;

/**
 * @author stela
 *
 */
public interface UserService extends UserDetailsService {
    User findByUsername(String userName);

    void save(UserForm user);
    
    UserRepository getUserRepository();
}
