package com.ammu.jwtsecurity.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ammu.jwtsecurity.entity.UserAccount;
@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {

	
	Optional<UserAccount> findByEmail(String email);
	
}
