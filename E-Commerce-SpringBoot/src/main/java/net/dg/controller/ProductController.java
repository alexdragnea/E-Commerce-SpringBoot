package net.dg.controller;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import net.dg.model.Product;
import net.dg.service.ProductService;

@Controller
public class ProductController {

	@Value("${uploadDir}")
	private String uploadFolder;

	@Autowired
	private ProductService productService;

	private final Logger log 
	= LoggerFactory.getLogger(this.getClass());

	@GetMapping("/admin/products/show")
	String show(Model map) {
		List<Product> product = productService.getAllProducts();
		map.addAttribute("product", product);
		return "products";
	}
	
	@GetMapping("/admin/showNewProductForm")
	public String viewProductPage() {

		return "addproduct";
	}
	
	@GetMapping("/admin/deleteProduct/{id}")
	public String deleteProduct(@PathVariable (value = "id") Long id) {
		  this.productService.deleteProductById(id);
		  return "redirect:/admin/products/show";
	}

	@PostMapping("/admin/product/saveProductDetails")
	public @ResponseBody ResponseEntity<?> createProduct(@RequestParam("name") String name,
			@RequestParam("price") double price, @RequestParam("description") String description, Model model, HttpServletRequest request
			,final @RequestParam("image") MultipartFile file) {
		
		try {
			
			String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
			log.info("uploadDirectory:: " + uploadDirectory);
			String fileName = file.getOriginalFilename();
			String filePath = Paths.get(uploadDirectory, fileName).toString();
			log.info("FileName: " + file.getOriginalFilename());
			if (fileName == null || fileName.contains("..")) {
				model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
				return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);
				
			}
			String[] names = name.split(",");
			String[] descriptions = description.split(",");
			Date createDate = new Date();
			log.info("Name: " + names[0]+" "+filePath);
			log.info("description: " + descriptions[0]);
			log.info("price: " + price);
			try {
				
				File dir = new File(uploadDirectory);
				if (!dir.exists()) {
					log.info("Folder Created");
					dir.mkdirs();
				}
				
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
				stream.write(file.getBytes());
				stream.close();
			} catch (Exception e) {
				log.info("in catch");
				e.printStackTrace();
			}
			byte[] imageData = file.getBytes();
			Product product = new Product();
			product.setName(names[0]);
			product.setImage(imageData);
			product.setPrice(price);
			product.setDescription(descriptions[0]);
			product.setCreateDate(createDate);
			productService.saveProduct(product);
			log.info("HttpStatus===" + new ResponseEntity<>(HttpStatus.OK));
			return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Exception: " + e);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/admin/product/display/{id}")
	@ResponseBody
	void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Product> product)
			throws ServletException, IOException {
		log.info("Id :: " + id);
		product = productService.getProductById(id);
		response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
		response.getOutputStream().write(product.get().getImage());
		response.getOutputStream().close();
	}

	@GetMapping("/admin/product/productDetails")
	String showProductDetails(@RequestParam("id") Long id, Optional<Product> product, Model model) {
		try {
			log.info("Id :: " + id);
			if (id != 0) {
				product = productService.getProductById(id);
			
				log.info("products :: " + product);
				if (product.isPresent()) {
					model.addAttribute("id", product.get().getId());
					model.addAttribute("description", product.get().getDescription());
					model.addAttribute("name", product.get().getName());
					model.addAttribute("price", product.get().getPrice());
					return "productdetails";
				}
				return "redirect:/admin/products/show";
			}
		return "redirect:/admin/products/show";
		} catch (Exception e) {
			e.printStackTrace();
			return "redirect:/admin/products/show";
		}	
	}
	
	@GetMapping("/showFormForUpdateProduct/{id}")
	public String showFormForUpdate(@PathVariable( value = "id") Long id, Model model) {
		
		Optional<Product> product = productService.getProductById(id);
		model.addAttribute("product", product);
		return "update_product";
	}
	
	@GetMapping("/products")
	String showProducts(Model map) {
		List<Product> productList = productService.getAllProducts();
		map.addAttribute("productList", productList);
		return "productlist";
	}
	

	

}
