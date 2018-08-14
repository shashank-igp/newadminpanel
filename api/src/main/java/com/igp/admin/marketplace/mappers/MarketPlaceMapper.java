package com.igp.admin.marketplace.mappers;

import com.igp.admin.marketplace.endpoints.MarketPlaceOrder;
import com.igp.admin.marketplace.models.*;
import com.igp.admin.marketplace.utils.CurrencyConversionUtil;
import com.igp.admin.marketplace.utils.MarketPlaceOrderUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by suditi on 15/1/18.
 */
public class MarketPlaceMapper {
    private static final Logger logger = LoggerFactory.getLogger(MarketPlaceMapper.class);

    public int findVendor(String value){
        int fkAssociateId = 0;
        MarketPlaceOrderUtil marketPlaceOrderUtil = new MarketPlaceOrderUtil();
        Map<String,Integer> marketPlaceVendorMap = new HashMap<String,Integer>() {

            {
                //Map(value,fkAssociateId)  user

                put("RL",433);     //Rewardz
                put("Rediff",457);      //Rediff
                put("TL",434);          //Talash
                put("SC",510);          //ShopClues
                put("SD",437);          //SnapDeal
                put("AD",580);          //Amazon
                put("GLA",504);         //Gift A Love
                put("AWS",511);         //Awesomeji
                put("KAV",541);         //Kavya(Aus)
                put("UKG",546);         //UKGiftsPortal
                put("oyo",547);         //Oyo
                put("corp",556);        //Corporate orders
                put("artisanG",561);    //Artisan Gilt
                put("Inductus",562);    //Inductus
                put("FNP",741);         //Fnp International
                put("JnJ",769);         //Johnsons and Johnsons
                put("INFUK",841);       //Interflora International
                put("ZIFIT",859);       // rakhi zifit vendor at live
            }
        };

        fkAssociateId = marketPlaceVendorMap.get(value)==null?0:marketPlaceVendorMap.get(value);
        if(fkAssociateId!=0){
            boolean affiliate =  marketPlaceOrderUtil.checkIfAffliate(fkAssociateId);
            if(affiliate==false){
                // not an affliate vendor
                fkAssociateId=0;
            }
        }
        return fkAssociateId;
    }

    public  Map<Integer, Map<String, String>> parseExcelForMarketPlace(FormDataMultiPart multiPart, String filePrefix, int fkAsId) {
        Map<Integer, Map<String, String>> data= new HashMap<>();
        int NUM_COLUMNS = 20;
        if(fkAsId == 433 || fkAsId == 859){
            NUM_COLUMNS = 25;
        }
        MarketPlaceOrderUtil marketPlaceOrderUtil = new MarketPlaceOrderUtil();
        try {
            FileUploadModel fileUploadModel = marketPlaceOrderUtil.uploadTheFile(multiPart, filePrefix);
            boolean chkExtension = fileUploadModel.getFileExtension().equalsIgnoreCase("xls") || fileUploadModel.getFileExtension().equalsIgnoreCase("xlsx");
            //check that file is in excel format.
            if (fileUploadModel.getError() == false && chkExtension) {
                // create new file and parse it

                File file = fileUploadModel.getFile();

                Workbook workbook = WorkbookFactory.create(file);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = datatypeSheet.iterator();
                int row = 1;
                // Assuming "column headers" are in the first row
                Row header_row = datatypeSheet.getRow(0);
                // Assuming 4 columns
                List<String> list = new ArrayList<>();
                for (int j = 0; j < NUM_COLUMNS; j++) {
                    Cell header_cell = header_row.getCell(j);
                    list.add(header_cell.getStringCellValue().trim());
                    // filled list with column names.
                }
                // now we store all the values start from next row
                rowIterator.next();
                while (rowIterator.hasNext()) {
                    Map<String, String> a = new HashMap<>();
                    row++;
                    // check if we get second row since first has column headers
                    Row currentRow = rowIterator.next();
                    int count = 0;
                    for (int currCol = 0; currCol < NUM_COLUMNS; currCol++) {
                        Cell currentCell = currentRow.getCell(currCol);
                        if (currentCell == null) {
                            a.put(list.get(currCol), "");
                            count++;
                        } else if(currentCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                            switch(currentCell.getCachedFormulaResultType()) {
                                case Cell.CELL_TYPE_NUMERIC:
                                    a.put(list.get(currCol), currentCell.getNumericCellValue()+"");
                                    break;
                                case Cell.CELL_TYPE_STRING:
                                    a.put(list.get(currCol), currentCell.getRichStringCellValue() + "\"");
                                    break;
                            }
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            a.put(list.get(currCol), currentCell.getNumericCellValue() + "");
                            if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                                a.put(list.get(currCol), currentCell.getDateCellValue() + "");
                            }
                        } else  if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                            a.put(list.get(currCol), currentCell.getRichStringCellValue() + "");

                            //   a.put(list.get(currCol), currentCell.getRichStringCellValue() + "");
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                            a.put(list.get(currCol), currentCell.getBooleanCellValue() + "");
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                            a.put(list.get(currCol), "");
                            count++;
                        }
                    }
                    if (count >= 15) {
                        // row is empty.
                        //          logger.debug("row : " + row);
                        //           logger.debug("Empty Row.");
                    } else {
                        data.put(row, a);
                        //         logger.debug("row : " + row);
                        //          logger.debug("values : " + data.get(row));
                    }
                }
            }
        } catch(Exception e){
            logger.error("Exception at populating Map from file : ", e);
            e.printStackTrace();

        }
        logger.debug("size : "+data.size());

        return data;

    }

    public List<ValidationModel> refineDataAndPopulateModels( Map<Integer, Map<String, String>> data,String userValue, Integer fk_associate_id) {
        List<ValidationModel> validationModelList = new ArrayList<>();
        MarketPlaceOrderUtil marketPlaceOrderUtil = new MarketPlaceOrderUtil();
        String prevRlReqId = "";
        int prevRowNum = 0;
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        //   SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date todayDate=new Date();

        for (Map.Entry<Integer, Map<String, String>> entry : data.entrySet()) {
            // revisiting each map i.e. each row
            int rowNumValue = entry.getKey();
            Map<String, String> row = entry.getValue();
            if(row != null || !row.isEmpty()) {
                Iterator<Map.Entry<String, String>> rowNum = row.entrySet().iterator();
                try {

                    ValidationModel validationModel = new ValidationModel();
                    UserModel userModel = null;
                    AddressModel addressModel = new AddressModel();
                    List<ProductModel> productModelList = new ArrayList<>();
                    ExtraInfoModel extraInfoModel = null;
                    DeliveryMessageModel deliveryMessageModel = new DeliveryMessageModel();
                    // visiting each column
                    Map<String, String> column = row;

                    // fill few generic values.
                    validationModel.setFkAssociateId(fk_associate_id);
                    validationModel.setError(Boolean.FALSE);
                    validationModel.setId(0);
                    validationModel.setRowNum(rowNumValue);
                    long millis = System.currentTimeMillis();

                    // take out all the values and fill the models.
                    // i.e. customer,address,product,extra_info


                    if (fk_associate_id == 433) {

                        // loyaty rewardz (RL)
                        extraInfoModel = new ExtraInfoModel.Builder()
                            .gstNo("")
                            .relId(column.get("PO Number"))
                            .marketName("RL")
                            .build();

                        if(rowNumValue!=2 && prevRlReqId.equalsIgnoreCase(extraInfoModel.getRelId()) ){
                            // when next row is old order and extra product
                            ValidationModel validationModelPrev = validationModelList.get(prevRowNum);
                            productModelList=validationModelPrev.getProductModelList();
                            ExtraInfoModel extraInfoModelPrev = validationModelPrev.getExtraInfoModel();

                            String quant = column.get("QTY");
                            int quantity = 0;
                            if (!quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }
                            String sP = column.get("SellingPrice");
                            int sellingPrice = 0;
                            if (!sP.isEmpty()) {
                                double d = Double.parseDouble(sP);
                                sellingPrice = (int) d;
                                if(quantity!=0)
                                    sellingPrice = sellingPrice/quantity; // as it has calculated amount
                            }

                            String itemCode = column.get("Item Code").trim();
                            if(!itemCode.isEmpty()) {
                                String itemCodeArray[] = column.get("Item Code").trim().split("IGP-");
                                if(itemCodeArray.length==2) {
                                    itemCode = itemCodeArray[1];
                                } else {
                                    itemCode = itemCodeArray[0];
                                }
                                // removes IGP-
                                itemCodeArray = itemCode.split("--");
                                itemCode = itemCodeArray[0];
                                // removes --
                                itemCodeArray = itemCode.split("—");
                                itemCode = itemCodeArray[0];
                                // removes —

                                itemCode = marketPlaceOrderUtil.getProductIdForLoyaltyOnly(itemCode);
                            }

                            if(marketPlaceOrderUtil.handelProductOrNot(itemCode)){
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate(simpleDateFormat.format(todayDate))
                                    .serviceTypeId(String.valueOf(4))
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            }else {
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate("1970-01-01")
                                    .serviceTypeId(1 + "")
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            }
                            StringBuilder marketPlaceData = new StringBuilder(extraInfoModelPrev.getMarketData());
                            marketPlaceData.append("(#)" +
                                column.get("Item Code") + "(#)" +
                                quantity + "(#)" +
                                column.get("MRP")+ "(#)" +
                                column.get("POAmount")+ "(#)" +
                                sP + "(#)" +
                                column.get("ProductName"));

                            extraInfoModelPrev.setMarketData(marketPlaceData.toString());

                            validationModelPrev.setExtraInfoModel(extraInfoModelPrev);
                            validationModelPrev.setProductModelList(productModelList);
                            validationModelList.set(prevRowNum,validationModelPrev);

                        }else {
                            // it's a new order id
                            String mprefix = marketPlaceOrderUtil.getMobilePrefixByCountryId(99);
                            String phone = column.get("Contact No");

                            if (!phone.isEmpty()) {
                                phone = new BigDecimal(column.get("Contact No")).toPlainString();
                            }
                            String zipCode = column.get("Zip");
                            if (!zipCode.isEmpty()) {
                                zipCode = zipCode.trim();
                                if (zipCode.length() >= 6) {
                                    zipCode = zipCode.substring(0, 6);
                                }
                            }

                            String name = column.get("MemberName");

                            name = " " + marketPlaceOrderUtil.replaceSpecialChars(name) + " ";
                            name = name.replaceAll("\\sNA\\s", " ").replaceAll("\\sna\\s", " ").replaceAll("\\sNa\\s", " ").trim();
                            String fname = "";
                            String lname = "";

                            if (name != null) {

                                if (name.contains(" ")) {
                                    String[] nameArray = name.trim().split(" ");

                                    fname = nameArray[0];
                                    if (nameArray.length > 1) {

                                        for (int i = 1; i < nameArray.length - 1; i++) {
                                            if (fname.length() + nameArray[i].length() < 20) {
                                                fname += " " + nameArray[i];
                                            } else {
                                                break;
                                            }
                                        }
                                        lname = name.trim().substring(fname.length() + 1, name.trim().length());
                                    }
                                } else {
                                    fname = name;
                                }
                            }


                            String address = column.get("AddressLine1").trim();
                            String address2 = column.get("AddressLine2").trim();

                            address = marketPlaceOrderUtil.replaceSpecialChars(address);
                            address2 = marketPlaceOrderUtil.replaceSpecialChars(address2);

                            String email = column.get("Email").trim();

                            if (email.isEmpty()) {
                                email = "asif@tokenz.com";
                            } else {
                                email = marketPlaceOrderUtil.replaceSpecialChars(email);
                            }

                            userModel = new UserModel.UserBuilder()
                                .id(null)
                                .firstname(fname)
                                .lastname(lname)
                                .addressField1(address + "," + address2 + " " + column.get("City") + "," + column.get("State"))
                                .state(column.get("State"))
                                .city(column.get("City"))
                                .postcode(zipCode)
                                .email(email)
                                .mobile(phone.split("\\.")[0])
                                .mobilePrefix(mprefix)
                                .password(millis + "")
                                .countryId(99)
                                .associateId(5)
                                .uniqsrc("Bulk-" + millis)
                                .build();


                            addressModel.setTitle("");
                            addressModel.setFirstname(userModel.getFirstname());
                            addressModel.setLastname(userModel.getLastname());
                            addressModel.setStreetAddress(userModel.getAddressField1());
                            addressModel.setPostcode(userModel.getPostcode());
                            addressModel.setCity(userModel.getCity());
                            addressModel.setState(userModel.getState());
                            addressModel.setCountryId(userModel.getCountryId().toString());
                            addressModel.setRelation("Self");
                            addressModel.setEmail(userModel.getEmail());
                            addressModel.setMobile(userModel.getMobile());
                            addressModel.setMobilePrefix(mprefix);
                            addressModel.setAddressType(0); // home

                            String quant = column.get("QTY");
                            int quantity = 0;
                            if (!quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }

                            String sP = column.get("SellingPrice");
                            int sellingPrice = 0;
                            if (!sP.isEmpty()) {
                                double d = Double.parseDouble(sP);
                                sellingPrice = (int) d;
                                if(quantity!=0)
                                    sellingPrice = sellingPrice / quantity; // as it has calculated amount
                            }

                            String itemCode = column.get("Item Code").trim();
                            if (!itemCode.isEmpty()) {
                                String itemCodeArray[] = column.get("Item Code").trim().split("IGP-");
                                if (itemCodeArray.length == 2) {
                                    itemCode = itemCodeArray[1];
                                } else {
                                    itemCode = itemCodeArray[0];
                                }
                                // removes IGP-
                                itemCodeArray = itemCode.split("--");
                                itemCode = itemCodeArray[0];
                                // removes --
                                itemCodeArray = itemCode.split("—");
                                itemCode = itemCodeArray[0];
                                // removes —

                                itemCode = marketPlaceOrderUtil.getProductIdForLoyaltyOnly(itemCode);
                            }

                            if (marketPlaceOrderUtil.handelProductOrNot(itemCode)) {
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate(simpleDateFormat.format(todayDate))
                                    .serviceTypeId(String.valueOf(4))
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            } else {
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate("1970-01-01")
                                    .serviceTypeId(1 + "")
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            }

                            String detail = column.get("PO Number") + "(#)" +
                                column.get("MemberName") + "(#)" +
                                userModel.getMobile() + "(#)" +
                                column.get("Email") + "(#)" +
                                address + "(#)" +
                                address2 + "(#)" +
                                column.get("City") + "(#)" +
                                column.get("State") + "(#)" +
                                userModel.getPostcode() + "(#)" +
                                column.get("Item Code") + "(#)" +
                                quantity + "(#)" +
                                column.get("MRP") + "(#)" +
                                column.get("POAmount") + "(#)" +
                                sP + "(#)" +
                                column.get("ProductName") + "(#)" +
                                column.get("ProgramName") + "(#)" +
                                column.get("Vendor") + "(#)" +
                                column.get("Open/Requested(DD/MM/YYYY)") + "(#)" +
                                column.get("POStatus") + "(#)" +
                                column.get("PODate") + "(#)" +
                                column.get("TrackingInfo") + "(#)" +
                                column.get("Courier") + "(#)" +
                                column.get("DispatchDate(DD/MM/YYYY)") + "(#)" +
                                column.get("DeliveryDate(DD/MM/YYYY)") + "(#)" +
                                column.get("CancellationComments");

                            extraInfoModel.setMarketData(detail);
                        }
                    } else if (fk_associate_id == 859){
                        // ZIFIT vendor for rakhi
                        String relId = column.get("order-id");

                        if (!relId.isEmpty()) {
                            relId = new BigDecimal(column.get("order-id")).toPlainString();
                        }
                        extraInfoModel = new ExtraInfoModel.Builder()
                            .gstNo("")
                            .relId(relId)
                            .marketName("ZIFIT")
                            .build();

                        if(rowNumValue!=2 && prevRlReqId.equalsIgnoreCase(extraInfoModel.getRelId()) ){
                            // when next row is old order and extra product
                            ValidationModel validationModelPrev = validationModelList.get(prevRowNum);
                            productModelList=validationModelPrev.getProductModelList();
                            ExtraInfoModel extraInfoModelPrev = validationModelPrev.getExtraInfoModel();

                            String quant = column.get("quantity-purchased");
                            int quantity = 0;
                            if (!quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }
                            String sellP = column.get("item-price");
                            int sellingPrice = 0;
                            if (!sellP.isEmpty()) {
                                // the item price is in USD so convert it in INR
                                BigDecimal d = CurrencyConversionUtil.getConvertedPrice(new BigDecimal(sellP),"USD","INR");
                                sellingPrice = d.intValue();
//                                if(quantity>0)
//                                    sellingPrice = sellingPrice/quantity; // as it has calculated amount
                            }

                            String shipP = column.get("shipping-price");
                            int shippingPrice = 0;
                            if (!shipP.isEmpty()) {
                                // the shipping price is in USD so convert it in INR
                                BigDecimal d = CurrencyConversionUtil.getConvertedPrice(new BigDecimal(shipP),"USD","INR");
                                shippingPrice = d.intValue();
//                                if(quantity>0)
//                                    sellingPrice = sellingPrice/quantity; // as it has calculated amount
                            }

                            String itemCode = column.get("sku").trim();

                            if(marketPlaceOrderUtil.handelProductOrNot(itemCode)){
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("product-name"))
                                    .serviceDate(simpleDateFormat.format(todayDate))
                                    .serviceTypeId(String.valueOf(4))
                                    .serviceCharge(new BigDecimal(shippingPrice))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            }else {
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("product-name"))
                                    .serviceDate(simpleDateFormat.format(todayDate))
                                    .serviceTypeId(String.valueOf(4))
                                    .serviceCharge(new BigDecimal(shippingPrice))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            }
                            StringBuilder marketPlaceData = new StringBuilder(extraInfoModelPrev.getMarketData());
                            marketPlaceData.append("(#)" +
                                column.get("sku") + "(#)" +
                                quantity + "(#)" +
                                column.get("item-price")+ "(#)" +
                                column.get("shipping-price") + "(#)" +
                                column.get("product-name"));

                            extraInfoModelPrev.setMarketData(marketPlaceData.toString());

                            validationModelPrev.setExtraInfoModel(extraInfoModelPrev);
                            validationModelPrev.setProductModelList(productModelList);
                            validationModelList.set(prevRowNum,validationModelPrev);

                        }else {
                            // it's a new order id


                            String mprefix = marketPlaceOrderUtil.getMobilePrefixByCountryId(223);
                            String phone = column.get("buyer-phone-number").trim();

                            if (!phone.isEmpty()) {
                                phone = column.get("buyer-phone-number").replace("-", "").replace("(", "").replace(")", "");
                            }else {
                                phone = "9044974244"; // asif number by default
                            }
                            String zipCode = column.get("ship-postal-code").replace(".0",""); // change it later i think constant

                            String quant = column.get("quantity-purchased");
                            int quantity = 0;
                            if (!quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }

                            String name = column.get("buyer-name").trim();

                            name = " " + marketPlaceOrderUtil.replaceSpecialChars(name) + " ";
                            name = name.replaceAll("\\sNA\\s", " ").replaceAll("\\sna\\s", " ").replaceAll("\\sNa\\s", " ").trim();
                            String fname = "";
                            String lname = "";

                            if (name != null) {

                                if (name.contains(" ")) {
                                    String[] nameArray = name.trim().split(" ");

                                    fname = nameArray[0];
                                    if (nameArray.length > 1) {

                                        for (int i = 1; i < nameArray.length - 1; i++) {
                                            if (fname.length() + nameArray[i].length() < 20) {
                                                fname += " " + nameArray[i];
                                            } else {
                                                break;
                                            }
                                        }
                                        lname = name.trim().substring(fname.length() + 1, name.trim().length());
                                    }
                                } else {
                                    fname = name;
                                }
                            }


                            String address = column.get("ship-address-1").trim();
                            String address2 = column.get("ship-address-2").trim();
                            String address3 = column.get("ship-address-3").trim();
                            // i think it'll be constant as is not there in file

                            address = marketPlaceOrderUtil.replaceSpecialChars(address);
                            address2 = marketPlaceOrderUtil.replaceSpecialChars(address2);

                            String email = column.get("buyer-email").trim();

                            userModel = new UserModel.UserBuilder()
                                .id(null)
                                .firstname(fname)
                                .lastname(lname)
                                .addressField1(address + "," + address2 + "," + address3 + column.get("ship-city") + "," + column.get("ship-state"))
                                .state(column.get("ship-state"))
                                .city(column.get("ship-city"))
                                .postcode(zipCode) // check
                                .email(email)
                                .mobile(phone.split("\\.")[0])
                                .mobilePrefix(mprefix)
                                .password(millis + "")
                                .countryId(223)
                                .associateId(5)
                                .uniqsrc("Bulk-" + millis)
                                .build();

                            phone = column.get("ship-phone-number").trim();

                            if (!phone.isEmpty()) {
                                phone = column.get("ship-phone-number").replace("-", "").replace("(", "").replace(")", "");
                            }else {
                                phone = "9044974244"; // asif number by default
                            }
                            addressModel.setTitle("");
                            addressModel.setFirstname(userModel.getFirstname());
                            addressModel.setLastname(userModel.getLastname());
                            addressModel.setStreetAddress(userModel.getAddressField1());
                            addressModel.setPostcode(userModel.getPostcode());
                            addressModel.setCity(userModel.getCity());
                            addressModel.setState(userModel.getState());
                            addressModel.setCountryId(userModel.getCountryId().toString());
                            addressModel.setRelation("Self");
                            addressModel.setEmail(userModel.getEmail());
                            addressModel.setMobile(phone.split("\\.")[0]);
                            addressModel.setMobilePrefix(mprefix);
                            addressModel.setAddressType(0); // home

                            // check complete address model i think address will be same as recepient

                            String sellP = column.get("item-price");
                            int sellingPrice = 0;
                            if (!sellP.isEmpty()) {
                                // the item price is in USD so convert it in INR
                                BigDecimal d = CurrencyConversionUtil.getConvertedPrice(new BigDecimal(sellP),"USD","INR");
                                sellingPrice = d.intValue();
//                                if(quantity>0)
//                                    sellingPrice = sellingPrice/quantity; // as it has calculated amount
                            }

                            String shipP = column.get("shipping-price");
                            int shippingPrice = 0;
                            if (!shipP.isEmpty()) {
                                // the shipping price is in USD so convert it in INR
                                BigDecimal d = CurrencyConversionUtil.getConvertedPrice(new BigDecimal(shipP),"USD","INR");
                                shippingPrice = d.intValue();
//                                if(quantity>0)
//                                    sellingPrice = sellingPrice/quantity; // as it has calculated amount
                            }

                            String itemCode = column.get("sku").trim();


                            if (marketPlaceOrderUtil.handelProductOrNot(itemCode)) {
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("product-name"))
                                    .serviceDate(simpleDateFormat.format(todayDate))
                                    .serviceTypeId(String.valueOf(4))  // for delivery type fixed date
                                    .serviceCharge(new BigDecimal(shippingPrice))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            } else {
                                ProductModel productModel = null;
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("product-name"))
                                    .serviceDate("1970-01-01")
                                    .serviceTypeId(1 + "") // for delivery type standard
                                    .serviceCharge(new BigDecimal(shippingPrice))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                                productModelList.add(productModel);
                            }

                            String detail = relId + "(#)" +
                                new BigDecimal(column.get("order-item-id")).toPlainString()   + "(#)" +
                                column.get("purchase-date")+ "(#)" +
                                column.get("buyer-email") + "(#)" +
                                column.get("buyer-name") + "(#)" +
                                column.get("buyer-phone-number") + "(#)" +
                                column.get("sku") + "(#)" +
                                column.get("product-name") + "(#)" +
                                column.get("quantity-purchased") + "(#)" +
                                column.get("item-price") + "(#)" +
                                column.get("item-tax") + "(#)" +
                                column.get("shipping-price") + "(#)" +
                                column.get("recipient-name") + "(#)" +
                                column.get("ship-address-1") + "(#)" +
                                column.get("ship-address-2") + "(#)" +
                                column.get("ship-address-3") + "(#)" +
                                column.get("ship-city") + "(#)" +
                                column.get("ship-state") + "(#)" +
                                column.get("ship-postal-code") + "(#)" +
                                column.get("ship-country") + "(#)" +
                                column.get("ship-phone-number");

                            extraInfoModel.setMarketData(detail);

                        }

                    } else if(fk_associate_id==841){
                        //Interflora International
                        String relId = column.get("Order No");

                        if (!relId.isEmpty()) {
                            relId = new BigDecimal(column.get("Order No")).toPlainString();
                        }
                        extraInfoModel = new ExtraInfoModel.Builder()
                            .gstNo("")
                            .relId(relId)
                            .marketName("InterFloraInt")
                            .build();

                        if(rowNumValue!=2 && prevRlReqId.equalsIgnoreCase(extraInfoModel.getRelId()) ){
                            // when next row is old order and extra product
                            ValidationModel validationModelPrev = validationModelList.get(prevRowNum);
                            productModelList=validationModelPrev.getProductModelList();
                            ExtraInfoModel extraInfoModelPrev = validationModelPrev.getExtraInfoModel();

                            String quant = column.get("Qty");
                            int quantity = 0;
                            if (!quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }

                            String strDate1 = "";

                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                                Date strDate = formatter.parse(column.get("Delivery Date"));
                                strDate1 = simpleDateFormat.format(strDate);
                            } catch (ParseException ex) {
                                strDate1 = "";
                            }
                            String sP = column.get("Amount");
                            int sellingPrice = 0;
                            if (!sP.isEmpty()) {
                                double d = Double.parseDouble(sP);
                                sellingPrice = (int) d;
                            }

                            ProductModel productModel = null;
                            productModel = new ProductModel.Builder()
                                .productCode(column.get("Product Code").trim())
                                .quantity(quantity)
                                .sellingPrice(new BigDecimal(sellingPrice))
                                .serviceDate(strDate1)
                                .serviceTypeId(4 + "")
                                .serviceCharge(new BigDecimal(0))
                                .displayAttrList(new HashMap<>())
                                .perProductDiscount(new BigDecimal(0))
                                .giftBox(0)
                                .voucher(null)
                                .fkId(72)
                                .build();
                            productModelList.add(productModel);
                            StringBuilder marketPlaceData = new StringBuilder(extraInfoModelPrev.getMarketData());
                            marketPlaceData.append( "(#)" + column.get("Product") + "(#)"
                                + column.get("Qty") + "(#)" +
                                productModel.getProductCode() + "(#)" +
                                quantity + "(#)" +
                                sellingPrice);

                            extraInfoModelPrev.setMarketData(marketPlaceData.toString());

                            validationModelPrev.setExtraInfoModel(extraInfoModelPrev);
                            validationModelPrev.setProductModelList(productModelList);
                            validationModelList.set(prevRowNum,validationModelPrev);

                        }else {
                            // new order

                            String name = column.get("Sender Name").trim().replace("\\sNA\\s", "");
                            String fname = "";
                            String lname = "";

                            if (name != null) {

                                if (name.contains(" ")) {
                                    String[] nameArray = name.trim().split(" ");

                                    fname = nameArray[0];
                                    if (nameArray.length > 1) {

                                        for (int i = 1; i < nameArray.length - 1; i++) {
                                            if (fname.length() + nameArray[i].length() < 20) {
                                                fname += " " + nameArray[i];
                                            } else {
                                                break;
                                            }
                                        }
                                        lname = name.trim().substring(fname.length() + 1, name.trim().length());
                                    }
                                } else {
                                    fname = column.get("Sender Name");
                                }
                            }
                            String zipCode = column.get("Pincode");
                            if (!zipCode.isEmpty()) {
                                zipCode = zipCode.trim();
                                if (zipCode.length() >= 6) {
                                    zipCode = zipCode.substring(0, 6);
                                }
                            }

                            String address = column.get("Address").trim();
                            address = marketPlaceOrderUtil.replaceSpecialChars(address);

                            userModel = new UserModel.UserBuilder()
                                .id(null)
                                .firstname(fname)
                                .lastname(lname)
                                .addressField1(address + "," + column.get("City") + "," + column.get("State"))
                                .state(column.get("State"))
                                .city(column.get("City"))
                                .postcode(zipCode)
                                .email("interflora@indiangiftsportal.com")
                                .mobile("123456789")
                                .mobilePrefix("91")
                                .password(millis + "")
                                .countryId(99)
                                .associateId(5)
                                .uniqsrc("Bulk-" + millis)
                                .build();

                            name = column.get("Deliver To");
                            if (name != null) {

                                if (name.contains(" ")) {
                                    String[] nameArray = name.trim().split(" ");
                                    fname = nameArray[0];
                                    if (nameArray.length > 1) {
                                        lname = name.trim().substring(fname.length() + 1, name.trim().length());

                                    } else {
                                        fname = column.get("Deliver To");
                                    }
                                } else {
                                    fname = column.get("Deliver To");
                                }
                            }
                            int countryId = marketPlaceOrderUtil.getCountryId(column.get("Country"));
                            String mprefix = marketPlaceOrderUtil.getMobilePrefixByCountryId(countryId);

                            String phone = column.get("Mobile");

                            if (!phone.isEmpty()) {
                                phone = new BigDecimal(column.get("Mobile")).toPlainString();
                            }
                            addressModel.setTitle("");
                            addressModel.setFirstname(fname);
                            addressModel.setLastname(lname);
                            addressModel.setEmail("");
                            addressModel.setStreetAddress(userModel.getAddressField1());
                            addressModel.setPostcode(userModel.getPostcode());
                            addressModel.setCity(userModel.getCity());
                            addressModel.setState(userModel.getState());
                            addressModel.setCountryId(countryId + "");
                            addressModel.setMobile(phone);
                            addressModel.setMobilePrefix(mprefix);
                            addressModel.setAddressType(0); // home

                            deliveryMessageModel.setMessage(column.get("Gift Msg").trim().equalsIgnoreCase("na") ? "" : column.get("Gift Msg").trim());

                            String sP = column.get("Amount");
                            int sellingPrice = 0;
                            if (!sP.isEmpty()) {
                                double d = Double.parseDouble(sP);
                                sellingPrice = (int) d;
                            }

                            String quant = column.get("Qty");
                            int quantity = 0;
                            if (!quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }

                            String strDate1 = "";

                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                                Date strDate = formatter.parse(column.get("Delivery Date"));
                                strDate1 = simpleDateFormat.format(strDate);
                            } catch (ParseException ex) {
                                strDate1 = "";
                            }

                            ProductModel productModel = null;
                            productModel = new ProductModel.Builder()
                                .productCode(column.get("Product Code").trim())
                                .quantity(quantity)
                                .sellingPrice(new BigDecimal(sellingPrice))
                                .serviceDate(strDate1)
                                .serviceTypeId(4 + "")
                                .serviceCharge(new BigDecimal(0))
                                .displayAttrList(new HashMap<>())
                                .perProductDiscount(new BigDecimal(0))
                                .giftBox(0)
                                .voucher(null)
                                .fkId(72)
                                .build();
                            productModelList.add(productModel);



                            String detail = relId + "(#)" +
                                column.get("Order Date") + "(#)" +
                                column.get("Delivery Date") + "(#)" +
                                column.get("Product") + "(#)" +
                                column.get("Qty") + "(#)" +
                                column.get("Sender Name") + "(#)" +
                                userModel.getMobile() + "(#)" +
                                userModel.getEmail() + "(#)" +
                                userModel.getAddressField1() + "(#)" +
                                userModel.getPostcode() + "(#)" +
                                productModel.getProductCode() + "(#)" +
                                quantity + "(#)" +
                                sellingPrice + "(#)" +
                                column.get("GST") + "(#)" +
                                column.get("Country") + "(#)" +
                                column.get("Deliver To") + "(#)" +
                                addressModel.getMobile() + "(#)" +
                                column.get("Gift Msg") + "(#)" +
                                column.get("Addons Detail");


                            extraInfoModel.setMarketData(detail);

                        }

                    }else if(fk_associate_id==556){
                        // corporate
                        extraInfoModel = new ExtraInfoModel.Builder()
                            .gstNo(column.get("GST").split("\\.")[0])
                            .relId(column.get("Order No").split("\\.")[0])
                            .marketName("Corporate")
                            .build();


                        if(rowNumValue!=2 && prevRlReqId.equalsIgnoreCase(extraInfoModel.getRelId()) ){
                            // when next row is old order and extra product
                            ValidationModel validationModelPrev = validationModelList.get(prevRowNum);
                            productModelList=validationModelPrev.getProductModelList();
                            ExtraInfoModel extraInfoModelPrev = validationModelPrev.getExtraInfoModel();

                            String sP = column.get("Amount");
                            int sellingPrice = 0;
                            if (!sP.isEmpty()) {
                                double d = Double.parseDouble(sP);
                                sellingPrice = (int) d;
                            }
                            String quant = column.get("QTY");
                            int quantity = 0;
                            if (quant!=null && !quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }

                            String strDate1 = "";

                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                                Date strDate = formatter.parse(column.get("Delivery Date"));
                                strDate1 = simpleDateFormat.format(strDate);
                            } catch (ParseException ex) {
                                strDate1 = "";
                            }

                            String itemCode = column.get("Product Code").trim();
                            ProductModel productModel = null;
                            if (marketPlaceOrderUtil.handelProductOrNot(itemCode)) {
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate(strDate1)
                                    .serviceTypeId(String.valueOf(4))
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                            } else {
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate(strDate1)
                                    .serviceTypeId(1 + "")
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                            }
                            productModelList.add(productModel);

                            StringBuilder marketPlaceData = new StringBuilder(extraInfoModelPrev.getMarketData());
                            marketPlaceData.append("(#)" +
                                column.get("Product") + "(#)" +
                                quantity + "(#)" +
                                productModel.getProductCode() + "(#)" +
                                sellingPrice + "(#)" );

                            extraInfoModelPrev.setMarketData(marketPlaceData.toString());

                            validationModelPrev.setExtraInfoModel(extraInfoModelPrev);
                            validationModelPrev.setProductModelList(productModelList);
                            validationModelList.set(prevRowNum,validationModelPrev);

                        }else {
                            // it's a new order id

                            String name = column.get("Sender Name").trim().replaceAll("\\sNA\\s", "");
                            String fname = "";
                            String lname = "";

                            if (name != null) {

                                if (name.contains(" ")) {
                                    String[] nameArray = name.trim().split(" ");

                                    fname = nameArray[0];
                                    if (nameArray.length > 1) {

                                        for (int i = 1; i < nameArray.length - 1; i++) {
                                            if (fname.length() + nameArray[i].length() < 20) {
                                                fname += " " + nameArray[i];
                                            } else {
                                                break;
                                            }
                                        }
                                        lname = name.trim().substring(fname.length() + 1, name.trim().length());
                                    }
                                } else {
                                    fname = column.get("Sender Name");
                                }
                            }
                            String zipCode = column.get("Pincode");
                            if (!zipCode.isEmpty()) {
                                zipCode = zipCode.trim();
                                zipCode = zipCode.split("\\.")[0];
                            }

                            String address = column.get("Address").trim();
                            address = marketPlaceOrderUtil.replaceSpecialChars(address);

                            String phone = column.get("Sender Mobile").trim();

                            if (!phone.isEmpty()) {
                                phone = new BigDecimal(phone).toPlainString();
                            }
                            userModel = new UserModel.UserBuilder()
                                .id(null)
                                .firstname(fname)
                                .lastname(lname)
                                .addressField1(address + "," + column.get("City") + "," + column.get("State"))
                                .state(column.get("State"))
                                .city(column.get("City"))
                                .postcode(zipCode)
                                .email(column.get("Sender Email").trim())
                                .mobile(phone.split("\\.")[0])
                                .mobilePrefix("91")
                                .password(millis + "")
                                .countryId(99)
                                .associateId(5)
                                .uniqsrc("Bulk-" + millis)
                                .build();

                            name = column.get("Deliver To");
                            if (name != null) {

                                if (name.contains(" ")) {
                                    String[] nameArray = name.trim().split(" ");
                                    fname = nameArray[0];
                                    if (nameArray.length > 1) {
                                        lname = name.trim().substring(fname.length() + 1, name.trim().length());

                                    } else {
                                        fname = column.get("Deliver To");
                                    }
                                } else {
                                    fname = column.get("Deliver To");
                                }
                            }
                            int countryId = marketPlaceOrderUtil.getCountryId(column.get("Country").trim());
                            String mprefix = marketPlaceOrderUtil.getMobilePrefixByCountryId(countryId);

                            phone = column.get("Mobile").trim();

                            if (!phone.isEmpty()) {
                                phone = new BigDecimal(column.get("Mobile")).toPlainString();
                            }
                            addressModel.setTitle("");
                            addressModel.setFirstname(fname);
                            addressModel.setLastname(lname);
                            addressModel.setStreetAddress(userModel.getAddressField1());
                            addressModel.setEmail("");
                            addressModel.setPostcode(userModel.getPostcode());
                            addressModel.setCity(userModel.getCity());
                            addressModel.setState(userModel.getState());
                            addressModel.setCountryId(countryId + "");
                            addressModel.setMobile(phone.split("\\.")[0]);
                            addressModel.setMobilePrefix(mprefix);
                            addressModel.setAddressType(0); // home

                            deliveryMessageModel.setMessage(column.get("Gift Msg").trim().equalsIgnoreCase("na") ? "" : column.get("Gift Msg").trim());

                            String sP = column.get("Amount");
                            int sellingPrice = 0;
                            if (!sP.isEmpty()) {
                                double d = Double.parseDouble(sP);
                                sellingPrice = (int) d;
                            }

                            String quant = column.get("Qty");
                            int quantity = 0;
                            if (!quant.isEmpty()) {
                                double d = Double.parseDouble(quant);
                                quantity = (int) d;
                            }
                            String strDate1 = "";

                            try {
                                SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy");
                                Date strDate = formatter.parse(column.get("Delivery Date"));
                                strDate1 = simpleDateFormat.format(strDate);
                            } catch (ParseException ex) {
                                strDate1 = "";
                            }

                            String itemCode = column.get("Product Code").trim();
                            ProductModel productModel = null;
                            if (marketPlaceOrderUtil.handelProductOrNot(itemCode)) {
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate(strDate1)
                                    .serviceTypeId(String.valueOf(4))
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                            } else {
                                productModel = new ProductModel.Builder()
                                    .productCode(itemCode)
                                    .quantity(quantity)
                                    .sellingPrice(new BigDecimal(sellingPrice))
                                    .name(column.get("ProductName"))
                                    .serviceDate(strDate1)
                                    .serviceTypeId(1 + "")
                                    .serviceCharge(new BigDecimal(0))
                                    .displayAttrList(new HashMap<>())
                                    .perProductDiscount(new BigDecimal(0))
                                    .giftBox(0)
                                    .voucher(null)
                                    .fkId(0)
                                    .build();
                            }
                            productModelList.add(productModel);

                            String detail = column.get("Order No").split("\\.")[0] + "(#)" +
                                column.get("Order Date") + "(#)" +
                                column.get("Delivery Date") + "(#)" +
                                column.get("Product") + "(#)" +
                                column.get("Qty") + "(#)" +
                                column.get("Sender Name") + "(#)" +
                                phone + "(#)" +
                                userModel.getEmail() + "(#)" +
                                userModel.getAddressField1() + "(#)" +
                                userModel.getPostcode() + "(#)" +
                                productModel.getProductCode() + "(#)" +
                                quantity + "(#)" +
                                sellingPrice + "(#)" +
                                column.get("GST") + "(#)" +
                                column.get("Country") + "(#)" +
                                column.get("Deliver To") + "(#)" +
                                addressModel.getMobile() + "(#)" +
                                column.get("Gift Msg") + "(#)" +
                                column.get("Addons Detail");

                            extraInfoModel.setMarketData(detail);


                        }
                    }else {
                        validationModel.setError(Boolean.TRUE);
                        validationModel.setMessage("Vendor Doesn't Match.");
                        userModel = new UserModel.UserBuilder()
                            .build();
                    }

                    /* populate validation model */
                    if(rowNumValue!=2 && prevRlReqId.equalsIgnoreCase(extraInfoModel.getRelId()) ){
                        // don't add the validation model to list
                    }else {
                        validationModel.setUserModel(userModel);
                        validationModel.setAddressModel(addressModel);
                        validationModel.setProductModelList(productModelList);
                        validationModel.setExtraInfoModel(extraInfoModel);
                        validationModel.setDeliveryMessageModel(deliveryMessageModel);
                        prevRlReqId = extraInfoModel.getRelId();

                        //          logger.debug("validation model : " + rowNumValue);
                        //          logger.debug("values : "+ validationModel.toString());

                        validationModelList.add(validationModel);
                        prevRowNum = validationModelList.size()-1;

                    }


                } catch (Exception e) {
                    logger.debug("exception at row : " + rowNumValue);
                    prevRowNum = validationModelList.size()-1;
                    ValidationModel validationModel = new ValidationModel();
                    validationModel.setError(true);
                    validationModel.setMessage("Details Inappropriate.");
                    validationModelList.add(validationModel);
                    logger.error("Exception at filling model after reading excel : ", e);
                }
            }
        }

        return validationModelList;
    }


    public MarketPlaceFinalOrderResponseModel validateDataForMarketPlace(List<ValidationModel> validationModelList) {

        MarketPlaceOrderUtil marketPlaceOrderUtil = new MarketPlaceOrderUtil();
        List<ValidationModel> validationModelList1 = validationModelList;
        MarketPlaceFinalOrderResponseModel marketPlaceFinalOrderResponseModel = new MarketPlaceFinalOrderResponseModel();
        List<ErrorModel> errorModelList = new ArrayList<>();
        Integer orderId = 0;
        MarketPlaceTempOrderModel marketPlaceTempOrderModel = new MarketPlaceTempOrderModel();
        ExtraInfoModel extraInfoModel;
        int fail = 0, correct = 0;
        int rowNumCount = 1;
        int listSize = validationModelList.size();
        logger.debug("listSize : "+listSize);

        for(int i=0; i<listSize; i++){

            CountModel countModel = new CountModel();
            ValidationModel validationModel = new ValidationModel();
            try {
                validationModel = validationModelList1.get(i);

                logger.debug("value of i : "+ i);
                logger.debug("row number : "+ validationModel.getRowNum());
                //     logger.debug("row values : "+ validationModel.toString());

                if(validationModel.getError() == false && validationModel!=null) {
                    AddressModel addressModel = validationModel.getAddressModel();
                    extraInfoModel = validationModel.getExtraInfoModel();
                    List<ProductModel> productModelList = validationModel.getProductModelList();
                    // check if order already exists.
                    validationModel = marketPlaceOrderUtil.checkIfCorpOrderExists(validationModel);
                    if (validationModel.getError() == false) {
                        // order doesn't exist, so create a new order.

                        // first step : validate customer details.
                        validationModel = marketPlaceOrderUtil.validateCustomerDetails(validationModel);
                        if (validationModel.getError() == Boolean.TRUE) {
                            // validationModel.setMessage("Error is Customer Details.");
                        } else {
                            // no error in getting customer model.
                            addressModel.setId(validationModel.getUserModel().getIdHash());
                            validationModel.setAddressModel(addressModel);
                            // second step : validate address details.

                            validationModel = marketPlaceOrderUtil.validateSelectedAddress(validationModel);
                            if (validationModel.getError() == Boolean.TRUE) {
                                // validationModel.setMessage("Error is Address Validation.");
                            } else {
                                // third step : check product details.
                                int noOfProducts = productModelList.size();
                                logger.debug("noOfProducts : "+noOfProducts);

                                for(int j=0; j<noOfProducts; j++) {
                                    ProductModel productModel = productModelList.get(j);
                                    String prodCode = productModel.getProductCode();
                                    if (prodCode == "" || prodCode == null || productModel.getQuantity() <= 0) {
                                        // product details incomplete.
                                        validationModel.setError(Boolean.TRUE);
                                        if(productModel.getQuantity() <= 0)
                                            validationModel.setMessage("Quantity can't be less than 1.");
                                        else
                                            validationModel.setMessage("Product is not available.");
                                        if(j==0 || validationModel.getErrorAt().contains("-1"))
                                            validationModel.setErrorAt(j+"");
                                        else
                                            validationModel.setErrorAt(validationModel.getErrorAt()+","+j);

                                    } else {
                                        // details are complete so now fetch the product details or if product is not available
                                        // throw error
                                        ProductModel productModel1 = marketPlaceOrderUtil.validateAndGetProductDetails(productModel);
                                        if(productModel1==null){
                                            // product not found
                                            validationModel.setError(true);
                                            validationModel.setMessage("Product is not available.");
                                            validationModel.setErrorAt(validationModel.getErrorAt()+","+j);
                                        }else {
                                            productModelList.set(j,productModel1);
                                        }

                                    }
                                }
                                validationModel.setProductModelList(productModelList);

                                if (validationModel.getError() == Boolean.TRUE) {
                                    // product details got error so lets not proceed the order creation.
                                } else {
                                    // fourth step : validate extra info values and add in the model
                                    if (extraInfoModel.getGstNo() != null &&
                                        (extraInfoModel.getMarketData() != null || extraInfoModel.getMarketData() != "") &&
                                        (extraInfoModel.getMarketName() != null || extraInfoModel.getMarketName() != "") &&
                                        extraInfoModel.getRelId() != "") {
                                        // fifth step : all info is good so create temp model and create temp order.
                                        marketPlaceTempOrderModel = fillTempModelAndCreateTempOrder(validationModel);
                                        if (marketPlaceTempOrderModel.getTempOrderId() != 0) {
                                            // last step : create order by hitting api
                                            logger.debug("Temp Order Created successfully : " + marketPlaceTempOrderModel.getTempOrderId());
                                            validationModel = marketPlaceOrderUtil.checkIfCorpOrderExists(validationModel);
                                            if(validationModel.getError()==false){
                                                // order doesn't exist checked again
                                                orderId = marketPlaceOrderUtil.createOrder(marketPlaceTempOrderModel, extraInfoModel);
                                            }else {
                                                logger.debug("Tried to create duplicate order for same row.");
                                            }
                                            if (orderId == 0) {
                                                validationModel.setError(Boolean.TRUE);
                                                validationModel.setMessage("Error at order creation.");
                                                validationModel.setId(orderId);
                                            } else {
                                                validationModel.setError(Boolean.FALSE);
                                                validationModel.setMessage("Order has been Successfully Created.");
                                                validationModel.setId(orderId);
                                            }
                                        } else {
                                            logger.error("Error at temp order creation.");
                                            validationModel.setError(Boolean.TRUE);
                                            validationModel.setMessage("Error at temp order creation.");
                                        }

                                    } else {
                                        // extra info is incomplete.
                                        logger.error("Incomplete Extra Info.");
                                        validationModel.setError(Boolean.TRUE);
                                        validationModel.setMessage("Incomplete Extra Info.");
                                    }

                                }
                            }
                        }
                    }
                }

                if (validationModel.getError()==true) {

                    if(validationModel.getProductModelList().size()>1){
                        String[] arr = validationModel.getErrorAt().split(",");
                        Arrays.asList(arr);
                        if(validationModel.getErrorAt().equals("-1")){
                            for (int k = 0; k < validationModel.getProductModelList().size(); k++) {
                                ErrorModel errorModel = new ErrorModel();
                                errorModel.setRow(++rowNumCount);
                                errorModel.setMsg(validationModel.getMessage());
                                countModel.setFail(++fail);
                                countModel.setCorrect(correct);
                                errorModelList.add(errorModel);
                            }
                        }else {
                            for (int k = 0; k < validationModel.getProductModelList().size(); k++) {
                                if (Arrays.asList(arr).contains(String.valueOf(k))) {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setRow(++rowNumCount);
                                    errorModel.setMsg(validationModel.getMessage());
                                    countModel.setFail(++fail);
                                    countModel.setCorrect(correct);
                                    errorModelList.add(errorModel);
                                } else {
                                    ErrorModel errorModel = new ErrorModel();
                                    errorModel.setRow(++rowNumCount);
                                    errorModel.setMsg("No Error in this row.");
                                    countModel.setFail(fail);// don't count it in fail as no order generated
                                    countModel.setCorrect(correct);// don't count it in correct as no order generated
                                    errorModelList.add(errorModel);
                                }
                            }
                        }
                    }else {
                        ErrorModel errorModel = new ErrorModel();
                        errorModel.setRow(++rowNumCount);
                        errorModel.setMsg(validationModel.getMessage());
                        countModel.setFail(++fail);
                        countModel.setCorrect(correct);
                        errorModelList.add(errorModel);
                    }

                }
                else {
                    for (int k = 0; k < validationModel.getProductModelList().size(); k++) {
                        ++rowNumCount;
                        countModel.setCorrect(++correct);
                        countModel.setFail(fail);
                    }
                }
            }
            catch(Exception e){
                logger.error("Exception Caught at validation : ", e);
                ErrorModel errorModel = new ErrorModel();
                errorModel.setRow(++rowNumCount);
                errorModel.setMsg(validationModel.getMessage());
                countModel.setFail(++fail);
                errorModelList.add(errorModel);
            }
            // everything went well and lets add the model in the list.
            marketPlaceFinalOrderResponseModel.setError(errorModelList);
            marketPlaceFinalOrderResponseModel.setCount(countModel);
        }
        return marketPlaceFinalOrderResponseModel;
    }

    public MarketPlaceTempOrderModel fillTempModelAndCreateTempOrder(ValidationModel validationModel) {
        UserModel userModel = validationModel.getUserModel();
        Integer orderTempId=0;
        MarketPlaceTempOrderModel marketPlaceTempOrderModel = new MarketPlaceTempOrderModel();
        AddressModel addressModel = validationModel.getAddressModel();
        List<ProductModel> productModelList = validationModel.getProductModelList();
        MarketPlaceOrderUtil marketPlaceOrderUtil = new MarketPlaceOrderUtil();
        try {
            // Everything went well,fill the tempmodel.
            marketPlaceTempOrderModel.setAddressBookId(new Integer(addressModel.getAid()));
            marketPlaceTempOrderModel.setCustomerId(new Integer(userModel.getId()));
            marketPlaceTempOrderModel.setAssociateId(validationModel.getFkAssociateId());
            marketPlaceTempOrderModel.setShippingAddressModel(addressModel);
            marketPlaceTempOrderModel.setDeliveryInstr("  ");
            marketPlaceTempOrderModel.setDeliveryMessageModel(validationModel.getDeliveryMessageModel());
            marketPlaceTempOrderModel.setComment(validationModel.getDeliveryMessageModel().getMessage());
            marketPlaceTempOrderModel.setOccasionId(17);
            marketPlaceTempOrderModel.setDeliveryDate(productModelList.get(0).getServiceDate());
            marketPlaceTempOrderModel.setExtraValue("CartID:0");
            marketPlaceTempOrderModel.setRelation(addressModel.getRelation());
            marketPlaceTempOrderModel.setVoucher(productModelList.get(0).getVoucher());
            marketPlaceTempOrderModel.setDiscount(productModelList.get(0).getPerProductDiscount());
            marketPlaceTempOrderModel.setIdHash(userModel.getIdHash());
            logger.debug("TempOrder model : "+marketPlaceTempOrderModel.toString());

            orderTempId  = marketPlaceOrderUtil.createTempOrder(marketPlaceTempOrderModel, productModelList);

            logger.debug("Retruned from function createTempOrder "+orderTempId);

            if (orderTempId!=0) {
                logger.debug("TEMP-ORDER CREATED SUCCESSFULLY: " + orderTempId);
            }
        }
        catch (Exception e){
            logger.error("Exception in Filling Temp Order Details : ", e);
        }
        marketPlaceTempOrderModel.setTempOrderId(orderTempId);
        return marketPlaceTempOrderModel;
    }
    public String setSemaphore(String processName) {
        // ensures that only one process is chosen if there are multiple requests at single instance
        String result = processName;
        try {
            MarketPlaceOrder.semaphore = processName;

        } catch (Exception exception) {
            logger.error("Error at semaphore checking : "+ exception);
        }
        return result;
    }
}

