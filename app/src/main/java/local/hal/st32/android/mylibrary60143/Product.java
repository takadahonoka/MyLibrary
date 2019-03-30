package local.hal.st32.android.mylibrary60143;

/**
 * 商品クラス。
 */

public class Product {

    private String productId;
    private String productName;
    private String productType;
    private String productNumber;
    private String productDateType;
    private String productDate;
    private String productMemo;
    private String productImage;

    private Boolean errorFlag;

    public Product(){
        this.productId = "";
        this.productName = "";
        this.productType = "";
        this.productNumber = "";
        this.productDateType = "";
        this.productDate = "";
        this.productMemo = "";
        this.productImage = "";

        this.errorFlag = true;
    }

    /**
     * エラーフラグ
     */
    public Boolean getErrorFlag() {
        return errorFlag;
    }

    /**
     * ID
     */
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * 商品名
     */
    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
        if("".equals(productName)){
            errorFlag = false;
        }
    }

    /**
     * 商品種別
     */
    public String getProductType() {
        return productType;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }

    /**
     * 商品個数
     */
    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    /**
     * 期限種別
     */
    public String getProductDateType() {
        return productDateType;
    }

    public void setProductDateType(String productDateType) {
        this.productDateType = productDateType;
    }

    /**
     * 期限
     */
    public String getProductDate() {
        return productDate;
    }

    public void setProductDate(String productDate) {
        this.productDate = productDate;
    }

    /**
     * メモ
     */
    public String getProductMemo() {
        return productMemo;
    }

    public void setProductMemo(String productMemo) {
        this.productMemo = productMemo;
    }

    /**
     * 写真
     */
    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }
}
