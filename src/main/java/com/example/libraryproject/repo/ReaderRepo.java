package com.example.libraryproject.repo;

import com.example.libraryproject.entity.Reader;
import com.example.libraryproject.entity.UserAuth;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ReaderRepo extends CrudRepository<Reader, Long> {
    @Query(value = "select * from readers where email = ?1", nativeQuery = true)
    Optional<Reader> findByEmail(String email);
    @Query(value = "delete from readers where email = ?1", nativeQuery = true)
    void deleteReaderByEmail(String email);

    @Override
    Optional<Reader> findById(Long aLong);

    @Transactional
    @Modifying
    @Query("UPDATE Reader r SET r.email = :email, r.password = :password, r.name = :name, r.surname = :surname, r.phone = :phone, r.placeToLive = :placeToLive WHERE r.id = :id")
    void update(String email, String password, String name, String surname, String phone, String placeToLive, Long id);
}
