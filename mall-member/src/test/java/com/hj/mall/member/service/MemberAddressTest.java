package com.hj.mall.member.service;

import com.hj.mall.common.exception.BizException;
import com.hj.mall.member.entity.MemberReceiveAddress;
import com.hj.mall.member.service.CustomerServiceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemberAddressTest {

    @Autowired
    private MemberReceiveAddressService addressService;

    @MockitoBean
    private CustomerServiceService customerServiceService;

    private static final Long MEMBER_ID = 9001L;

    private MemberReceiveAddress buildAddress(String name, String phone, String detail, Integer isDefault) {
        MemberReceiveAddress addr = new MemberReceiveAddress();
        addr.setMemberId(MEMBER_ID);
        addr.setName(name);
        addr.setPhone(phone);
        addr.setProvince("广东省");
        addr.setCity("深圳市");
        addr.setDistrict("南山区");
        addr.setDetailAddress(detail);
        addr.setDefaultStatus(isDefault);
        return addr;
    }

    @Test
    void saveAndGetAddress() {
        MemberReceiveAddress addr = buildAddress("张三", "13800138000", "科技园路1号", 0);
        addressService.save(addr);

        assertNotNull(addr.getId());

        MemberReceiveAddress found = addressService.getById(addr.getId());
        assertEquals("张三", found.getName());
        assertEquals(MEMBER_ID, found.getMemberId());
    }

    @Test
    void listByMemberId() {
        addressService.save(buildAddress("李四", "13800138001", "地址A", 0));
        addressService.save(buildAddress("李四", "13800138002", "地址B", 0));

        List<MemberReceiveAddress> list = addressService.listByMemberId(MEMBER_ID);
        assertTrue(list.size() >= 2);
    }

    @Test
    void updateAddress() {
        MemberReceiveAddress addr = buildAddress("王五", "13800138003", "旧地址", 0);
        addressService.save(addr);

        addr.setName("王五六");
        addr.setDetailAddress("新地址");
        addressService.updateById(addr);

        MemberReceiveAddress updated = addressService.getById(addr.getId());
        assertEquals("王五六", updated.getName());
        assertEquals("新地址", updated.getDetailAddress());
    }

    @Test
    void setDefaultAddress() {
        MemberReceiveAddress addr1 = buildAddress("赵六", "13800138004", "地址1", 1);
        addressService.save(addr1);
        MemberReceiveAddress addr2 = buildAddress("赵六", "13800138005", "地址2", 0);
        addressService.save(addr2);

        addressService.setDefault(addr2.getId(), MEMBER_ID);

        MemberReceiveAddress found1 = addressService.getById(addr1.getId());
        assertEquals(0, found1.getDefaultStatus());
        MemberReceiveAddress found2 = addressService.getById(addr2.getId());
        assertEquals(1, found2.getDefaultStatus());
    }

    @Test
    void deleteAddress() {
        MemberReceiveAddress addr = buildAddress("钱七", "13800138006", "待删除", 0);
        addressService.save(addr);

        addressService.removeById(addr.getId());

        assertThrows(BizException.class, () -> addressService.getById(addr.getId()));
    }
}