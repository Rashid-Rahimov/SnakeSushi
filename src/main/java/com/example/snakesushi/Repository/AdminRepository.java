package com.example.snakesushi.Repository;

import com.example.snakesushi.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByNick(String nick);
}
