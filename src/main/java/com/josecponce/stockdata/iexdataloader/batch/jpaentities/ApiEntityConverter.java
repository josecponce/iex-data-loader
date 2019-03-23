package com.josecponce.stockdata.iexdataloader.batch.jpaentities;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

@Service
@Slf4j
public class ApiEntityConverter {
    public <Entity> Entity convert(Object apiObject, Class<Entity> clazz) {
        Entity entity = null;
        try {
            entity = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            log.error(String.format("Failed to convert api entity from type '%s' to '%s'",
                    apiObject.getClass().getSimpleName(), clazz.getSimpleName()), e);
            ReflectionUtils.rethrowRuntimeException(e);
        }
        BeanUtils.copyProperties(apiObject, entity);
        return entity;
    }
}
