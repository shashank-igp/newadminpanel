package com.igp.admin.mappers.marketPlace;

import com.igp.admin.models.marketPlace.*;
import com.igp.admin.utils.marketPlace.MarketPlaceOrderUtil;
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
                put("artisanG",561);      //Artisan Gilt
                put("Inductus",562);    //Inductus
                put("FNP",741);         //Fnp International
                put("JnJ",769);           //Johnsons and Johnsons
                put("INFUK",841);       //Interflora International

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

    public  Map<Integer, Map<String, String>> parseExcelForMarketPlace(FormDataMultiPart multiPart, String filePrefix) {
        Map<Integer, Map<String, String>> data= new HashMap<>();
        int NUM_COLUMNS = 20;
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
                for (int j = 0; j < 20; j++) {
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
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            a.put(list.get(currCol), currentCell.getNumericCellValue() + "");
                            if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
                                a.put(list.get(currCol), currentCell.getDateCellValue() + "");
                            }
                        } else  if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                            a.put(list.get(currCol), currentCell.getRichStringCellValue() + "");
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                            a.put(list.get(currCol), currentCell.getBooleanCellValue() + "");
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                            a.put(list.get(currCol), "");
                            count++;
                        }
                    }
                    if (count >= 15) {
                        // row is empty.
                        logger.debug("row : " + row);
                        logger.debug("Empty Row.");
                    } else {
                        data.put(row, a);
                        logger.debug("row : " + row);
                        logger.debug("values : " + data.get(row));
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
                    //  while (rowNum.hasNext()) {
                    ValidationModel validationModel = new ValidationModel();
                    UserModel userModel;
                    AddressModel addressModel = new AddressModel();
                    ProductModel productModel = null;
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
                        String quant = column.get("QTY");
                        int quantity = 0;
                        if (!quant.isEmpty()) {
                            double d = Double.parseDouble(quant);
                            quantity = (int) d;
                        }

                        String name = " "+column.get("MemberName")+" ";
                        name=name.replaceAll("\\sNA\\s"," ").replaceAll("\\sna\\s"," ").replaceAll("\\sNa\\s"," ").trim();
                        String fname = "";
                        String lname = "";

                        if (name != null) {

                            if (name.contains(" ")) {
                                String[] nameArray = name.trim().split(" ");

                                fname = nameArray[0];
                                if (nameArray.length > 1) {

                                    for(int i=1;i<nameArray.length-1;i++){
                                        if(fname.length()+nameArray[i].length()<20){
                                            fname+=" "+nameArray[i];
                                        }else {
                                            break;
                                        }
                                    }
                                    lname = name.trim().substring(fname.length() + 1, name.trim().length());
                                }
                            } else {
                                fname = name;
                            }
                        }


                        String address = column.get("AddressLine1").trim().replace("\n"," ");
                        String address2 = column.get("AddressLine2").trim().replace("\n"," ");

                        userModel = new UserModel.UserBuilder()
                            .id(null)
                            .firstname(fname)
                            .lastname(lname)
                            .addressField1(address+","+address2+" " +column.get("City")+ "," + column.get("State"))
                            .state(column.get("State"))
                            .city(column.get("City"))
                            .postcode(zipCode)
                            .email(column.get("Email").trim().isEmpty()?"asif@tokenz.com":column.get("Email").trim())
                            .mobile(phone.split("\\.")[0])
                            .mobilePrefix(mprefix)
                            .password(millis + "")
                            .countryId(99)
                            .associateId(fk_associate_id)
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

                        String sP = column.get("SellingPrice");
                        int sellingPrice = 0;
                        if (!sP.isEmpty()) {
                            double d = Double.parseDouble(sP);
                            sellingPrice = (int) d;
                        }

                        String itemCode = column.get("Item Code").trim();
                        if(!itemCode.isEmpty()) {
                            String itemCodeArray[] = column.get("Item Code").trim().split("IGP-");
                            if(itemCodeArray.length==2) {
                                itemCode = itemCodeArray[1];
                            } else {
                                itemCode = itemCodeArray[0];
                            }
                            itemCode = marketPlaceOrderUtil.getProductIdForLoyaltyOnly(itemCode);
                        }
                        if(marketPlaceOrderUtil.handelProductOrNot(itemCode)){
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
                        }else {
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
                            sellingPrice + "(#)" +
                            column.get("ProductName");

                        extraInfoModel = new ExtraInfoModel.Builder()
                            .gstNo("")
                            .relId(column.get("PO Number"))
                            .marketData(detail)
                            .marketName("RL")
                            .build();

                    } else if(fk_associate_id==841){
                        //Interflora International

                        String name = column.get("Sender Name").trim().replace("\\sNA\\s","");;
                        String fname = "";
                        String lname = "";

                        if (name != null) {

                            if (name.contains(" ")) {
                                String[] nameArray = name.trim().split(" ");

                                fname = nameArray[0];
                                if (nameArray.length > 1) {

                                    for(int i=1;i<nameArray.length-1;i++){
                                        if(fname.length()+nameArray[i].length()<20){
                                            fname+=" "+nameArray[i];
                                        }else {
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

                        String address = column.get("Address").trim().replace("\n"," ").replace("–"," ");
                        userModel = new UserModel.UserBuilder()
                            .id(null)
                            .firstname(fname)
                            .lastname(lname)
                            .addressField1(address+","+column.get("City")+ "," + column.get("State"))
                            .state(column.get("State"))
                            .city(column.get("City"))
                            .postcode(zipCode)
                            .email("interflora@indiangiftsportal.com")
                            .mobile("123456789")
                            .mobilePrefix("91")
                            .password(millis + "")
                            .countryId(99)
                            .associateId(fk_associate_id)
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
                        addressModel.setCountryId(countryId+"");
                        addressModel.setMobile(phone);
                        addressModel.setMobilePrefix(mprefix);
                        addressModel.setAddressType(0); // home

                        deliveryMessageModel.setMessage(column.get("Gift Msg").trim().equalsIgnoreCase("na")?"":column.get("Gift Msg").trim());

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
                        }catch (ParseException ex) {
                            strDate1 = "";
                        }

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

                        String relId = column.get("Order No");

                        if (!relId.isEmpty()) {
                            relId = new BigDecimal(column.get("Order No")).toPlainString();
                        }

                        String detail = relId + "(#)" +
                            column.get("Sender Name") + "(#)" +
                            userModel.getMobile() + "(#)" +
                            userModel.getEmail() + "(#)" +
                            userModel.getAddressField1() + "(#)" +
                            userModel.getPostcode() + "(#)" +
                            productModel.getProductCode() + "(#)" +
                            quantity + "(#)" +
                            sellingPrice;

                        extraInfoModel = new ExtraInfoModel.Builder()
                            .gstNo("")
                            .relId(relId)
                            .marketData(detail)
                            .marketName("InterFloraInt")
                            .build();

                    }else if(fk_associate_id==556){
                        // corporate

                        String name = column.get("Sender Name").trim().replaceAll("\\sNA\\s","");
                        String fname = "";
                        String lname = "";

                        if (name != null) {

                            if (name.contains(" ")) {
                                String[] nameArray = name.trim().split(" ");

                                fname = nameArray[0];
                                if (nameArray.length > 1) {

                                    for(int i=1;i<nameArray.length-1;i++){
                                        if(fname.length()+nameArray[i].length()<20){
                                            fname+=" "+nameArray[i];
                                        }else {
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

                        String address = column.get("Address").trim().replace("\n"," ").replace("–"," ");
                        String phone = column.get("Sender Mobile").trim();

                        if (!phone.isEmpty()) {
                            phone = new BigDecimal(phone).toPlainString();
                        }
                        userModel = new UserModel.UserBuilder()
                            .id(null)
                            .firstname(fname)
                            .lastname(lname)
                            .addressField1(address+","+column.get("City")+ "," + column.get("State"))
                            .state(column.get("State"))
                            .city(column.get("City"))
                            .postcode(zipCode)
                            .email(column.get("Sender Email").trim())
                            .mobile(phone.split("\\.")[0])
                            .mobilePrefix("91")
                            .password(millis + "")
                            .countryId(99)
                            .associateId(fk_associate_id)
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
                        addressModel.setCountryId(countryId+"");
                        addressModel.setMobile(phone.split("\\.")[0]);
                        addressModel.setMobilePrefix(mprefix);
                        addressModel.setAddressType(0); // home

                        deliveryMessageModel.setMessage(column.get("Gift Msg").trim().equalsIgnoreCase("na")?"":column.get("Gift Msg").trim());

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
                        }catch (ParseException ex) {
                            strDate1 = "";
                        }

                        String itemCode = column.get("Product Code").trim();
                        if(marketPlaceOrderUtil.handelProductOrNot(itemCode)){
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
                        }else {
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


                        String detail = column.get("Order No").split("\\.")[0] + "(#)" +
                            column.get("Sender Name") + "(#)" +
                            phone + "(#)" +
                            userModel.getEmail() + "(#)" +
                            userModel.getAddressField1() + "(#)" +
                            userModel.getPostcode() + "(#)" +
                            productModel.getProductCode() + "(#)" +
                            quantity + "(#)" +
                            sellingPrice;

                        extraInfoModel = new ExtraInfoModel.Builder()
                            .gstNo(column.get("GST").split("\\.")[0])
                            .relId(column.get("Order No").split("\\.")[0])
                            .marketData(detail)
                            .marketName("Corporate")
                            .build();

                    }else {
                        validationModel.setError(Boolean.TRUE);
                        validationModel.setMessage("Vendor Doesn't Match.");
                        userModel = new UserModel.UserBuilder()
                            .build();
                    }

                    /* populate validation model */
                    validationModel.setUserModel(userModel);
                    validationModel.setAddressModel(addressModel);
                    validationModel.setProductModel(productModel);
                    validationModel.setExtraInfoModel(extraInfoModel);
                    validationModel.setDeliveryMessageModel(deliveryMessageModel);

                    logger.debug("validation model : " + rowNum);
                    logger.debug("values : ", validationModel);

                    validationModelList.add(validationModel);

                } catch (Exception e) {
                    logger.debug("exception at row : " + rowNum);
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
        List<MarketPlaceFinalOrderResponseModel> marketPlaceFinalOrderResponseModelList = new ArrayList<>();
        MarketPlaceFinalOrderResponseModel marketPlaceFinalOrderResponseModel = new MarketPlaceFinalOrderResponseModel();
        List<ErrorModel> errorModelList = new ArrayList<>();
        Integer orderId = 0;
        MarketPlaceTempOrderModel marketPlaceTempOrderModel = new MarketPlaceTempOrderModel();
        ExtraInfoModel extraInfoModel;
        ProductModel productModel = null;
        int i = 0, fail = 0, correct = 0;
        int listSize = validationModelList.size();
        logger.debug("listSize : "+listSize);

        while (listSize!=0) {

            ErrorModel errorModel = new ErrorModel();
            CountModel countModel = new CountModel();
            ValidationModel validationModel = new ValidationModel();
            try {
                validationModel = validationModelList1.get(i);
                i++;
                logger.debug("row number : "+ validationModel.getRowNum());
                logger.debug("row values : "+ validationModel.toString());

                if(validationModel.getError() == false && validationModel!=null) {
                    AddressModel addressModel = validationModel.getAddressModel();
                    extraInfoModel = validationModel.getExtraInfoModel();
                    productModel = validationModel.getProductModel();
                    // check if order already exists.
                    validationModel = marketPlaceOrderUtil.checkIfCorpOrderExists(validationModel);
                    if (validationModel.getError() == false) {
                        // order doesn't exist, so create a new order.

                        // validate customer details.
                        validationModel = marketPlaceOrderUtil.validateCustomerDetails(validationModel);
                        if (validationModel.getError() == Boolean.TRUE) {
                            // validationModel.setMessage("Error is Customer Details.");
                        } else {
                            // no error in getting customer model.
                            addressModel.setId(validationModel.getUserModel().getIdHash());
                            validationModel.setAddressModel(addressModel);
                            // validate address details.

                            //  addressModel.setAid("1614158");
                            //   validationModel.setAddressModel(addressModel);

                            validationModel = marketPlaceOrderUtil.validateSelectedAddress(validationModel);
                            if (validationModel.getError() == Boolean.TRUE) {
                                // validationModel.setMessage("Error is Address Validation.");
                            } else {
                                // check product details.
                                String prodCode = productModel.getProductCode();
                                Integer prodQty = productModel.getQuantity();
                                if (prodCode == "" || prodCode == null || prodQty <= 0) {
                                    // product details incomplete.
                                    logger.error("Incomplete/Invalid Product Details.");
                                    validationModel.setError(Boolean.TRUE);
                                    if(prodCode == "" || prodCode == null){
                                        validationModel.setMessage("Incomplete/Invalid Product Details.");
                                    }else if(prodQty <= 0){
                                        validationModel.setMessage("Product Quantity is zero. ");
                                    }
                                } else {
                                    // product details are not empty so bring product details to the model.
                                    validationModel = marketPlaceOrderUtil.validateAndGetProductDetails(validationModel);
                                    productModel = validationModel.getProductModel();
                                    if (validationModel.getError() == Boolean.TRUE || productModel.getId()==null) {
                                        validationModel.setError(Boolean.TRUE);
                                        validationModel.setMessage("Product Can't be found.");
                                    } else {
                                        // finally validate extra info values and add in the model
                                        if (extraInfoModel.getGstNo() != null || extraInfoModel.getGstNo() != "" ||
                                            extraInfoModel.getMarketData() != null || extraInfoModel.getMarketData() != "" ||
                                            extraInfoModel.getMarketName() != null || extraInfoModel.getMarketName() != "" ||
                                            extraInfoModel.getRelId() != "") {
                                            // all info is good so create temp model and create temp order.
                                            marketPlaceTempOrderModel = fillTempModelAndCreateTempOrder(validationModel);
                                            if (marketPlaceTempOrderModel.getTempOrderId() != 0) {
                                                // create order by hitting api
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
                }
                if (validationModel.getError()==true) {
                    errorModel.setRow(validationModel.getRowNum());
                    errorModel.setMsg(validationModel.getMessage());
                    countModel.setFail(++fail);
                    countModel.setCorrect(correct);
                    errorModelList.add(errorModel);

                }
                else {
                    countModel.setCorrect(++correct);
                    countModel.setFail(fail);
                }

                // everything went well and lets add the model in the list.
            }
            catch(Exception e){
                logger.error("Exception Caught at validation : ", e);
                errorModel.setRow(validationModel.getRowNum());
                errorModel.setMsg(validationModel.getMessage());
                countModel.setFail(++fail);
                errorModelList.add(errorModel);
            }
            listSize--;
            validationModelList1.add(validationModel);
            marketPlaceFinalOrderResponseModel.setError(errorModelList);
            marketPlaceFinalOrderResponseModel.setCount(countModel);
            marketPlaceFinalOrderResponseModelList.add(marketPlaceFinalOrderResponseModel);
        }

        return marketPlaceFinalOrderResponseModel;
    }

    public MarketPlaceTempOrderModel fillTempModelAndCreateTempOrder(ValidationModel validationModel) {
        UserModel userModel = validationModel.getUserModel();
        Integer orderTempId=0;
        MarketPlaceTempOrderModel marketPlaceTempOrderModel = new MarketPlaceTempOrderModel();
        AddressModel addressModel = validationModel.getAddressModel();
        ProductModel productModel = validationModel.getProductModel();
        MarketPlaceOrderUtil marketPlaceOrderUtil = new MarketPlaceOrderUtil();
        try {
            // Everything went well,fill the tempmodel.
            marketPlaceTempOrderModel.setAddressBookId(new Integer(addressModel.getAid()));
            logger.debug("TEMP-ORDER DEBUGGING : " + "setAddressBookId " +addressModel.getAid());
            marketPlaceTempOrderModel.setCustomerId(new Integer(userModel.getId()));
            logger.debug("TEMP-ORDER DEBUGGING : " + "setCustomerId "+userModel.getId());
            marketPlaceTempOrderModel.setAssociateId(validationModel.getFkAssociateId());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setAssociateId "+validationModel.getFkAssociateId());

            marketPlaceTempOrderModel.setShippingAddressModel(addressModel);
            logger.debug("TEMP-ORDER DEBUGGING : " + "setShippingAddressModel");

            marketPlaceTempOrderModel.setDeliveryInstr("  ");
            logger.debug("TEMP-ORDER DEBUGGING : " + "setDeliveryInstr"+"   ");

            marketPlaceTempOrderModel.setDeliveryMessageModel(validationModel.getDeliveryMessageModel()); // change it later
            logger.debug("TEMP-ORDER DEBUGGING : " + validationModel.getDeliveryMessageModel().toString());

            marketPlaceTempOrderModel.setComment(validationModel.getDeliveryMessageModel().getMessage());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setComment"+validationModel.getDeliveryMessageModel().getMessage());

            marketPlaceTempOrderModel.setOccasionId(17);
            logger.debug("TEMP-ORDER DEBUGGING : " + "setOccassionId 17");

            marketPlaceTempOrderModel.setDeliveryDate(productModel.getServiceDate());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setDeliveryDate "+productModel.getServiceDate());

            marketPlaceTempOrderModel.setExtraValue("CartID:0");
            logger.debug("TEMP-ORDER DEBUGGING : " + "setExtraValue "+"CartID:0");

            marketPlaceTempOrderModel.setRelation(addressModel.getRelation());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setRelation "+addressModel.getRelation());

            marketPlaceTempOrderModel.setVoucher(productModel.getVoucher());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setVoucher "+productModel.getVoucher());

            marketPlaceTempOrderModel.setDiscount(productModel.getPerProductDiscount()); // change it later
            logger.debug("TEMP-ORDER DEBUGGING : " + "setDiscount "+productModel.getPerProductDiscount());

            marketPlaceTempOrderModel.setIdHash(userModel.getIdHash());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setIdHash "+userModel.getIdHash());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setSellingPrice "+productModel.getSellingPrice());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setQuantity "+productModel.getQuantity());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setProdId "+productModel.getId());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setServiceCharge "+productModel.getServiceCharge());
            logger.debug("TEMP-ORDER DEBUGGING : " + "setGiftBox "+productModel.getGiftBox());

            orderTempId  = marketPlaceOrderUtil.createTempOrder(marketPlaceTempOrderModel, productModel);

            logger.debug("Retruned from function DEBUGGING : " + "createTempOrder "+orderTempId);

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
}
