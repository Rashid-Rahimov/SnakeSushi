package com.example.snakesushi.Repository;

import com.example.snakesushi.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Admin findAdminByNick(String nick);
}
