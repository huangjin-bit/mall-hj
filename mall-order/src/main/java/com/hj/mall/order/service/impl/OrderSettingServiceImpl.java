package com.hj.mall.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.hj.mall.order.entity.OrderSetting;
import com.hj.mall.order.mapper.OrderSettingMapper;
import com.hj.mall.order.service.OrderSettingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderSettingServiceImpl implements OrderSettingService {

    private final OrderSettingMapper orderSettingMapper;

    @Override
    public OrderSetting getSetting() {
        LambdaQueryWrapper<OrderSetting> wrapper = new LambdaQueryWrapper<>();
        wrapper.last("LIMIT 1");
        List<OrderSetting> list = orderSettingMapper.selectList(wrapper);
        if (list.isEmpty()) {
            // 返回默认配置
            OrderSetting setting = new OrderSetting();
            setting.setFlashOrderOvertime(60);
            setting.setNormalOrderOvertime(60);
            setting.setConfirmOvertime(7);
            setting.setFinishOvertime(15);
            setting.setCommentOvertime(30);
            return setting;
        }
        return list.get(0);
    }

    @Override
    public void update(OrderSetting setting) {
        setting.setUpdateTime(LocalDateTime.now());
        if (setting.getId() == null) {
            // 如果没有ID，先查询是否有记录
            OrderSetting exist = getSetting();
            if (exist.getId() != null) {
                setting.setId(exist.getId());
            }
        }
        if (setting.getId() != null) {
            orderSettingMapper.updateById(setting);
        } else {
            setting.setCreateTime(LocalDateTime.now());
            orderSettingMapper.insert(setting);
        }
        log.info("[OrderSettingService] 订单配置更新成功");
    }
}
