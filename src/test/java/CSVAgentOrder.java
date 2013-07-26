import com.alex.fileparse.csv.CsvBean;
import com.alex.fileparse.csv.annotation.Column;

import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: alex
 * Date: 13-7-23
 * Time: 下午12:43
 * To change this template use File | Settings | File Templates.
 */
public class CSVAgentOrder extends CsvBean {
    @Column(name = "网络订单号", nullable = false, needTab = true, length = 50)
    private String orderNum;
    @Column(name = "款号")
    private String featureCode;
    @Column(name = "尺码")
    private String size;
    @Column(name = "数量")
    private BigDecimal qty;
    @Column(name = "销售单价")
    private BigDecimal price;
    @Column(name = "运费")
    private BigDecimal postFee;
    @Column(name = "描述")
    private String remark;
    @Column(name = "买家会员名")
    private String buyerName;
    @Column(name = "收货人姓名")
    private String receiverName;
    @Column(name = "省")
    private String province;
    @Column(name = "市")
    private String city;
    @Column(name = "区")
    private String district;
    @Column(name = "详细地址")
    private String street;
    @Column(name = "邮政编码")
    private String postCode;
    @Column(name = "联系电话", needTab = true)
    private String phone;
    @Column(name = "联系手机", needTab = true)
    private String mobile;
    @Column(name = "传真", needTab = true)
    private String fax;
    @Column(name = "电子邮件")
    private String email;
    @Column(name = "原始外部定单号", needTab = true)
    private String sourceOrderNum;
    @Column(name = "是否虚单")
    private Boolean isVirtual;
    @Column(name = "是否货到付款")
    private Boolean isCod;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPostFee() {
        return postFee;
    }

    public void setPostFee(BigDecimal postFee) {
        this.postFee = postFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSourceOrderNum() {
        return sourceOrderNum;
    }

    public void setSourceOrderNum(String sourceOrderNum) {
        this.sourceOrderNum = sourceOrderNum;
    }

    public Boolean getIsVirtual() {
        return isVirtual;
    }

    public void setIsVirtual(Boolean virtual) {
        isVirtual = virtual;
    }

    public Boolean getIsCod() {
        return isCod;
    }

    public void setIsCod(Boolean cod) {
        isCod = cod;
    }
}
