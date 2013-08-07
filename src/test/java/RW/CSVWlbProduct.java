package RW; /**
 * 版权所有(C)，上海海鼎信息工程股份有限公司，2013，所有权利保留。
 *
 * 项目名：	h5-ejb-core
 * 文件名：	WlbProductImp.java
 * 模块说明：	
 * 修改历史：
 * 2013-7-31 - qyh - 创建。
 */

import com.alex.fileparse.csv.CsvBean;
import com.alex.fileparse.csv.annotation.Column;
import com.alex.fileparse.csv.annotation.Pattern;

import java.util.Date;

/**
 * @author qyh
 */
public class CSVWlbProduct extends CsvBean {
    private static final long serialVersionUID = 300100L;

    /**
     * 商品码
     */
    @Column(name = "款号", length = 40, nullable = false)
    private String productCode;
    /**
     * 特征码
     */
    @Column(name = "尺码", length = 200, nullable = false)
    private String featureCode;
    /**
     * 起始日期
     */
    @Column(name = "开始日期", nullable = false)
    @Pattern(value = "yyyy-MM-dd", description = "必须是\"年-月-日\"格式。")
    private Date beginDate;
    /**
     * 截止日期
     */
    @Column(name = "截止日期", nullable = false)
    @Pattern(value = "yyyy-MM-dd", description = "必须是\"年-月-日\"格式。")
    private Date endDate;

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getFeatureCode() {
        return featureCode;
    }

    public void setFeatureCode(String featureCode) {
        this.featureCode = featureCode;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
