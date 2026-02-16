package com.example.Blogging_platform2.service;

import com.example.Blogging_platform2.dao.ActivityLogDao;
import com.example.Blogging_platform2.exception.ActivityLogNotFoundException;
import com.example.Blogging_platform2.model.ActivityLog;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ActivityLogService {
    private final ActivityLogDao activityLogDao;

    public ActivityLogService(ActivityLogDao activityLogDao) {
        this.activityLogDao = activityLogDao;
    }

    @Transactional
    @CachePut(value = "activityLogs", key = "#result.id")
    public ActivityLog saveLog(ActivityLog log) {
        return activityLogDao.save(log);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "activityLogs", key = "'all'")
    public List<ActivityLog> getAllLogs() {
        return activityLogDao.findAll();
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "activityLogs", key = "#id")
    public Optional<ActivityLog> getLogById(Long id) {
        return activityLogDao.findById(id);
    }

    @Transactional
    @CacheEvict(value = "activityLogs", key = "#id")
    public void deleteLog(Long id) {
        if (!activityLogDao.existsById(id)) {
            throw new ActivityLogNotFoundException(id);
        }
        activityLogDao.deleteById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "activityLogs", key = "#id")
    public ActivityLog getLog(Long id) {
        return activityLogDao.findById(id)
                .orElseThrow(() -> new ActivityLogNotFoundException(id));
    }

    @Transactional
    @CachePut(value = "activityLogs", key = "#result.id")
    public ActivityLog createLog(ActivityLog log) {
        return activityLogDao.save(log);
    }

    @Transactional
    @CacheEvict(value = "activityLogs", key = "#id")
    public Boolean deleteLogReturnBoolean(Long id) {
        if (!activityLogDao.existsById(id)) {
            throw new ActivityLogNotFoundException(id);
        }
        activityLogDao.deleteById(id);
        return true;
    }
}
