package ru.course.recipemanager.foundation;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.course.recipemanager.entity.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByEmail(String email);
}

