package ops.school.service;

import ops.school.api.dto.SenderTj;
import ops.school.api.dto.ShopTj;
import ops.school.api.entity.FullCut;
import ops.school.api.entity.Shop;
import ops.school.api.entity.ShopOpenTime;

import javax.validation.Valid;
import java.util.List;

public interface ShopService {

	void add(@Valid Shop shop);

	List<Shop> find(Shop shop);

	int update(Shop shop);

	void addFullCut(@Valid FullCut fullcut);

	int deleteFullCut(int id);

	List<FullCut> findFullCut(int shopId);

	int count(Shop shop);

	Shop login(String loginName, String enCode);

	SenderTj statistics(Integer shopId, String beginTime, String endTime);

	Shop findById(int id);

	int openorclose(Integer id);

	int addOpenTime(@Valid ShopOpenTime time);

	int removeopentime(int id);

	List<ShopOpenTime> findOpenTime(int shopId);

    ShopTj shopstatistics(Integer shopId, String beginTime, String endTime);
}
