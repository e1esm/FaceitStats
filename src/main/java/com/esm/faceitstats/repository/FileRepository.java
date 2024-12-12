package com.esm.faceitstats.repository;

import com.esm.faceitstats.entity.File;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM File f WHERE f.fileName = :path")
    void deleteByPath(String path);
}
