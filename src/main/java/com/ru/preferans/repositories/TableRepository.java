package com.ru.preferans.repositories;

import com.ru.preferans.entities.table.Table;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TableRepository extends JpaRepository<Table, String> {
}
