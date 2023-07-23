package com.smartcontact.config;

import com.smartcontact.dao.UserRepository;
import com.smartcontact.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       User user=userRepository.getUserByEmail(username);//username=email
        if(user==null)
            throw new UsernameNotFoundException("Could not found user!!");
        CustomerUserDetail customerUserDetail=new CustomerUserDetail(user);
        return customerUserDetail;
    }

}
