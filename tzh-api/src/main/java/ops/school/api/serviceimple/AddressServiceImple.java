package ops.school.api.serviceimple;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import ops.school.api.dao.AddressMapper;
import ops.school.api.entity.Address;
import ops.school.api.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImple extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Autowired
    private AddressMapper addressMapper;


    @Override
    public List<Address> find(Address address) {
        switch (address.getQueryType()) {
            case "wxuser":
                address.setOpenId(address.getQuery());
                break;
            case "ops":
                address.setSchoolId(Integer.valueOf(address.getQuery()));
                break;
        }
        return addressMapper.find(address);
    }



}
