package ops.school.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import ops.school.api.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {


    List<Address> find(Address address);

}
