package net.dg.controller;

import net.dg.model.Product;
import net.dg.service.ProductService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;


@Controller
public class ProductController {

    @Value("${uploadDir}")
    private String uploadFolder;

    private final static String REDIRECT_ADMIN_PRODUCTS = "redirect:/admin/products/show";

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/admin/products/show")
    public String show(Model model) {
        return findPaginated(model, 1);
    }

    @RequestMapping("/admin/products/show/page/{pageNum}")
    public String findPaginated(Model model,
                                @PathVariable(name = "pageNum") int pageNum) {

        Page<Product> page = productService.listAll(pageNum);

        List<Product> productList = page.getContent();

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute("productList", productList);

        return "admin/products";
    }

    @GetMapping("/admin/showNewProductForm")
    public String viewProductPage() {

        return "product/addproduct";
    }

    @GetMapping("/admin/deleteProduct/{id}")
    public String deleteProduct(@PathVariable(value = "id") Long id) {
        this.productService.deleteProductById(id);
        return REDIRECT_ADMIN_PRODUCTS;
    }

    @PostMapping("/admin/product/saveProductDetails")
    public @ResponseBody
    ResponseEntity<?> createProduct(@RequestParam("name") String name,
                                    @RequestParam("price") BigDecimal price, @RequestParam("description") String description
            , @RequestParam("quantity") int quantity, Model model, HttpServletRequest request
            , final @RequestParam("image") MultipartFile file) {

        try {

            String uploadDirectory = request.getServletContext().getRealPath(uploadFolder);
            String fileName = file.getOriginalFilename();
            String filePath = Paths.get(uploadDirectory, fileName).toString();

            if (fileName == null || fileName.contains("..")) {
                model.addAttribute("invalid", "Sorry! Filename contains invalid path sequence \" + fileName");
                return new ResponseEntity<>("Sorry! Filename contains invalid path sequence " + fileName, HttpStatus.BAD_REQUEST);

            }
            String[] names = name.split(",");
            String[] descriptions = description.split(",");
            Date createDate = new Date();

            try {

                File dir = new File(uploadDirectory);
                if (!dir.exists()) {

                    dir.mkdirs();
                }

                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath)));
                stream.write(file.getBytes());
                stream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            byte[] imageData = file.getBytes();
            Product product = new Product();
            product.setName(names[0]);
            product.setImage(imageData);
            product.setPrice(price);
            product.setDescription(descriptions[0]);
            product.setCreateDate(createDate);
            product.setQuantity(quantity);
            productService.saveProduct(product);

            return new ResponseEntity<>("Product Saved With File - " + fileName, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/display/{id}")
    @ResponseBody
    public void showImage(@PathVariable("id") Long id, HttpServletResponse response, Optional<Product> product)
            throws IOException {

        if (product.isPresent()) {

            product = productService.getProductById(id);
            response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
            response.getOutputStream().write(product.get().getImage());
            response.getOutputStream().close();
        }
    }

    @GetMapping("/product/productDetails")
    public String showProductDetails(@RequestParam("id") Long id, Optional<Product> product, Model model) {
        try {

            if (id != 0) {
                product = productService.getProductById(id);


                if (product.isPresent()) {
                    model.addAttribute("id", product.get().getId());
                    model.addAttribute("description", product.get().getDescription());
                    model.addAttribute("name", product.get().getName());
                    model.addAttribute("price", product.get().getPrice());
                    return "user/productdetails";
                }
                return REDIRECT_ADMIN_PRODUCTS;
            }
            return REDIRECT_ADMIN_PRODUCTS;
        } catch (Exception e) {
            e.printStackTrace();
            return REDIRECT_ADMIN_PRODUCTS;
        }
    }


}
