package com.example.demo.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Member;

@Repository
public interface MemberRepository extends MongoRepository<Member, String> {
	Member findFirstByName(String name);

	List<Member> findByNameContaining(String name);

	List<Member> findByPositionContaining(String position);

	List<Member> findByCreateUserContaining(String createUser);
}
