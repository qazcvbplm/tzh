package ops.school.serviceimple;

import ops.school.service.FloorService;
import ops.school.api.dao.FloorMapper;
import ops.school.api.dao.WxUserMapper;
import ops.school.api.entity.Floor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FloorServiceImple implements FloorService {

	@Autowired
	private FloorMapper floorMapper;
	@Autowired
	private WxUserMapper wxUserMapper;

	@Override
	public void add(Floor floor) {
		floor.setSort(System.currentTimeMillis());
		floorMapper.insert(floor);
	}

	@Override
	public List<Floor> find(Floor floor) {
		switch (floor.getQueryType()) {
		case "wxuser":
			if(floor.getSchoolId()==null){
				floor.setSchoolId(Integer.valueOf(floor.getQuery()));
			}
			break;
		case "ops":
			floor.setSchoolId(Integer.valueOf(floor.getQuery()));
			break;
		case "admin":
			break;
		}
		return floorMapper.find(floor);
	}

	@Override
	public int update(Floor floor) {
		return floorMapper.updateByPrimaryKeySelective(floor);
	}
}
