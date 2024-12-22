package com.libraryapi.dao.repositories;


import com.libraryapi.model.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, UUID> {

}
