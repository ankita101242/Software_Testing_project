package com.example.ProsePetal.Repositories;

import com.example.ProsePetal.Entity.Post;
import com.example.ProsePetal.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepo extends JpaRepository<Post, Integer> {
    List<Post> findByUser(User user);
    List<Post> findByTitleContainingOrContentContaining(String title, String content);
    // You can add additional query methods as needed
}
