package com.danimeko.blog.controller;

import com.danimeko.blog.exception.ResourceNotFoundException;
import com.danimeko.blog.model.Blog;
import com.danimeko.blog.service.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController

public class BlogController {

    @Autowired
    BlogRepository blogRepository;

    @GetMapping("/blog")
    public List<Blog> index(){
        return blogRepository.findAll();
    }

    @GetMapping("/blog/{id}")
    public ResponseEntity<Blog> getBlogById(@PathVariable String id)
            throws ResourceNotFoundException {
        int blogId = Integer.parseInt(id);
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("There is no blog with this id :: " + id));
        return ResponseEntity.ok().body(blog);
    }

    @PostMapping("/blog/search")
    public List<Blog> search(@RequestBody Map<String, String> body){
        String searchTerm = body.get("text");
        return blogRepository.findByTitleContainingOrContentContaining(searchTerm, searchTerm);
    }

    @PostMapping("/blog")
    public Blog create(@Valid @RequestBody Blog newBlog){
        return blogRepository.save(newBlog);
    }

    @PutMapping("/blog/{id}")
    public ResponseEntity<Blog> update(@PathVariable String id, @RequestBody Blog updateBlog)
            throws ResourceNotFoundException{
        int blogId = Integer.parseInt(id);
        // getting blog
        Blog blog = blogRepository.findById(blogId)
                .orElseThrow(()-> new ResourceNotFoundException("Blog not found with this id ::" + id ));
        blog.setTitle( updateBlog.getTitle());
        blog.setContent( updateBlog.getContent());
        final Blog updatedBlog = blogRepository.save(blog);
        return ResponseEntity.ok(updatedBlog);
    }

    @DeleteMapping("blog/{id}")
    public Map<String , Boolean> delete(@PathVariable String id)
            throws ResourceNotFoundException{

        int blogId = Integer.parseInt(id);
        Blog blog=blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found for this id :: " + id));
        blogRepository.deleteById(blogId);
        Map<String ,Boolean> responce = new HashMap<>();
        responce.put("deleted",Boolean.TRUE);
        return responce;
    }

}
