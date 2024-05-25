package repositiory;

import java.util.HashMap;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.springwebservices.product.schema.beans.Product;

@Component
public class ProductRepository {
	private static final Map<String, Product> products = new HashMap<String, Product>();
	

	public void init(){
		Product p1 =new Product();
		p1.setCode("111");
		p1.setDescription("first");
		p1.setPrice(5000);
		products.put("first",p1);
		
		Product p2 =new Product();
		p2.setCode("222");
		p2.setDescription("second");
		p2.setPrice(10000);
		products.put("second",p2);
	}
	
	public Product findproduct(String name) {
		init();
		return products.get(name);
	}
	
}
