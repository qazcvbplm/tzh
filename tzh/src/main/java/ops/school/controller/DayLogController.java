package ops.school.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.StatisticsConstants;
import ops.school.api.entity.DayLogTakeout;
import ops.school.api.entity.Shop;
import ops.school.api.service.DayLogTakeoutService;
import ops.school.api.service.ShopService;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import ops.school.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "店铺日营业额统计")
@RequestMapping("ops/daylogtakeout")
public class DayLogController {

    @Autowired
    private DayLogTakeoutService dayLogTakeoutService;

    @Autowired
    private ShopService shopService;

    @ApiOperation(value = "查询", httpMethod = "POST")
    @PostMapping("find")
    public ResponseObject find(HttpServletRequest request, HttpServletResponse response,
                               String selfId, String day, String type, Integer parentId, int page, int size) {
        QueryWrapper<DayLogTakeout> query = new QueryWrapper<>();
        if (parentId != null)
            query.lambda().eq(DayLogTakeout::getParentId, parentId);
        if (selfId != null)
            query.lambda().eq(DayLogTakeout::getSelfId, selfId);
        if (type != null)
            query.lambda().eq(DayLogTakeout::getType, type);
        if (day != null)
            query.lambda().eq(DayLogTakeout::getDay, day);
        query.lambda().orderByDesc(DayLogTakeout::getDay);
        IPage<DayLogTakeout> list = dayLogTakeoutService.page(PageUtil.getPage(page, size), query);
        if (!"商铺日志".equals(type)){
            return new ResponseObject(true, "ok")
                    .push("total", list.getTotal())
                    .push("list", list.getRecords());
        }
        List<Integer> shopIds = PublicUtilS.getValueList(list.getRecords(),"selfId");
        if (shopIds.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true, "ok")
                    .push("total", list.getTotal())
                    .push("list", list.getRecords())
                    .push("all",new ArrayList<>());
        }
        Collection<Shop> shopList = shopService.listByIds(shopIds);
        if (shopList.size() < NumConstants.INT_NUM_1){
            return new ResponseObject(true, "ok")
                    .push("total", list.getTotal())
                    .push("list", list.getRecords())
                    .push("all",new ArrayList<>());
        }
        Map<Integer,Shop> shopMap = PublicUtilS.listForMapValueE(shopList,"id");
        for (DayLogTakeout dayLog : list.getRecords()) {
            if (shopMap.get(dayLog.getSelfId()) != null ){
                dayLog.setIsDelete(shopMap.get(dayLog.getSelfId()).getIsDelete());
            }
        }
        //加入当日数据统计-提现和负责人截至可提现
        QueryWrapper<DayLogTakeout> txSchoolWrapper = new QueryWrapper<DayLogTakeout>();
        txSchoolWrapper.lambda().eq(DayLogTakeout::getType, StatisticsConstants.DAY_SCHOOL_CAN_TX_MONEY);
        txSchoolWrapper.lambda().eq(DayLogTakeout::getSelfId,parentId);
        txSchoolWrapper.lambda().like(DayLogTakeout::getDay,day);
        List<DayLogTakeout> allTxSchoolList = dayLogTakeoutService.list(txSchoolWrapper);
        return new ResponseObject(true, "ok")
                .push("total", list.getTotal())
                .push("list", list.getRecords())
                .push("all",allTxSchoolList)
                ;
    }
}
