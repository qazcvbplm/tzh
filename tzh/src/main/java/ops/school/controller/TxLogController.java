package ops.school.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import ops.school.api.entity.TxLog;
import ops.school.api.service.TxLogService;
import ops.school.api.util.ResponseObject;
import ops.school.service.TCommonService;
import ops.school.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@Api(tags="提现记录模块")
@RequestMapping("/ops/txlog")
public class TxLogController {

	@Autowired
    private TxLogService txLogService;
	@Autowired
	private TCommonService tCommonService;

    @ApiOperation(value = "查询", httpMethod = "POST")
	@PostMapping("/find")
	public ResponseObject find(String appId, String schoolId, int page, int size,String txName){
		if (appId == null && schoolId == null) {
			return new ResponseObject(false, "");
		}
		QueryWrapper<TxLog> query=new QueryWrapper<>();
        query.select("id", "txer_id", "type", "create_time", "amount","result","ishow","is_tx","dz_openid","tx_name");
		if(schoolId!=null)
			query.lambda().eq(TxLog::getSchoolId, schoolId);
		if(appId!=null)
			query.lambda().eq(TxLog::getAppId, appId);
		query.lambda().orderByDesc(TxLog::getCreateTime);
		if (txName != null){
			query.lambda().like(TxLog::getTxName,txName);
		}
		IPage<TxLog> rs = txLogService.page(PageUtil.getPage(page, size), query);
		return new ResponseObject(true, "ok").push("list", rs.getRecords()).push("total", rs.getTotal());
	}

	@ApiOperation(value = "按照配送员查询", httpMethod = "GET")
	@GetMapping("/sender/find")
	public ResponseObject find(Integer id, int page, int size) {
		if (id == null || id <= 0) {
			return new ResponseObject(false, "");
		}
		QueryWrapper<TxLog> query = new QueryWrapper<>();
		query.lambda().eq(TxLog::getTxerId, id);
		query.lambda().eq(TxLog::getType, "配送员提现");
		query.lambda().eq(TxLog::getIshow, "0");
		query.select("id", "txer_id", "type", "create_time", "amount","result","ishow","is_tx","dz_openid","tx_name");
		query.lambda().orderByDesc(TxLog::getCreateTime);
		IPage<TxLog> rs = txLogService.page(PageUtil.getPage(page, size), query);
		return new ResponseObject(true, "ok").push("list", rs.getRecords()).push("total", rs.getTotal());
	}

	/**
	 * @date:   2019/8/8 17:38
	 * @author: QinDaoFang
	 * @version:version
	 * @return: ops.school.api.util.ResponseObject
	 * @param   shopId
	 * @param   page
	 * @param   size
	 * @Desc:   desc
	 */
	@ApiOperation(value = "按照商家查询", httpMethod = "POST")
	@RequestMapping(value = "/shop/find",method = RequestMethod.POST)
	public ResponseObject shopFind(Integer shopId, int page, int size) {
		if (shopId == null || shopId <= 0) {
			return new ResponseObject(false, "shopId有误");
		}
		QueryWrapper<TxLog> query = new QueryWrapper<>();
		query.lambda().eq(TxLog::getTxerId, shopId);
		query.lambda().eq(TxLog::getType, "商家提现");
		query.lambda().eq(TxLog::getIshow, "0");
		query.select("id", "txer_id", "type", "create_time", "amount","result","ishow","is_tx","dz_openid","tx_name");
		query.lambda().orderByDesc(TxLog::getCreateTime);
		IPage<TxLog> rs = txLogService.page(PageUtil.getPage(page, size), query);
		return new ResponseObject(true, "ok").push("list", rs.getRecords()).push("total", rs.getTotal());
	}

	/**
	 * 提现申请
	 * @param amount 提现金额
	 * @param shopid 店铺id
	 * @param senderid 配送员id
	 * @param dzOpenid 到账openid
	 * @return
	 */
	@ApiOperation(value = "提现申请", httpMethod = "POST")
	@PostMapping("txapply")
	public ResponseObject txapply(@RequestParam BigDecimal amount, @RequestParam Integer shopid,
								   @RequestParam String senderid,@RequestParam String dzOpenid) {
		int result = tCommonService.txApply(amount,senderid,dzOpenid,shopid);
		if (result == 1) {
			return new ResponseObject(true, "申请提现成功");
		} else if (result == 2){
			return new ResponseObject(false, "商家暂未开启提现功能");
		} else if (result == 3){
			return new ResponseObject(false, "用户还有审核中记录，暂无法继续提现");
		}
		return new ResponseObject(false, "申请提现失败");
	}

	/**
	 * 提现审核
	 * @param txId 提现记录id
	 * @param status 审核状态（1.成功 2.失败）
	 * @return
	 */
	@ApiOperation(value = "提现审核",httpMethod = "POST")
	@PostMapping("txaudit")
	public ResponseObject txaudit(@RequestParam Integer txId, @RequestParam Integer status){
		int rs = tCommonService.txAudit(txId,status);
		if (rs == 1) {
			return new ResponseObject(true, "提现成功");
		}
		return new ResponseObject(true,"提现失败");
	}
}
