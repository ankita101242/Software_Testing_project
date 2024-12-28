package com.example.ProsePetal.Impl;

import com.example.ProsePetal.Entity.Post;
import com.example.ProsePetal.Entity.User;
import com.example.ProsePetal.Exceptions.ResourceNotFoundException;
import com.example.ProsePetal.Payloads.PostDTO;
import com.example.ProsePetal.Repositories.PostRepo;
import com.example.ProsePetal.Repositories.UserRepo;
import com.example.ProsePetal.Services.PostService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private PostRepo postRepository;

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public PostDTO createPost(PostDTO postDTO, Integer userId) {


        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        Post post = modelMapper.map(postDTO, Post.class);
        post.setCreatedDate(new Date());
        post.setUser(user);
        // Save the post
        Post newPost = this.postRepository.save(post);
        return this.modelMapper.map(newPost, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post", "id", postId));

        // Update post details from DTO
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setImageName(postDTO.getImageName());

        // Save the updated post
        post = postRepository.save(post);
        return this.modelMapper.map(post, PostDTO.class);
    }

    @Override
    public void deletePost(Integer postId) {
        postRepository.deleteById(postId);
    }

    @Override
    public List<PostDTO> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<PostDTO> getPostById(Integer postId) {
        Optional<Post> postOptional = postRepository.findById(postId);
        return postOptional.map(post -> modelMapper.map(post, PostDTO.class));
    }

    @Override
    public List<PostDTO> getPostsByUser(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "userId", userId));
        List<Post> posts = this.postRepository.findByUser(user);
        List<PostDTO> postDTOS = posts.stream().map(post -> this.modelMapper.map(post, PostDTO.class)).collect(Collectors.toList());
        return postDTOS;
    }


    @Override
    public List<PostDTO> searchPosts(String keyword) {
        List<Post> posts = postRepository.findByTitleContainingOrContentContaining(keyword, keyword);
        return posts.stream()
                .map(post -> modelMapper.map(post, PostDTO.class))
                .collect(Collectors.toList());
    }
}