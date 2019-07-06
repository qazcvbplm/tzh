package ops.school.service;


import ops.school.api.entity.Address;

import java.util.List;

public interface AddressService {

	void add(Address address);

	List<Address> find(Address address);

	int update(Address address);

}
