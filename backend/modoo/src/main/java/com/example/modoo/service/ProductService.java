package com.example.modoo.service;

import com.example.modoo.dto.ProductDto;
import com.example.modoo.dto.StoreDto;
import com.example.modoo.entity.Member;
import com.example.modoo.entity.Product;
import com.example.modoo.entity.Store;
import com.example.modoo.repository.MemberRepository;
import com.example.modoo.repository.ProductRepository;
import com.example.modoo.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserEmailLookupService userEmailLookupService;

    @Autowired
    private FileService fileService;

    /**
     * 파일을 작성하는 비즈니스 로직을 수항하는 메서드.
     * @param productDto
     * @param files
     * @return
     */
    public Product saveProduct(ProductDto productDto, List<MultipartFile> files) {

        // 현재 인증된 사용자의 이메일 가져오기
        String userEmail = userEmailLookupService.getCurrentUserEmail();

        // userEmail을 이용해 Member를 조회
        Member member = memberRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("Member 엔티티에서 해당하는 email을 찾을 수 없습니다: " + userEmail));

        // Member를 이용해 Store를 조회
        Store store = storeRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new RuntimeException("Store 엔티티에서 해당하는 member를 찾을 수 없습니다: " + member.getId()));

        // DTO에서 엔티티로 변환
        Product product = new Product();
        product.setTitle(productDto.getTitle());
        product.setCondition(productDto.getCondition());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setAcceptOffers(productDto.getAcceptOffers());
        product.setStore(store);

        // 파일 저장 로직: 이 부분 로직 다시 검토해야함
        List<String> fileUrls = fileService.saveFiles(files);
        product.setFileUrls(fileUrls);

        Product savedProduct = productRepository.save(product);
        fileService.saveFiles(files);

        return  savedProduct;
    }

    /**
     * 제품 id를 통해 제품을 조회하는 메서드
     *
     * @param productId
     * @return
     */
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDto(product);
    }

    /**
     * 상점 id를 기준으로 상점에 등록되어있는 제품 목록을 조회하는 메서드
     *
     * @param storeId
     * @return
     */
    public List<ProductDto> getProductsByStoreId(Long storeId) {
        List<Product> products = productRepository.findByStoreId(storeId);
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     *
     *
     * @param productId
     * @return
     */
    public ProductDto likeProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDto productDto = convertToDto(product);
        StoreDto storeDto = productDto.getStore();
        String email = userEmailLookupService.getCurrentUserEmail();

        if (storeDto.getEmail().equals(email)) {
            throw new RuntimeException("You cannot like your own product.");
        }

        // 찜 수를 하나 증가시킴
        product.setLikedCount(product.getLikedCount() + 1);

        Product updatedProduct = productRepository.save(product);

        return convertToDto(updatedProduct);
    }

    /**
     *
     *
     * @param query
     * @return
     */
    public List<ProductDto> searchProducts(String query) {
        List<Product> products = productRepository.findByTitleContaining(query);
        return products.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     *
     *
     * @param count
     * @return
     */
    public List<ProductDto> getRandomProducts(int count) {
        List<Product> randomProducts = productRepository.findRandomProducts(count);
        return randomProducts.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    /**
     * 사용자가 상품을 삭제하는 비즈니스 로직을 처리하는 메서드.
     *
     * @param productId
     * @param userEmail
     */
    public void deleteProduct(Long productId, String userEmail) {
        // 상품 조회
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 상품을 찾을 수 없습니다: " + productId));

        // 삭제 권한 확인.
        if(!product.getStore().getEmail().equals(userEmail)) {
            throw new RuntimeException("삭제 권한이 없습니다.");
        } else {
            // 상품 삭제
            productRepository.delete(product);
        }
    }

    // Product 엔티티를 ProductDto로 변환하는 메서드
    public ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setId(product.getId());
        productDto.setTitle(product.getTitle());
        productDto.setCondition(product.getCondition());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setLikedCount(product.getLikedCount());
        productDto.setAcceptOffers(product.isAcceptOffers());
        productDto.setFileUrls(product.getFileUrls()); // fileUrls 필드 설정

        if (product.getStore() != null) {
            StoreDto storeDto = new StoreDto();
            storeDto.setId(product.getStore().getId());
            storeDto.setName(product.getStore().getName());
            storeDto.setEmail(product.getStore().getEmail());
            storeDto.setNote(product.getStore().getNote());
            productDto.setStore(storeDto);
        }

        return productDto;
    }
}
