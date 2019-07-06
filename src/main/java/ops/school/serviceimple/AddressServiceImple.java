package ops.school.serviceimple;

import ops.school.service.AddressService;
import ops.school.api.dao.AddressMapper;
import ops.school.api.entity.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImple implements AddressService {

	@Autowired
	private AddressMapper addressMapper;

	@Override
	public void add(Address address) {
		addressMapper.insert(address);
	}

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

	@Override
	public int update(Address address) {
		return addressMapper.updateByPrimaryKeySelective(address);
	}
	
	
}
