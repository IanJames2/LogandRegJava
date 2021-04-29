package com.CodingDojo.LognadReg.Repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.CodingDojo.LognadReg.Models.User;


@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	User findByEmail(String email);

}
