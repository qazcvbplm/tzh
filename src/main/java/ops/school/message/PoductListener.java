package ops.school.message;

import ops.school.api.entity.OrderProduct;
import ops.school.api.entity.Orders;
import ops.school.api.service.OrdersService;
import ops.school.api.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PoductListener {

	@Autowired
	private OrdersService orderService;
	@Autowired
    private ProductService productService;
	
	 public void receiveMessage(String message){
		 Orders orders=orderService.findById(message);
		 List<OrderProduct> list=orders.getOp();
		 for(OrderProduct temp:list){
			 Map<String,Object> map=new HashMap<>();
			 map.put("id", temp.getProductId());
			 map.put("count", temp.getProductCount());
             productService.sales(map);
		 }
	  }
}