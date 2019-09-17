package ops.school.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.constants.NumConstants;
import ops.school.api.constants.StatisticsConstants;
import ops.school.api.dto.DayLogTakeoutDTO;
import ops.school.api.dto.TimeEveryDTO;
import ops.school.api.entity.DayLogTakeout;
import ops.school.api.entity.Shop;
import ops.school.api.exception.DisplayException;
import ops.school.api.service.DayLogTakeoutService;
import ops.school.api.service.ShopService;
import ops.school.api.util.PublicUtilS;
import ops.school.api.util.ResponseObject;
import ops.school.scheduled.Task;
import ops.school.util.PageUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@Api(tags = "店铺日营业额统计")
@RequestMapping("ops/daylogtakeout")
public class DayLogController {

    @Resource(name = "scheduledTask")
    private Task task;

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
        //加入当日数据统计-提现和负责人截至可提现
        if ("学校商铺日志".equals(type)){
            //---------查询学校跑腿日志
            QueryWrapper<DayLogTakeout> runWrapper = new QueryWrapper<DayLogTakeout>();
            runWrapper.lambda().eq(DayLogTakeout::getType, StatisticsConstants.DAY_RUN_ORDER_TYPE);
            runWrapper.lambda().eq(DayLogTakeout::getSelfId,selfId);
            List<DayLogTakeout> runCountList = dayLogTakeoutService.list(runWrapper);
            Map<String,DayLogTakeout> runCountMap = PublicUtilS.listForMapValueE(runCountList,"day");
            //---------查询学校店铺日志，总的外卖统计
            QueryWrapper<DayLogTakeout> allSchoolWrapper = new QueryWrapper<DayLogTakeout>();
            allSchoolWrapper.lambda().eq(DayLogTakeout::getSelfId,selfId);
            allSchoolWrapper.lambda().eq(DayLogTakeout::getType, StatisticsConstants.DAY_ALL_SCHOOL_TYPE);
            List<DayLogTakeout> allSchoolList = dayLogTakeoutService.list(allSchoolWrapper);
            Map<String,DayLogTakeout> allSchoolMap = PublicUtilS.listForMapValueE(allSchoolList,"day");
            //---------查询当日可提现的
            QueryWrapper<DayLogTakeout> txSchoolWrapper = new QueryWrapper<DayLogTakeout>();
            txSchoolWrapper.lambda().eq(DayLogTakeout::getType, StatisticsConstants.DAY_SCHOOL_CAN_TX_MONEY);
            txSchoolWrapper.lambda().eq(DayLogTakeout::getSelfId,selfId);
            List<DayLogTakeout> allTxSchoolList = dayLogTakeoutService.list(txSchoolWrapper);
            Map<String,DayLogTakeout> takeoutMap = PublicUtilS.listForMapValueE(allTxSchoolList,"day");
            for (DayLogTakeout record : list.getRecords()) {
                DayLogTakeoutDTO dto = new DayLogTakeoutDTO();
                if (takeoutMap.get(record.getDay()) != null){
                    BeanUtils.copyProperties(takeoutMap.get(record.getDay()),dto);
                }else {
                    Integer i = null;
                }
                //当日跑腿负责人所得
                BigDecimal runSchoolTotal = new BigDecimal(0);
                //跑腿总单数
                Integer runAllCounts = 0;
                //跑腿总营业额
                BigDecimal runMoneyAmount = new BigDecimal(0);
                if (runCountMap.get(record.getDay()) != null){
                    runSchoolTotal = runCountMap.get(record.getDay()).getSelfGet();
                    //跑腿总单数
                    runAllCounts = runCountMap.get(record.getDay()).getTotalCount();
                    //跑腿总营业额
                    runMoneyAmount = runCountMap.get(record.getDay()).getTotalPrice();
                }
                //外卖总单数
                Integer orderAllCounts = 0;
                //外卖总营业额
                BigDecimal orderAllMoneyAmount = new BigDecimal(0);
                if (allSchoolMap.get(record.getDay()) != null){
                    orderAllCounts = allSchoolMap.get(record.getDay()).getTotalCount();
                    orderAllMoneyAmount = allSchoolMap.get(record.getDay()).getTotalPrice();
                }
                dto.setRunSchoolTotal(runSchoolTotal);
                dto.setRunAllCounts(runAllCounts);
                dto.setRunMoneyAmount(runMoneyAmount);
                dto.setOrderAllCounts(orderAllCounts);
                dto.setOrderAllMoneyAmount(orderAllMoneyAmount);
                dto.setUpSendCount(orderAllCounts - dto.getDownSendCount());
                dto.setUpSendMoney(dto.getSendPrice().subtract(dto.getDownSendMoney()));
                //转换返回
                record.setEveryDayCount(dto);
            }
            return new ResponseObject(true, "ok")
                    .push("total", list.getTotal())
                    .push("list", list.getRecords())
                    ;
        }
        // 商铺日志
        if ("商铺日志".equals(type)){
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
                dayLog.setUpSendCount(dayLog.getTotalCount() - dayLog.getDownSendCount());
                dayLog.setUpSendMoney(dayLog.getSendPrice().subtract(dayLog.getDownSendMoney()));
            }
        }
        return new ResponseObject(true, "ok")
                .push("total", list.getTotal())
                .push("list", list.getRecords())
                ;

    }
}
