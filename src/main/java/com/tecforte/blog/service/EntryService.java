package com.tecforte.blog.service;

import com.tecforte.blog.domain.Blog;
import com.tecforte.blog.domain.Entry;
import com.tecforte.blog.domain.enumeration.Emoji;
import com.tecforte.blog.repository.EntryRepository;
import com.tecforte.blog.repository.BlogRepository;
import com.tecforte.blog.service.dto.EntryDTO;
import com.tecforte.blog.service.mapper.EntryMapper;
import com.tecforte.blog.service.util.ValidationUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Entry}.
 */
@Service
@Transactional
public class EntryService {

    private final Logger log = LoggerFactory.getLogger(EntryService.class);

    private final EntryRepository entryRepository;

    private final EntryMapper entryMapper;
    
    private final BlogRepository blogRepository;

    public EntryService(EntryRepository entryRepository, EntryMapper entryMapper, BlogRepository blogRepository) {
        this.entryRepository = entryRepository;
        this.entryMapper = entryMapper;
        this.blogRepository = blogRepository;
    }

    /**
     * Save a entry.
     *
     * @param entryDTO the entity to save.
     * @return the persisted entity.
     */
    public EntryDTO save(EntryDTO entryDTO) {
        log.debug("Request to save Entry : {}", entryDTO);
        Entry entry = entryMapper.toEntity(entryDTO);
        entry = entryRepository.save(entry);
        return entryMapper.toDto(entry);
    }

    /**
     * Get all the entries.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EntryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Entries");
        return entryRepository.findAll(pageable)
            .map(entryMapper::toDto);
    }


    /**
     * Get one entry by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EntryDTO> findOne(Long id) {
        log.debug("Request to get Entry : {}", id);
        return entryRepository.findById(id)
            .map(entryMapper::toDto);
    }

    /**
     * Delete the entry by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Entry : {}", id);
        entryRepository.deleteById(id);
    }
    
    /**
     * 
     * @param entryDTO
     * @return number indicate the entry violate the rule or not
     * if number 0 is returned the entry does not violate any rule
     * if number 1 is returned cant find the blog with the id
     * if number 2 is returned the entry violates emoji rule
     * if number 3 is returned the entry violates content rule
     * if number 4 is returned the entry violates title rule
     */
    public int validateEntry(EntryDTO entryDTO) {
        if(null != entryDTO.getBlogId()) {
        	Optional<Blog> blog = blogRepository.findById(entryDTO.getBlogId());
        	if(!blog.isPresent()) {
        		return 1;
        	}
            if(ValidationUtil.validateEmoji(blog.get().isPositive(), entryDTO.getEmoji()) == false) {
            	return 2;
            }
            if(ValidationUtil.validateContentOrTitle(blog.get().isPositive(), entryDTO.getContent()) == false) {
            	return 3;
            }
            if(ValidationUtil.validateContentOrTitle(blog.get().isPositive(), entryDTO.getTitle()) == false) {
            	return 4;
            }
        }
        return 0;
    }
}
