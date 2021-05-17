package com.tecforte.blog.service;

import com.tecforte.blog.domain.Blog;
import com.tecforte.blog.repository.BlogRepository;
import com.tecforte.blog.repository.EntryRepository;
import com.tecforte.blog.service.dto.BlogDTO;
import com.tecforte.blog.service.mapper.BlogMapper;
import com.tecforte.blog.service.util.StringMatchingUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Blog}.
 */
@Service
@Transactional
public class BlogService {

    private final Logger log = LoggerFactory.getLogger(BlogService.class);

    private final BlogRepository blogRepository;

    private final BlogMapper blogMapper;
    
    private final EntryRepository entryRepository;

    public BlogService(BlogRepository blogRepository, BlogMapper blogMapper, EntryRepository entryRepository) {
        this.entryRepository = entryRepository;
        this.blogRepository = blogRepository;
        this.blogMapper = blogMapper;
    }

    /**
     * Save a blog.
     *
     * @param blogDTO the entity to save.
     * @return the persisted entity.
     */
    public BlogDTO save(BlogDTO blogDTO) {
        log.debug("Request to save Blog : {}", blogDTO);
        Blog blog = blogMapper.toEntity(blogDTO);
        blog = blogRepository.save(blog);
        return blogMapper.toDto(blog);
    }

    /**
     * Get all the blogs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<BlogDTO> findAll() {
        log.debug("Request to get all Blogs");
        return blogRepository.findAllWithEagerRelationships().stream()
            .map(blogMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one blog by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BlogDTO> findOne(Long id) {
        log.debug("Request to get Blog : {}", id);
        return blogRepository.findById(id)
            .map(blogMapper::toDto);
    }

    /**
     * Delete the blog by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Blog : {}", id);
        blogRepository.deleteById(id);
    }

    /**
     * Clean all entries of all blogs that contain the keywords.
     *
     * @param keywords the keywords of entry that will be find and clean.
     */
    public void cleanAllBlogs(List<String> keywords) {
        log.debug("Request to clean all Blog with keywords {}", keywords);
        blogRepository.findAll().forEach((blog) -> {
        	cleanBlog(blog.getId(), keywords);
        });
    }
    

    /**
     * Clean all entries of all blogs that contain the keywords.
     *
     * @param keywords the keywords of entry that will be find and clean.
     */
    public void cleanBlog(Long id, List<String> keywords) {
        log.debug("Request to clean Blog with id : {} with keywords {}", id, keywords);
        entryRepository.findAllByBlogId(id).forEach((entry) -> {
        	if(StringMatchingUtil.containKeyword(keywords, entry.getContent()) || StringMatchingUtil.containKeyword(keywords, entry.getTitle())) {
        		log.debug("Request to delete Entry : {}", entry.getId());
                entryRepository.deleteById(entry.getId());
        	}
        });;
    }
}
