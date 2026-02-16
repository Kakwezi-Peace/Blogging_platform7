package com.example.Blogging_platform2.service;
import com.example.Blogging_platform2.model.Tag;
import com.example.Blogging_platform2.dao.TagDao;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagService {
    private final TagDao tagDao;

    public TagService(TagDao tagDao) {
        this.tagDao = tagDao;
    }

    @Transactional
    @CachePut(value = "tags", key = "#result.id")
    public Tag saveTag(Tag tag) {
        return tagDao.save(tag);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tags", key = "#id")
    public Optional<Tag> getTagById(Long id) {
        return tagDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tags", key = "#name")
    public Optional<Tag> getTagByName(String name) {
        return tagDao.findByName(name);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "tags")
    public List<Tag> getAllTags() {
        return tagDao.findAll();
    }

    @Transactional
    @CacheEvict(value = "tags", key = "#id")
    public void deleteTag(Long id) {
        tagDao.deleteById(id);
    }
}
