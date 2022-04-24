/**
 * 
 */
package com.ase0401.msfsdemo.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ase0401.msfsdemo.controller.form.UserForm;
import com.ase0401.msfsdemo.repository.UserRepository;

import msfs_0401.Msfs_0401Factory;
import msfs_0401.Role;
import msfs_0401.Type;
import msfs_0401.User;

/**
 * @author stela
 *
 */
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repo;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.repo.getUserByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}
		// else
		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
				mapRolesToAuthorities(user.getType().getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		// System.out.println(roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public User findByUsername(String username) {
		return this.repo.getUserByUsername(username);
	}

	@Override
	@Transactional
	public void save(UserForm user) {
		Msfs_0401Factory factory = Msfs_0401Factory.eINSTANCE;
		User newUser = factory.createUser();
		Type type = factory.createType();
		type.setName(user.getType());
		
		switch(user.getType()) {
		
		case "Micro Farmer": 
			newUser = factory.createMicroFarmer();
			type.getRoles().add(Role.ROLE_FARMER);
			type.getRoles().add(Role.ROLE_CONSUMER);
			type.getRoles().add(Role.ROLE_SEEDSUPPLIER);
			break;
			
		case "Device Supplier":
			newUser = factory.createDeviceSupplier();
			type.getRoles().add(Role.ROLE_DEVICESUPPLIER);
			break;
			
		case "Seed Supplier":
			newUser = factory.createSeedSupplier();
			type.getRoles().add(Role.ROLE_SEEDSUPPLIER);
			break;
			
		case "Consumer":
			newUser = factory.createConsumer();
			type.getRoles().add(Role.ROLE_CONSUMER);
			break;
			
		default:
			newUser = factory.createConsumer();
			type.getRoles().add(Role.ROLE_CONSUMER);
			break;
		}
		
		newUser.setId(this.repo.getNextId());
		newUser.setUsername(user.getUsername());
		newUser.setPassword(passwordEncoder.encode(user.getPassword()));
		newUser.setType(type);
		newUser.setFirstname(user.getFirstname());
		newUser.setLastname(user.getLastname());
		if(!user.getCompany().isEmpty()) newUser.setCompany(user.getCompany());
		if(!user.getContact().isEmpty()) newUser.setContact(user.getContact());
		
		this.repo.addUser(newUser);
	}

	@Override
	public UserRepository getUserRepository() {
		return this.repo;
	}

}

