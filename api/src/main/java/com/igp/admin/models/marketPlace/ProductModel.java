package com.igp.admin.models.marketPlace;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import scala.Int;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by suditi on 16/1/18.
 */
public class ProductModel {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("mrp")
    private BigDecimal mrp;

    @JsonProperty("sprice")
    private BigDecimal sellingPrice;

    @JsonProperty("dprice")
    private Integer displayPrice;

    @JsonProperty("image")
    private String image;

    @JsonProperty("lbh")
    private String lbh;

    @JsonProperty("sdesc")
    private String shortDescription;

    @JsonProperty("volume")
    private Integer volumeWeight;

    @JsonProperty("pcode")
    private String productCode;

    @JsonProperty("currency")
    private Integer baseCurrency;

    @JsonProperty("ptype")
    private String prodType;

    @JsonProperty("stotal")
    private BigDecimal subTotal;

    @JsonProperty("stotalusd")
    private BigDecimal subTotalUSD;

    @JsonProperty("qty")
    private Integer quantity;

    @JsonProperty("gbox")
    private Integer giftBox;

    @JsonProperty("gboxprc")
    private Integer giftBoxPrice;

    @JsonProperty("gboxprcusd")
    private BigDecimal giftBoxPriceUsd;

    @JsonProperty("dsc")
    private Boolean discountApplied;

    @JsonProperty("disamnt")
    private BigDecimal perProductDiscount;

    @JsonProperty("stype")
    private String serviceType;

    @JsonProperty("stypeid")
    private String serviceTypeId;

    @JsonProperty("scharge")
    private BigDecimal serviceCharge;

    @JsonProperty("ppheral")
    private BigDecimal peripheral;

    @JsonProperty("ppheralusd")
    private BigDecimal peripheralUsd;

    @JsonProperty("pprice")
    private BigDecimal pprice;

    @JsonProperty("ppriceusd")
    private BigDecimal ppriceUsd;

    @JsonIgnore
    private BigDecimal ppriceUsdQuant;

    @JsonProperty("schargeusd")
    private BigDecimal serviceChargeUSD;

    @JsonProperty("sdate")
    private String serviceDate;

    @JsonProperty("stime")
    private String serviceTime;

    @JsonProperty("scity")
    private String serviceCity;

    @JsonProperty("attr")
    private Map<String, String> displayAttrList;

    @JsonProperty("relpid")
    private String relProdId;

    @JsonProperty("p")
    private ProductModel product;

    @JsonProperty("error")
    private Boolean error;

    @JsonProperty("errtype")
    private Integer errorType;

    @JsonProperty("fkid")
    private Integer fkId;

    private String voucher;
    private Integer cartValue;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMrp() {
        return mrp;
    }

    public void setMrp(BigDecimal mrp) {
        this.mrp = mrp;
    }

    public BigDecimal getSellingPrice() {
        return sellingPrice;
    }

    public void setSellingPrice(BigDecimal sellingPrice) {
        this.sellingPrice = sellingPrice;
    }

    public Integer getDisplayPrice() {
        return displayPrice;
    }

    public void setDisplayPrice(Integer displayPrice) {
        this.displayPrice = displayPrice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLbh() {
        return lbh;
    }

    public void setLbh(String lbh) {
        this.lbh = lbh;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public Integer getVolumeWeight() {
        return volumeWeight;
    }

    public void setVolumeWeight(Integer volumeWeight) {
        this.volumeWeight = volumeWeight;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(Integer baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getSubTotalUSD() {
        return subTotalUSD;
    }

    public void setSubTotalUSD(BigDecimal subTotalUSD) {
        this.subTotalUSD = subTotalUSD;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getGiftBox() {
        return giftBox;
    }

    public void setGiftBox(Integer giftBox) {
        this.giftBox = giftBox;
    }

    public Integer getGiftBoxPrice() {
        return giftBoxPrice;
    }

    public void setGiftBoxPrice(Integer giftBoxPrice) {
        this.giftBoxPrice = giftBoxPrice;
    }

    public BigDecimal getGiftBoxPriceUsd() {
        return giftBoxPriceUsd;
    }

    public void setGiftBoxPriceUsd(BigDecimal giftBoxPriceUsd) {
        this.giftBoxPriceUsd = giftBoxPriceUsd;
    }

    public Boolean getDiscountApplied() {
        return discountApplied;
    }

    public void setDiscountApplied(Boolean discountApplied) {
        this.discountApplied = discountApplied;
    }

    public BigDecimal getPerProductDiscount() {
        return perProductDiscount;
    }

    public void setPerProductDiscount(BigDecimal perProductDiscount) {
        this.perProductDiscount = perProductDiscount;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeId() {
        return serviceTypeId;
    }

    public void setServiceTypeId(String serviceTypeId) {
        this.serviceTypeId = serviceTypeId;
    }

    public BigDecimal getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(BigDecimal serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public BigDecimal getPeripheral() {
        return peripheral;
    }

    public void setPeripheral(BigDecimal peripheral) {
        this.peripheral = peripheral;
    }

    public BigDecimal getPeripheralUsd() {
        return peripheralUsd;
    }

    public void setPeripheralUsd(BigDecimal peripheralUsd) {
        this.peripheralUsd = peripheralUsd;
    }

    public BigDecimal getPprice() {
        return pprice;
    }

    public void setPprice(BigDecimal pprice) {
        this.pprice = pprice;
    }

    public BigDecimal getPpriceUsd() {
        return ppriceUsd;
    }

    public void setPpriceUsd(BigDecimal ppriceUsd) {
        this.ppriceUsd = ppriceUsd;
    }

    public BigDecimal getPpriceUsdQuant() {
        return ppriceUsdQuant;
    }

    public void setPpriceUsdQuant(BigDecimal ppriceUsdQuant) {
        this.ppriceUsdQuant = ppriceUsdQuant;
    }

    public BigDecimal getServiceChargeUSD() {
        return serviceChargeUSD;
    }

    public void setServiceChargeUSD(BigDecimal serviceChargeUSD) {
        this.serviceChargeUSD = serviceChargeUSD;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(String serviceTime) {
        this.serviceTime = serviceTime;
    }

    public String getServiceCity() {
        return serviceCity;
    }

    public void setServiceCity(String serviceCity) {
        this.serviceCity = serviceCity;
    }

    public Map<String, String> getDisplayAttrList() {
        return displayAttrList;
    }

    public void setDisplayAttrList(Map<String, String> displayAttrList) {
        this.displayAttrList = displayAttrList;
    }

    public String getRelProdId() {
        return relProdId;
    }

    public void setRelProdId(String relProdId) {
        this.relProdId = relProdId;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public Integer getErrorType() {
        return errorType;
    }

    public void setErrorType(Integer errorType) {
        this.errorType = errorType;
    }

    public Integer getFkId() {
        return fkId;
    }

    public void setFkId(Integer fkId) {
        this.fkId = fkId;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public Integer getCartValue() {
        return cartValue;
    }

    public void setCartValue(Integer cartValue) {
        this.cartValue = cartValue;
    }

    private ProductModel(Builder builder) {
        id = builder.id;
        name = builder.name;
        mrp = builder.mrp;
        sellingPrice = builder.sellingPrice;
        displayPrice = builder.displayPrice;
        image = builder.image;
        lbh = builder.lbh;
        shortDescription = builder.shortDescription;
        volumeWeight = builder.volumeWeight;
        productCode = builder.productCode;
        baseCurrency = builder.baseCurrency;
        prodType = builder.prodType;
        subTotal = builder.subTotal;
        subTotalUSD = builder.subTotalUSD;
        quantity = builder.quantity;
        giftBox = builder.giftBox;
        giftBoxPrice = builder.giftBoxPrice;
        giftBoxPriceUsd = builder.giftBoxPriceUsd;
        discountApplied = builder.discountApplied;
        perProductDiscount = builder.perProductDiscount;
        serviceType = builder.serviceType;
        serviceTypeId = builder.serviceTypeId;
        serviceCharge = builder.serviceCharge;
        peripheral = builder.peripheral;
        peripheralUsd = builder.peripheralUsd;
        pprice = builder.pprice;
        ppriceUsd = builder.ppriceUsd;
        ppriceUsdQuant = builder.ppriceUsdQuant;
        serviceChargeUSD = builder.serviceChargeUSD;
        serviceDate = builder.serviceDate;
        serviceTime = builder.serviceTime;
        serviceCity = builder.serviceCity;
        displayAttrList = builder.displayAttrList;
        relProdId = builder.relProdId;
        product = builder.product;
        error = builder.error;
        errorType = builder.errorType;
        fkId = builder.fkId;
        voucher = builder.voucher;
        cartValue = builder.cartValue;
    }


    public static final class Builder {
        private Integer id;
        private String name;
        private BigDecimal mrp;
        private BigDecimal sellingPrice;
        private Integer displayPrice;
        private String image;
        private String lbh;
        private String shortDescription;
        private Integer volumeWeight;
        private String productCode;
        private Integer baseCurrency;
        private String prodType;
        private BigDecimal subTotal;
        private BigDecimal subTotalUSD;
        private Integer quantity;
        private Integer giftBox;
        private Integer giftBoxPrice;
        private BigDecimal giftBoxPriceUsd;
        private Boolean discountApplied;
        private BigDecimal perProductDiscount;
        private String serviceType;
        private String serviceTypeId;
        private BigDecimal serviceCharge;
        private BigDecimal peripheral;
        private BigDecimal peripheralUsd;
        private BigDecimal pprice;
        private BigDecimal ppriceUsd;
        private BigDecimal ppriceUsdQuant;
        private BigDecimal serviceChargeUSD;
        private String serviceDate;
        private String serviceTime;
        private String serviceCity;
        private Map<String, String> displayAttrList;
        private String relProdId;
        private ProductModel product;
        private Boolean error;
        private Integer errorType;
        private Integer fkId;
        private String voucher;
        private Integer cartValue;

        public Builder() {
        }

        public Builder id(Integer val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder mrp(BigDecimal val) {
            mrp = val;
            return this;
        }

        public Builder sellingPrice(BigDecimal val) {
            sellingPrice = val;
            return this;
        }

        public Builder displayPrice(Integer val) {
            displayPrice = val;
            return this;
        }

        public Builder image(String val) {
            image = val;
            return this;
        }

        public Builder lbh(String val) {
            lbh = val;
            return this;
        }

        public Builder shortDescription(String val) {
            shortDescription = val;
            return this;
        }

        public Builder volumeWeight(Integer val) {
            volumeWeight = val;
            return this;
        }

        public Builder productCode(String val) {
            productCode = val;
            return this;
        }

        public Builder baseCurrency(Integer val) {
            baseCurrency = val;
            return this;
        }

        public Builder prodType(String val) {
            prodType = val;
            return this;
        }

        public Builder subTotal(BigDecimal val) {
            subTotal = val;
            return this;
        }

        public Builder subTotalUSD(BigDecimal val) {
            subTotalUSD = val;
            return this;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder giftBox(Integer val) {
            giftBox = val;
            return this;
        }

        public Builder giftBoxPrice(Integer val) {
            giftBoxPrice = val;
            return this;
        }

        public Builder giftBoxPriceUsd(BigDecimal val) {
            giftBoxPriceUsd = val;
            return this;
        }

        public Builder discountApplied(Boolean val) {
            discountApplied = val;
            return this;
        }

        public Builder perProductDiscount(BigDecimal val) {
            perProductDiscount = val;
            return this;
        }

        public Builder serviceType(String val) {
            serviceType = val;
            return this;
        }

        public Builder serviceTypeId(String val) {
            serviceTypeId = val;
            return this;
        }

        public Builder serviceCharge(BigDecimal val) {
            serviceCharge = val;
            return this;
        }

        public Builder peripheral(BigDecimal val) {
            peripheral = val;
            return this;
        }

        public Builder peripheralUsd(BigDecimal val) {
            peripheralUsd = val;
            return this;
        }

        public Builder pprice(BigDecimal val) {
            pprice = val;
            return this;
        }

        public Builder ppriceUsd(BigDecimal val) {
            ppriceUsd = val;
            return this;
        }

        public Builder ppriceUsdQuant(BigDecimal val) {
            ppriceUsdQuant = val;
            return this;
        }

        public Builder serviceChargeUSD(BigDecimal val) {
            serviceChargeUSD = val;
            return this;
        }

        public Builder serviceDate(String val) {
            serviceDate = val;
            return this;
        }

        public Builder serviceTime(String val) {
            serviceTime = val;
            return this;
        }

        public Builder serviceCity(String val) {
            serviceCity = val;
            return this;
        }

        public Builder displayAttrList(Map<String, String> val) {
            displayAttrList = val;
            return this;
        }

        public Builder relProdId(String val) {
            relProdId = val;
            return this;
        }

        public Builder product(ProductModel val) {
            product = val;
            return this;
        }

        public Builder error(Boolean val) {
            error = val;
            return this;
        }

        public Builder errorType(Integer val) {
            errorType = val;
            return this;
        }

        public Builder fkId(Integer val) {
            fkId = val;
            return this;
        }

        public Builder voucher(String val) {
            voucher = val;
            return this;
        }

        public Builder cartValue(Integer val) {
            cartValue = val;
            return this;
        }

        public ProductModel build() {
            return new ProductModel(this);
        }
    }
}
