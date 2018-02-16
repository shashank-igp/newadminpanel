package com.igp.admin.mappers.marketPlace;

import com.igp.admin.models.marketPlace.*;
import com.igp.admin.utils.marketPlace.MarketPlaceOrderUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by suditi on 15/1/18.
 */
public class MarketPlaceMapper {
    private static final Logger logger = LoggerFactory.getLogger(MarketPlaceMapper.class);

    public  Map<Integer, Map<String, String>> parseExcelForMarketPlace(FormDataMultiPart multiPart, String filePrefix) {
        Map<Integer, Map<String, String>> data= new HashMap<>();
        int NUM_COLUMNS = 20;
        MarketPlaceOrderUtil marketPlaceOrderUtil = new MarketPlaceOrderUtil();

        try {
            FileUploadModel fileUploadModel = marketPlaceOrderUtil.uploadTheFile(multiPart, filePrefix);
            // create new file and parse it

            if (fileUploadModel.getError() == false) {
                File file = fileUploadModel.getFile();
                POIFSFileSystem excelFile = new POIFSFileSystem(file);
                Workbook workbook = new HSSFWorkbook(excelFile);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = datatypeSheet.iterator();
                int row = 0;
                // Assuming "column headers" are in the first row
                Row header_row = datatypeSheet.getRow(0);
                // Assuming 4 columns
                List<String> list = new ArrayList<>();
                for (int j = 0; j < 20; j++) {
                    Cell header_cell = header_row.getCell(j);
                    list.add(header_cell.getStringCellValue());
                    // filled list with column names.
                }
                // now we store all the values start from next row
                rowIterator.next();
                while (rowIterator.hasNext()) {
                    Map<String, String> a = new HashMap<>();
                    row++;
// check if we get second row since first has column headers
                    Row currentRow = rowIterator.next();
                    for (int currCol = 0; currCol < NUM_COLUMNS; currCol++) {
                        Cell currentCell = currentRow.getCell(currCol);

                        if (currentCell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            a.put(list.get(currCol), currentCell.getNumericCellValue() + "");
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_STRING) {
                            a.put(list.get(currCol), currentCell.getRichStringCellValue() + "");
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                            a.put(list.get(currCol), currentCell.getBooleanCellValue() + "");
                        } else if (currentCell.getCellType() == Cell.CELL_TYPE_BLANK) {
                            a.put(list.get(currCol), null);
                        }

                    }
                    data.put(row, a);
                    logger.debug("row : "+row);
                    logger.debug("values : "+data.get(row));
                }
            }
        } catch(FileNotFoundException e){
            logger.error("Exception at populating Map from file : ", e);
            e.printStackTrace();

        } catch(IOException e){
            e.printStackTrace();
            logger.error("Exception at populating Map from file : ", e);

        }

        return data;

    }

    public List<ValidationModel> refineDataAndPopulateModels( Map<Integer, Map<String, String>> data,String user, Integer fk_associate_id) {
        List<ValidationModel> validationModelList = new ArrayList<>();
        Map<Integer, String> serviceType = new HashMap<Integer, String>() {

            {
                put(1, Constants.getSTANDARD());
                put(2, Constants.getFIXED());
                put(3, Constants.getMIDNIGHT());
                put(4, Constants.getSameDay());
            }

        };



        for (Map.Entry<Integer, Map<String, String>> entry : data.entrySet()) {
            try {
                // revisiting each map i.e. each row
                Map<String, String> row = entry.getValue();
                Iterator<Map.Entry<String, String>> rowNum = row.entrySet().iterator();
                //  while (rowNum.hasNext()) {
                ValidationModel validationModel = new ValidationModel();
                UserModel userModel;
                AddressModel addressModel = new AddressModel();
                ProductModel productModel = null;
                ExtraInfoModel extraInfoModel = null;
                // visiting each column
                Map<String, String> column = row;

                // fill few generic values.
                String mprefix = MarketPlaceOrderUtil.getMobilePrefixByCountryId("99");
                validationModel.setFkAssociateId(fk_associate_id);
                validationModel.setError(Boolean.FALSE);
                validationModel.setId(0);
                long millis = System.currentTimeMillis();
                String phone =  new BigDecimal(column.get("Contact No")).toPlainString();
                String zipCode = column.get("Zip");
                String zip = zipCode.substring(0,6);
                String quant = column.get("QTY");
                String qty = quant.substring(0,1);


                // take out all the values and fill the models.
                // i.e. customer,address,product,extra_info
                if(user.equals("loyalty")) {

                    String name = column.get("MemberName");
                    String fname = "";
                    String lname = "";

                    if (name!=null) {

                        if (name.contains(" ")) {
                            String[] nameArray = name.trim().split(" ");
                            fname = nameArray[0];
                            if (nameArray.length > 1) {
                                lname = name.trim().substring(fname.length() + 1, name.trim().length());

                            } else {
                                fname = column.get("MemberName");
                            }
                        } else {
                            fname = column.get("MemberName");
                        }
                    }


                    userModel = new UserModel.UserBuilder()
                        .id(null)
                        .firstname(fname)
                        .lastname(lname)
                        .addressField1(column.get("AddressLine1"))
                        .addressField2(column.get("AddressLine2"))
                        .state(column.get("State"))
                        .city(column.get("City"))
                        .postcode(zip)
                        .email(column.get("Email"))
                        .mobile(phone)
                        .mobilePrefix(mprefix)
                        .password(millis+"")
                        .countryId(99)
                        .associateId(fk_associate_id)
                        .uniqsrc("Bulk-"+ millis)
                        .build();


                    addressModel.setTitle("");
                    addressModel.setFirstname(userModel.getFirstname());
                    addressModel.setLastname(userModel.getLastname());
                    addressModel.setStreetAddress(userModel.getAddressField1());
                    addressModel.setStreetAddress2(userModel.getAddressField2());
                    addressModel.setPostcode(userModel.getPostcode());
                    addressModel.setCity(userModel.getCity());
                    addressModel.setState(userModel.getState());
                    addressModel.setCountryId(userModel.getCountryId().toString());
                    addressModel.setRelation("Self");
                    addressModel.setEmail(userModel.getEmail());
                    addressModel.setMobile(userModel.getMobile());
                    addressModel.setMobilePrefix(mprefix);
                    addressModel.setAddressType(0); // home


                    productModel = new ProductModel.Builder()
                        .productCode(column.get("Item Code"))
                        .quantity(new Integer(qty))
                        .sellingPrice(new BigDecimal(column.get("SellingPrice")))
                        .name(column.get("ProductName"))
                        .serviceDate("1970-01-01")
                        .serviceTypeId(1+"")
                        .serviceType(serviceType.get(1))
                        .serviceCharge(new BigDecimal(0))
                        .displayAttrList(new HashMap<>())
                        .perProductDiscount(new BigDecimal(0))
                        .giftBox(0)
                        .voucher(null)
                        .build();


                    String detail = column.get("PO Number") + "(#)" +
                        column.get("MemberName")  + "(#)" +
                        column.get("Contact No"  + "(#)" +
                            column.get("Email"))  + "(#)" +
                        column.get("AddressLine1") + "(#)" +
                        column.get("AddressLine2") + "(#)" +
                        column.get("State") + "(#)" +
                        column.get("City") + "(#)" +
                        column.get("Zip") + "(#)" +
                        column.get("Item Code") + "(#)" +
                        column.get("QTY") + "(#)" +
                        column.get("SellingPrice") + "(#)" +
                        column.get("ProductName");

                    extraInfoModel = new ExtraInfoModel.Builder()
                        .gstNo("")
                        .relId(column.get("PO Number"))
                        .marketData(detail)
                        .marketName("RL")
                        .build();

                }else{

                    userModel = new UserModel.UserBuilder()
                        .id(null)
                        .firstname("suditi")
                        .lastname("choudhary")
                        .email("a131@gma.com")
                        .mobile("8223255954")
                        .mobilePrefix(mprefix)
                        .password(millis+"")
                        .countryId(99)
                        .associateId(fk_associate_id)
                        .uniqsrc("Bulk-"+ millis)
                        .build();


                    addressModel.setTitle("f");
                    addressModel.setFirstname("sud");
                    addressModel.setLastname("chou");
                    addressModel.setStreetAddress("desd");
                    addressModel.setPostcode("400072");
                    addressModel.setCity("mumbai");
                    addressModel.setState("mha");
                    addressModel.setCountryId("99");
                    addressModel.setRelation("self");
                    addressModel.setEmail("ssx@dfcs");
                    addressModel.setMobile("9413223688");
                    addressModel.setMobilePrefix(mprefix);
                    addressModel.setAddressType(new Integer(7));


                    productModel = new ProductModel.Builder()
                        .productCode("HD1046865")
                        .quantity(new Integer(2))
                        .displayPrice(new Integer(100))
                        .serviceDate("1970-01-01")
                        .serviceTypeId(1+"")
                        .serviceType(serviceType.get(1))
                        .displayAttrList(new HashMap<>())
                        .perProductDiscount(new BigDecimal(0))
                        .giftBox(0)
                        .voucher(null)
                        .build();


                    extraInfoModel = new ExtraInfoModel.Builder()
                        .gstNo("gffv")
                        .relId("xyz")
                        .marketData("")
                        .marketName("")
                        .build();
                }

                    /*// populate validation model*/
                validationModel.setUserModel(userModel);
                validationModel.setAddressModel(addressModel);
                validationModel.setProductModel(productModel);
                validationModel.setExtraInfoModel(extraInfoModel);
                validationModel.setError(false);


                validationModelList.add(validationModel);

            }catch (Exception e){
                ValidationModel validationModel = new ValidationModel();
                validationModel.setError(false);
                validationModel.setMessage("Details Inappropriate.");
                validationModelList.add(validationModel);
                logger.error("Exception at filling model after reading excel : ", e);
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

        while (listSize!=0) {

            ErrorModel errorModel = new ErrorModel();
            CountModel countModel = new CountModel();
            ValidationModel validationModel = new ValidationModel();
            try {
                validationModel = validationModelList1.get(i);
                logger.debug("row number : "+i);
                i++;
                AddressModel addressModel = validationModel.getAddressModel();
                extraInfoModel = validationModel.getExtraInfoModel();
                productModel = validationModel.getProductModel();
                // check if order already exists.
                validationModel = marketPlaceOrderUtil.checkCorpOrderExists(validationModel);
                if(validationModel.getError() == false){
                    // order doesn't exist, so create a new order.

                    // validate customer details.
                    validationModel = marketPlaceOrderUtil.validateCustomerDetails(validationModel);
                    if (validationModel.getError() == Boolean.TRUE) {
                        validationModel.setMessage("Error is Customer Details.");
                    } else {
                        // no error in getting customer model.
                        addressModel.setId(validationModel.getUserModel().getIdHash());
                        validationModel.setAddressModel(addressModel);
                        // validate address details.

                     //  addressModel.setAid("1614158");
                    //   validationModel.setAddressModel(addressModel);

                       validationModel = marketPlaceOrderUtil.validateSelectedAddress(validationModel);
                        if (validationModel.getError() == Boolean.TRUE) {
                            validationModel.setMessage("Error is Address Validation.");
                        } else {
                            // check product details.
                            String prodCode = productModel.getProductCode();
                            Integer prodQty = productModel.getQuantity();
                            if (prodCode == "" || prodCode == null || prodQty <= 0) {
                                // product details incomplete.
                                logger.error("Incomplete Product Details.");
                                validationModel.setError(Boolean.TRUE);
                                validationModel.setMessage("Incomplete Product Details.");
                            } else {
                                // product details are not empty so bring product details to the model.
                                validationModel = marketPlaceOrderUtil.validateAndGetProductDetails(validationModel);
                                if (validationModel.getError() == Boolean.TRUE) {

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
                                            orderId = marketPlaceOrderUtil.createOrder(marketPlaceTempOrderModel, extraInfoModel);
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
                else {
                    validationModel.setMessage("Order exists already");
                }

                if (validationModel.getError()==true) {
                    errorModel.setRow(i);
                    errorModel.setMsg(validationModel.getMessage());
                    countModel.setFail(++fail);
                    countModel.setCorrect(correct);
                    errorModelList.add(errorModel);

                }
                else {
                    countModel.setCorrect(correct++);
                    countModel.setFail(fail);
                }

                // everything went well and lets add the model in the list.
            }
            catch(Exception e){
                logger.error("Exception Caught at validation : ", e);
                errorModel.setRow(i);
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
        DeliveryMessageModel deliveryMessageModel = new DeliveryMessageModel();
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

            marketPlaceTempOrderModel.setDeliveryMessageModel(deliveryMessageModel); // change it later
            logger.debug("TEMP-ORDER DEBUGGING : " + "setDeliveryMessageModel");

            marketPlaceTempOrderModel.setComment("0");
            logger.debug("TEMP-ORDER DEBUGGING : " + "setComment"+"0");

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
