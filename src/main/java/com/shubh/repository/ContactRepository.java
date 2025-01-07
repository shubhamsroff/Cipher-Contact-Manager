package com.shubh.repository;



import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shubh.entities.Contact;
import com.shubh.entities.User;

import jakarta.transaction.Transactional;

public interface ContactRepository extends JpaRepository<Contact, Integer>{

	//		Pagination....
//	currentpage= page
//	contact per page=5
	@Query("from Contact as d where d.user.id=:userId")
	public Page<Contact> findContactsByUser(@Param("userId")int userId,Pageable pePageable);
	
	
	@Modifying
	@Transactional
	@Query("delete from Contact c where c.Id =:id")
	    public void deleteContactById(@Param("id") Integer id);
	
	@Modifying
	@Transactional
	@Query("update Contact set phone=:p where cId=:id")
	public void updateContactById(@Param("id") Integer id,@Param("p") String phone);

	//	Custom finder method
	//	Like ka use krke or Query bhi search krke
	public List<Contact> findByNameContainingAndUser(String keywords,User user);
	

}
