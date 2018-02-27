package com.igp.admin.utils.marketPlace;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.igp.admin.models.marketPlace.*;
import com.igp.admin.models.marketPlace.APIOrderResponseModel;
import com.igp.admin.utils.httpRequest.HttpRequestUtil;
import com.igp.config.Environment;
import com.igp.config.SecretProperties;
import com.igp.config.instance.Database;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.BodyPartEntity;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * Created by suditi on 15/1/18.
 */
public class MarketPlaceOrderUtil {
    private static final Logger logger = LoggerFactory.getLogger(MarketPlaceOrderUtil.class);

    public boolean checkIfAffliate(int fkAssociateId) {
        Map<String, String> data = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        boolean result = false;
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "SELECT aa.associate_type_id FROM associate_type aa JOIN associate a on  a.fk_associate_type_id=aa.associate_type_id WHERE a.associate_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, fkAssociateId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                int type = resultSet.getInt("aa.associate_type_id");
                if(type==1){
                    result=true;
                }
            }
        } catch (Exception exception) {
            logger.error("Error at finding associate type :", exception);
        } finally {
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return result;
    }

    public ValidationModel validateCustomerDetails(ValidationModel validationModel) {

        UserModel userModel = validationModel.getUserModel();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequestUtil httpRequestUtil = new HttpRequestUtil();
        AuthResponseModel authResponseModel;
        GeneralUserResponseModel generalUserResponseModel = new GeneralUserResponseModel();
        validationModel.setError(Boolean.FALSE);
        try {
            if (userModel.getFirstname() == null || Objects.equals(userModel.getFirstname(), "") ||
                userModel.getEmail() == null || Objects.equals(userModel.getEmail(), "") ||
                userModel.getPassword() == null || Objects.equals(userModel.getPassword(), "") ||
                userModel.getMobile() == null || Objects.equals(userModel.getMobile(), "") ||
                userModel.getCountryId() == null || userModel.getCountryId() == 0) {

                logger.error("Customer Details can't be empty.");
                throw new Exception("Customer Details can't be empty.");
            } else {
                // other checks

                // check if customer exists.

                UserModel userModel1 = isUser(userModel);
                Integer custId = (new Integer(userModel1.getId()));
                if (custId != 0) {
                    userModel.setId(custId.toString());
                    userModel.setIdHash(userModel1.getIdHash());
                    // for existing customer blindly update all the fields.
                    if (updateDetails(userModel)) {
                        // updated the customer.
                        validationModel.setUserModel(userModel);
                    }
                    else {
                        validationModel.setError(true);
                        validationModel.setMessage("Error at updating Customer Details.");
                    }
                } else {
                    userModel.setId(null);
                    // customer doesn't exist therefore create a new customer.
                    String postData = objectMapper.writeValueAsString(userModel);
                    String custResponse = httpRequestUtil.sendCurlRequest(postData, "http://api.igp.com/v1/signup",new ArrayList<>());
                    generalUserResponseModel = objectMapper.readValue(custResponse, GeneralUserResponseModel.class);
                    // populate cust id hash in customer and address model.
                    authResponseModel = generalUserResponseModel.getData();
                    UserModel userModel2 = authResponseModel.getUser();
                    validationModel.setError(!authResponseModel.getLogin());
                    if(validationModel.getError()==true){
                        throw new Exception("New Customer couldn't be Created.");
                    }
                    else {
                        // since new customer created therefore update rest of the details.
                        String postData1 = objectMapper.writeValueAsString(userModel2);
                        String custUpdate = httpRequestUtil.sendCurlRequest(postData1, "http://api.igp.com/v1/signup",new ArrayList<>());
                        generalUserResponseModel = objectMapper.readValue(custUpdate, GeneralUserResponseModel.class);
                        authResponseModel =  generalUserResponseModel.getData();
                        // storing idHash and id in proper fields.
                        userModel2 = authResponseModel.getUser();
                        userModel.setIdHash(userModel2.getId());
                        validationModel.setUserModel(userModel);
                        validationModel.setError(!authResponseModel.getLogin());
                        if(validationModel.getError()==true){
                            throw new Exception("Customer details were not updated.");
                        }
                        else {
                            // fill all the details.
                            userModel = isUser(userModel);
                            updateDetails(userModel);
                            validationModel.setUserModel(userModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Exception at validating Customer Details.", e);
            validationModel.setError(Boolean.TRUE);
            validationModel.setMessage(e.getMessage());
        }
        logger.debug("Customer Id : " + userModel.getId());
        return validationModel;
    }

    public ValidationModel validateSelectedAddress(ValidationModel validationModel) {
        AddressModel shippingAddress = validationModel.getAddressModel();
        ObjectMapper objectMapper = new ObjectMapper();
        GeneralCustomerAddressMapResponseModel generalCustomerAddressMapResponseModel;
        GeneralShipResponseModel generalShipResponseModel = new GeneralShipResponseModel();
        HttpRequestUtil httpRequestUtil = new HttpRequestUtil();
        validationModel.setError(Boolean.FALSE);
        try {
            if (shippingAddress.getFirstname() == null || Objects.equals(shippingAddress.getFirstname(), "") ||
                shippingAddress.getStreetAddress() == null || Objects.equals(shippingAddress.getStreetAddress(), "") ||
                shippingAddress.getMobile() == null || Objects.equals(shippingAddress.getMobile(), "") ||
                shippingAddress.getPostcode() == null || Objects.equals(shippingAddress.getPostcode(), "") ||
                shippingAddress.getCountryId() == null || Objects.equals(shippingAddress.getCountryId(), "") ||
                shippingAddress.getState() == null || Objects.equals(shippingAddress.getState(), "") ||
                shippingAddress.getCity() == null || Objects.equals(shippingAddress.getCity(), "")
                ){

                logger.error("Delivery Details can't be empty.");
                throw new Exception("Delivery Details can't be empty.");

            } else if (Objects.equals(shippingAddress.getCountryId(), "99")) {
                Map<String, String> data = getStateAndCityByPin(shippingAddress.getPostcode());
                if (data.get("error").equals("0")) {
                    shippingAddress.setState(data.get("state"));
                    shippingAddress.setCity(data.get("city"));
                    shippingAddress.setMobilePrefix("91");
                } else {
                    //pincode entered was not found in the database.
                    logger.debug("Pincode not found in our database.");
                    throw new Exception("We do not serve this Pincode.");
                }
            }
            // model didn't return any error and now work on address book.
            String postData = objectMapper.writeValueAsString(shippingAddress);
            String addressExist = httpRequestUtil.sendCurlRequest(postData, "http://api.igp.com/v1/user/checkaddress",new ArrayList<>());
            generalShipResponseModel = objectMapper.readValue(addressExist, GeneralShipResponseModel.class);
            shippingAddress = generalShipResponseModel.getData();

            if (shippingAddress.getAid() == "" || shippingAddress.getAid() == null) {
                logger.error("Couldn't get proper address.");
                // create new address entry.
                String postData1 = objectMapper.writeValueAsString(shippingAddress);
                String createAddress = httpRequestUtil.sendCurlRequest(postData1, "http://api.igp.com/v1/user/address",new ArrayList<>());
                if(createAddress.contains("error")){
                    throw new Exception("Problem in Delivery Details.");

                }else {
                    generalCustomerAddressMapResponseModel = objectMapper.readValue(createAddress, GeneralCustomerAddressMapResponseModel.class);
                    Map<String, UserAddressModel> addressResponse = generalCustomerAddressMapResponseModel.getData();
                    UserAddressModel userAddressModel = addressResponse.get("addr");
                    shippingAddress.setAid(userAddressModel.getAddressId().toString());
                    logger.debug("Address id is : "+shippingAddress.getAid());
                }
                if (shippingAddress.getAid() == "" || shippingAddress.getAid() == null) {
                    throw new Exception("Couldn't get this Address from database.");
                }
            }
            validationModel.setAddressModel(shippingAddress);
        } catch (Exception e) {
            logger.error("Exception at validating Delivery Details : ", e);
            validationModel.setError(Boolean.TRUE);
            validationModel.setMessage(e.getMessage());
        }
        logger.debug("Address id is : "+shippingAddress.getAid());
        return validationModel;
    }

    public Map<String, String> getStateAndCityByPin(String id) {
        Map<String, String> data = new HashMap<>();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "SELECT city, state FROM pincode WHERE pincode = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                data.put("city", resultSet.getString("city"));
                data.put("state", resultSet.getString("state"));
                data.put("error", "0");
            } else {
                data.put("error", "1");
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
        } finally {
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return data;
    }

    public ValidationModel validateAndGetProductDetails(ValidationModel validationModel) {
        ProductModel productModel = validationModel.getProductModel();
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String prodCode = productModel.getProductCode();
        validationModel.setError(Boolean.FALSE);
        try {
            if (prodCode == "" || prodCode == null || prodCode.length() != 9 ) {
                logger.debug("product details : "+productModel);
                throw new Exception("Product Code is Wrong.");
            }
            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "SELECT * FROM products INNER JOIN newigp_product_extra_info WHERE " +
                "products.products_id = newigp_product_extra_info.products_id AND products.products_code = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, prodCode);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                //- trying to convert the prices from INR to USD.
                BigDecimal inrPrice = resultSet.getBigDecimal("products.products_mrp");
                //- INR-USD conversion completed

                ProductModel productModel1 = new ProductModel.Builder()
                    .id(resultSet.getInt("products.products_id"))
                    .quantity(productModel.getQuantity())
                    .name(resultSet.getString("newigp_product_extra_info.display_name"))
                    .sellingPrice(productModel.getSellingPrice())
                    .serviceDate(productModel.getServiceDate())
                    .giftBox(productModel.getGiftBox())
                    .perProductDiscount(productModel.getPerProductDiscount())
                    .serviceCharge(productModel.getServiceCharge())
                    .fkId(new Integer(resultSet.getString("products.fk_associate_id")))
                    .image(resultSet.getString("newigp_product_extra_info.m_img"))
                    .shortDescription(resultSet.getString("products.products_description_small"))
                    .baseCurrency(resultSet.getInt("products.products_base_currency"))
                    .lbh(resultSet.getString("newigp_product_extra_info.lbh"))
                    .volumeWeight(Integer.parseInt(resultSet.getString("products.products_volume_weight")))
                    .productCode(prodCode)
                    .displayAttrList(productModel.getDisplayAttrList())
                    .serviceTypeId(productModel.getServiceTypeId())
                    .serviceType(productModel.getServiceType())
                    .build();
                logger.debug("product details : "+productModel1);
                productModel = productModel1;
                validationModel.setProductModel(productModel1);
            }
            if(productModel.getId() == null || productModel.getId() == 0 ||
                productModel.getSellingPrice() == null || productModel.getQuantity() <= 0 ||
                productModel.getServiceCharge() == null){
                throw new Exception("Product is not available Or Details incorrect.");
            }
        } catch (Exception exception) {
            logger.error("Exception in products : ", exception);
            validationModel.setError(Boolean.TRUE);
            validationModel.setMessage(exception.getMessage());

        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return validationModel;
    }

    public String encryptPayment(String msg) throws NoSuchAlgorithmException {
        MessageDigest md;
        String out = "";
        try {
            md = MessageDigest.getInstance("SHA-512");

            md.update(msg.getBytes());
            byte[] mb = md.digest();

            for (byte temp : mb) {
                String s = Integer.toHexString(temp);
                while (s.length() < 2) {
                    s = "0" + s;
                }
                s = s.substring(s.length() - 2);
                out += s;
            }

        } catch (NoSuchAlgorithmException e) {
            logger.error("ERROR No algo found: {}", e);
        }
        return out;
    }

    public Integer createTempOrder(MarketPlaceTempOrderModel orderTempModel, ProductModel productModel) {
        Integer orderTempId = 0;
        Connection connection = null;
        ResultSet resultSet = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            connection.setAutoCommit(false);
            statement = "INSERT INTO orders_temp (" +
                "customers_id, address_book_id, gender, firstname, lastname, email_address, " +
                "street_address, city, state, postcode, country, relation, comments, delivery_instruction, " +
                "date_of_delivery, extra_value, order_product_total, order_product_discount, shipping_charges, shipping_charges_in_inr, " +
                "telephone_number, fk_associate_id, voucher_code, call_agent_name, call_customer_issue, campaignTracking, orders_temp_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, now())";

            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, orderTempModel.getCustomerId());
            preparedStatement.setInt(2, orderTempModel.getAddressBookId());
            if (orderTempModel.getShippingAddressModel().getTitle().equalsIgnoreCase("Mr.") || orderTempModel.getShippingAddressModel().getTitle().equalsIgnoreCase("m"))
                preparedStatement.setString(3, "m");
            else if (orderTempModel.getShippingAddressModel().getTitle().equalsIgnoreCase("Ms.") || orderTempModel.getShippingAddressModel().getTitle().equalsIgnoreCase("f"))
                preparedStatement.setString(3, "f");
            else
                preparedStatement.setString(3, "");
            logger.debug("serive charges : " + productModel.getServiceCharge());
            logger.debug("gift box : " + productModel.getGiftBox());
            logger.debug("quantity : " + productModel.getQuantity());
            logger.debug("sellingPrice : " + productModel.getSellingPrice());
            logger.debug("discount : "+orderTempModel.getDiscount());


            BigDecimal serviceCharges = productModel.getServiceCharge().add(new BigDecimal(productModel.getGiftBox() * productModel.getQuantity()));// * Environment.getGiftBoxPrice()));
            BigDecimal cartValue = productModel.getSellingPrice().add(serviceCharges).subtract(orderTempModel.getDiscount());

            preparedStatement.setString(4, orderTempModel.getShippingAddressModel().getFirstname());
            preparedStatement.setString(5, orderTempModel.getShippingAddressModel().getLastname());
            preparedStatement.setString(6, orderTempModel.getShippingAddressModel().getEmail());
            preparedStatement.setString(7, orderTempModel.getShippingAddressModel().getStreetAddress() + ", " + orderTempModel.getShippingAddressModel().getStreetAddress2());
            preparedStatement.setString(8, orderTempModel.getShippingAddressModel().getCity());
            preparedStatement.setString(9, orderTempModel.getShippingAddressModel().getState());
            preparedStatement.setString(10, orderTempModel.getShippingAddressModel().getPostcode());
            preparedStatement.setString(11, orderTempModel.getShippingAddressModel().getCountryId());
            preparedStatement.setString(12, orderTempModel.getShippingAddressModel().getRelation());
            preparedStatement.setString(13, orderTempModel.getComment());
            preparedStatement.setString(14, orderTempModel.getDeliveryInstr());
            preparedStatement.setString(15, orderTempModel.getDeliveryDate());
            preparedStatement.setString(16, orderTempModel.getExtraValue());
            preparedStatement.setBigDecimal(17, CurrencyConversionUtil.getUsdPrice(cartValue, 2));
            preparedStatement.setBigDecimal(18, CurrencyConversionUtil.getUsdPrice(orderTempModel.getDiscount(), 2));
            preparedStatement.setBigDecimal(19, CurrencyConversionUtil.getUsdPrice(serviceCharges, 2));
            preparedStatement.setBigDecimal(20, serviceCharges);
            preparedStatement.setString(21, orderTempModel.getShippingAddressModel().getMobile());
            preparedStatement.setInt(22, orderTempModel.getAssociateId());
            preparedStatement.setString(23, orderTempModel.getVoucher());
            preparedStatement.setString(24, "call_agent_name");
            preparedStatement.setString(25, "call_customer_issue");
            preparedStatement.setString(26, "campaignTracking");
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to create tempOrder");
                orderTempId = 0;
            } else {
                resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                String shippingType ="";
                orderTempId = resultSet.getInt(1);
                if (orderTempId != null && orderTempId != 0) {
                    logger.debug("TEMP-ORDER CREATED SUCCESSFULLY: " + orderTempId);
                    if (productModel.getGiftBox().equals("1")) {
                        shippingType = shippingType + productModel.getServiceType() + "[Rs. " + productModel.getServiceCharge() + "]" + "GiftBox Item";
                    } else {
                        shippingType = shippingType + productModel.getServiceType() + "[Rs. " + productModel.getServiceCharge() + "]";
                    }

                    OrderTempBasketModel orderTempBasketModel = new OrderTempBasketModel();
                    orderTempBasketModel.setCustomerId(orderTempModel.getCustomerId());
                    orderTempBasketModel.setOrderTempId(orderTempId);
                    orderTempBasketModel.setProductId(productModel.getId());
                    orderTempBasketModel.setQuantity(productModel.getQuantity());
                    orderTempBasketModel.setVendorId(productModel.getFkId());
                    orderTempBasketModel.setBaseCurrency(2);
                    orderTempBasketModel.setBaseCurrencyValue(1);
                    orderTempBasketModel.setBaseCurrencyValueInr(65);
                    orderTempBasketModel.setServiceCharges(serviceCharges);
                    orderTempBasketModel.setServiceType(shippingType);
                    orderTempBasketModel.setProductAttribute(parseAndSortProductAttributes(productModel.getDisplayAttrList()));
                    orderTempBasketModel.setGiftBox(productModel.getGiftBox());
                    orderTempBasketModel.setServiceTypeId(new Integer(productModel.getServiceTypeId()));
                    orderTempBasketModel.setServiceDate(productModel.getServiceDate()); // check format of storing it
                    orderTempBasketModel.setServiceTime("");
                    Integer tempOrderBasketId = createTempOrderBasket(orderTempBasketModel);
                    if (tempOrderBasketId != null) {
                        if (insertIntoOrdersTempBasketExtraInfo(orderTempBasketModel, tempOrderBasketId))
                            logger.debug("ORDER-TEMP-EXTRA-INFO UPDATED");

                        connection.commit();
                    } else {
                        connection.rollback();
                        throw new Exception("Exception in connection while creation of temp order.");
                    }
                }
            }
        } catch (Exception exception) {
            orderTempId = 0;
            logger.error("Exception in connection while creation of temp order : ", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
            Database.INSTANCE.closeResultSet(resultSet);
        }
        return orderTempId;
    }
    public String parseAndSortProductAttributes(Map<String, String> attributes) {
        String sortedAttributes;
        String[] attributeArray = new String[attributes.size()];
        Integer count = 0;
        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            logger.debug(entry.getKey() + ":" + entry.getValue());
            attributeArray[count] = entry.getKey() + ":" + entry.getValue();
            count++;
        }
        logger.debug("UNSORTED ATTRIBUTE-LIST: " + Arrays.toString(attributeArray));

        Arrays.sort(attributeArray);
        sortedAttributes = Arrays.toString(attributeArray);

        logger.debug("SORTED ATTRIBUTES: " + sortedAttributes);
        return sortedAttributes;
    }


    public Integer createTempOrderBasket(OrderTempBasketModel orderTempBasketModel) {
        Integer tempOrderBasketId = null;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;

        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO orders_temp_basket (customers_id, products_id, products_quantity, orders_temp_id, " +
                "fk_associate_id, products_base_currency, products_base_currency_value, products_base_currency_value_inr, " +
                "special_charges, shipping_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, orderTempBasketModel.getCustomerId());
            preparedStatement.setInt(2, orderTempBasketModel.getProductId());
            preparedStatement.setInt(3, orderTempBasketModel.getQuantity());
            preparedStatement.setInt(4, orderTempBasketModel.getOrderTempId());
            preparedStatement.setInt(5, orderTempBasketModel.getVendorId());
            preparedStatement.setInt(6, orderTempBasketModel.getBaseCurrency());
            preparedStatement.setInt(7, orderTempBasketModel.getBaseCurrencyValue());
            preparedStatement.setInt(8, orderTempBasketModel.getBaseCurrencyValueInr()); // to be updated based on the current prices and products_base_currency.
            preparedStatement.setBigDecimal(9, orderTempBasketModel.getServiceCharges());
            preparedStatement.setString(10, orderTempBasketModel.getServiceType());

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to create tempOrder");
            } else {
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.first();
                tempOrderBasketId = resultSet.getInt(1);
                logger.debug("ORDER-TEMP-BASKET CREATED SUCCESSFULLY: " + tempOrderBasketId);
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return tempOrderBasketId;
    }

    public Boolean insertIntoOrdersTempBasketExtraInfo(OrderTempBasketModel orderTempBasketModel, Integer orderTempBasketId) {
        Boolean response = Boolean.FALSE;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        try {
            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "INSERT INTO order_temp_basket_extra_info (order_temp_basket_id, product_id, " +
                "quantity, attributes, gift_box, delivery_type, delivery_date, delivery_time)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, orderTempBasketId);
            preparedStatement.setInt(2, orderTempBasketModel.getProductId());
            preparedStatement.setInt(3, orderTempBasketModel.getQuantity());
            preparedStatement.setString(4, orderTempBasketModel.getProductAttribute());
            preparedStatement.setInt(5, orderTempBasketModel.getGiftBox());
            preparedStatement.setInt(6, orderTempBasketModel.getServiceTypeId());
            preparedStatement.setString(7, orderTempBasketModel.getServiceDate());
            preparedStatement.setString(8, orderTempBasketModel.getServiceTime());

            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed to create Order");
            } else {
                response = Boolean.TRUE;
                logger.debug("order_temp_basket_extra_info successfully populated!");
            }
        } catch (Exception exception) {
            logger.error("Exception in connection", exception);
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return response;
    }


    public UserModel isUser(UserModel user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        user.setId(0 + "");
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "SELECT * FROM customers c INNER JOIN n_user n ON n.id = c.customers_id where c.customers_email_address = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, user.getEmail());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                logger.debug("USER DEBUGGING : " + "PreparedStatement "+preparedStatement);
                logger.debug("USER DEBUGGING : " + "setUser_id "+resultSet.getString("c.customers_id"));
                user.setId(resultSet.getString("c.customers_id"));
                logger.debug("USER DEBUGGING : " + "setUserHash "+resultSet.getString("n.id_hash"));
                user.setIdHash(resultSet.getString("n.id_hash"));
                //  logger.debug("USER DEBUGGING : " + "setUserDOB "+resultSet.getString("c.customers_dob"));

                //  if(resultSet.getString("c.customers_dob").equals("none") || resultSet.getString("c.customers_dob").equals("") || resultSet.getString("c.customers_dob").isEmpty()){
                // don't take dob.
                //  }
                //  else {
                //       user.setDob(resultSet.getString("c.customers_dob"));
                //   }
            }
        } catch (Exception exception) {
            logger.error("Exception getting user from database : ", exception);
        } finally {
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return user;
    }

    public Boolean updateDetails(UserModel userModel) {
        Boolean response;
        Connection connection = null;
        String statement;
        PreparedStatement preparedStatement = null;
        int custId = new Integer(userModel.getId());
        try {

            Integer isChangeDob = 0;
            String columns = "";
            if (userModel.getDob() != null && !userModel.getDob().isEmpty()) {
                columns = ", c.customers_dob = ?";
                isChangeDob = 1;
            }
            if (Objects.equals(userModel.getCountryId(), "99") && !userModel.getPostcode().isEmpty() && userModel.getPostcode()!=null) {
                Map<String, String> data = getStateAndCityByPin(userModel.getPostcode());
                if (data.get("error").equals("0")) {
                    userModel.setState(data.get("state"));
                    userModel.setCity(data.get("city"));
                    userModel.setMobilePrefix("91");
                } else {
                    //pincode entered was not found in the database.
                    logger.debug("Pincode not found in our database.");
                    throw new Exception("We do not serve this Pincode.");
                }
            }

            connection = Database.INSTANCE.getReadWriteConnection();
            statement = "UPDATE customers c INNER JOIN n_user ce ON ce.id = c.customers_id SET " +
                " c.customers_firstname = ?, c.customers_lastname = ?, c.customers_mobile = ? ," +
                " ce.int_mob_prefix = ? , c.customers_country_id = ? , c.customers_street_address = ? ," +
                " c.customers_postcode = ? , c.customers_city = ? , c.customers_state = ? "+columns + " WHERE c.customers_id = ?";
            preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, userModel.getFirstname());
            preparedStatement.setString(2, userModel.getLastname());
            preparedStatement.setString(3, userModel.getMobile());
            preparedStatement.setString(4, userModel.getMobilePrefix());
            preparedStatement.setInt(5, userModel.getCountryId());
            preparedStatement.setString(6, userModel.getAddressField1()==null?"":userModel.getAddressField1());
            preparedStatement.setString(7, userModel.getPostcode()==null?"":userModel.getPostcode());
            preparedStatement.setString(8, userModel.getCity()==null?"":userModel.getCity());
            preparedStatement.setString(9, userModel.getState()==null?"":userModel.getState());

            if (isChangeDob == 1){
                preparedStatement.setString(10, userModel.getDob());
                preparedStatement.setInt(11, custId);
            }
            else {
                preparedStatement.setInt(10, custId);
            }
            Integer status = preparedStatement.executeUpdate();
            if (status == 0) {
                logger.error("Failed while updating user profile.");
                response = Boolean.FALSE;
            } else {
                response = Boolean.TRUE;
                logger.debug("USER PROFILE UPDATED SUCCESSFULLY..!!");
            }
        } catch (Exception exception) {
            logger.error("Exception at Updating Customer : "+exception.getMessage(), exception);
            response = Boolean.FALSE;
        } finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return response;
    }

    public  Integer createOrder(MarketPlaceTempOrderModel orderTempModel, ExtraInfoModel extraInfoModel) {
        // fill the order model.
        Integer orderId = 0;
        MarketPlaceOrderModel marketPlaceOrderModel = new MarketPlaceOrderModel();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequestUtil httpRequestUtil = new HttpRequestUtil();
        GeneralOrderResponseModel generalOrderResponseModel;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        String statement, statement1;
        try {
            marketPlaceOrderModel.setOrderTempId(orderTempModel.getTempOrderId());
            marketPlaceOrderModel.setAssociateId(orderTempModel.getAssociateId());
            marketPlaceOrderModel.setIdHash(orderTempModel.getIdHash());
            marketPlaceOrderModel.setPaymentStatus(true);
            marketPlaceOrderModel.setOrderPaySite("MarketPlace");
            String hashStringSequence = marketPlaceOrderModel.getIdHash() +
                marketPlaceOrderModel.getOrderTempId() +
                marketPlaceOrderModel.getPaymentStatus().toString() +
                marketPlaceOrderModel.getOrderPaySite();

            String hash = encryptPayment(hashStringSequence + SecretProperties.getPaymentKey());
            marketPlaceOrderModel.setHash(hash);
            // hit api and receive the order response.
            HeaderKeyValueModel headerKeyValueModel = new HeaderKeyValueModel();
            List<HeaderKeyValueModel> headerKeyValueModelList = new ArrayList<>();
            headerKeyValueModel.setKey("deviceType");
            headerKeyValueModel.setValue("BO-Panel");
            headerKeyValueModelList.add(headerKeyValueModel);
            String postData = objectMapper.writeValueAsString(marketPlaceOrderModel);
            String orderRequest = httpRequestUtil.sendCurlRequest(postData, "http://api.igp.com/v1/checkout/order",headerKeyValueModelList);
            generalOrderResponseModel = objectMapper.readValue(orderRequest, GeneralOrderResponseModel.class);
            Map<String, APIOrderResponseModel> orderResponse = generalOrderResponseModel.getData();
            APIOrderResponseModel apiOrderResponseModel = orderResponse.get("payment");

            if(orderResponse.get("error") != null || (apiOrderResponseModel.getOrderId() == 0 || apiOrderResponseModel.getOrderId() == null)){
                // order is not created
                throw new Exception("Exception in Order Creation.");
            }else{
                Integer status;
                orderId = apiOrderResponseModel.getOrderId();
                logger.debug("Orders Created successfully : " + orderId);
                connection = Database.INSTANCE.getReadWriteConnection();
                statement = "UPDATE orders SET rl_req_ID = ? , marketplace_data = ? , marketplace_name = ? WHERE orders_id = ?";
                preparedStatement = connection.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, extraInfoModel.getRelId());
                preparedStatement.setString(2, extraInfoModel.getMarketData());
                preparedStatement.setString(3, extraInfoModel.getMarketName());
                preparedStatement.setInt(4, apiOrderResponseModel.getOrderId());
                status = preparedStatement.executeUpdate();
                if(status==1) {
                    // then only update extra info table.
                    statement1 = "UPDATE orders_extra_info SET gst_no = ? WHERE orders_id = ?";
                    preparedStatement1 = connection.prepareStatement(statement1, Statement.RETURN_GENERATED_KEYS);
                    preparedStatement1.setString(1, extraInfoModel.getGstNo());
                    preparedStatement1.setInt(2, apiOrderResponseModel.getOrderId());
                    status = preparedStatement1.executeUpdate();
                }

                if (status == 0) {
                    logger.error("Failed while updating orders table");
                    throw new Exception("Failed while updating orders table");
                } else {
                    logger.debug("Orders & orders_extra_info updation successful");
                }
            }
        }
        catch (Exception e){
            logger.error("Exception While Creation of Order : ", e);
            orderId = 0;
        }
        finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeStatement(preparedStatement1);
            Database.INSTANCE.closeConnection(connection);
        }
        return orderId;
    }
    public int getCountryId(String countryName) {
        int countryId = 0;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "SELECT countries_id FROM countries WHERE countries_name = ?";
            preparedStatement = connection.prepareStatement(statement);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                countryId = Integer.valueOf(resultSet.getString("countries_id"));
            }
        } catch (Exception exception) {
            logger.error("Exception in getting country id from database", exception);
        } finally {
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return countryId;
    }

    public String getMobilePrefixByCountryId(int countryId) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String mobilePrefix = "0";
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "SELECT mprefix FROM country_mobile_prefix WHERE id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, countryId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                mobilePrefix = resultSet.getString("mprefix");
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
        } finally {
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return mobilePrefix;
    }

    public String getProductIdForLoyaltyOnly(String productCode) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "select distinct f.product_sku from prep_sku_attribution a join prep_ticket_index f on f.id = a.prep_id join prep_sku s on s.prep_sku_id = f.id join prep_ticket_index b on b.id = s.prep_id where a.festival_config like '96|%' or a.festival_config like '%|96|%' and (b.barcode = ? or f.product_sku = ?)  limit 1;";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setString(1, productCode);
            preparedStatement.setString(2, productCode);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.first()) {
                productCode = resultSet.getString("product_sku");
            }
            else {
                productCode = "";
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
        } finally {
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return productCode;
    }


    public ValidationModel checkCorpOrderExists(ValidationModel validationModel){
        CheckCorpOrderModel checkCorpOrderModel = new CheckCorpOrderModel();
        ObjectMapper objectMapper = new ObjectMapper();
        HttpRequestUtil httpRequestUtil = new HttpRequestUtil();
        GenerateCheckCorpOrderResponseModel generalAddressResponseModel = new  GenerateCheckCorpOrderResponseModel();
        try {
            checkCorpOrderModel.setFkId(validationModel.getFkAssociateId());
            checkCorpOrderModel.setError(false);
            checkCorpOrderModel.setRelId(validationModel.getExtraInfoModel().getRelId());
            checkCorpOrderModel.setOrderId(0);
            if(!checkCorpOrderModel.getRelId().isEmpty()||!checkCorpOrderModel.getRelId().equals("0")) {
                String postData = objectMapper.writeValueAsString(checkCorpOrderModel);
                String orderExist = httpRequestUtil.sendCurlRequest(postData, "http://api.igp.com/v1/corporate/corpordercheck",new ArrayList<>());
                generalAddressResponseModel = objectMapper.readValue(orderExist, GenerateCheckCorpOrderResponseModel.class);
                checkCorpOrderModel = generalAddressResponseModel.getData();
                if (checkCorpOrderModel.getError() == false) {
                    if (checkCorpOrderModel.getFlag() == true) {
                        throw new Exception("Duplicate Order.");
                    }
                } else {
                    throw new Exception("Exception at checking order already exists");
                }
            }else {
                throw new Exception("PO number can't be Empty.");
            }
        }
        catch (Exception e){
            validationModel.setError(true);
            validationModel.setMessage(e.getMessage());
            logger.error("Exception at check order exists : " + e);
        }
        return validationModel;
    }

    public FileUploadModel uploadTheFile(FormDataMultiPart multiPart, String filePrefix){
        FileUploadModel fileUploadModel = new FileUploadModel();
        String filePath;
        String fileExtention="";
        String fileNamePrefix="";
        try {
            fileNamePrefix += filePrefix + "/";

            Map<String, List<FormDataBodyPart>> bodyParts = multiPart.getFields();
            for (Map.Entry<String, List<FormDataBodyPart>> entry : bodyParts.entrySet()) {
                List<FormDataBodyPart> bodyPartsList = entry.getValue();
                for (int i = 0; i < bodyPartsList.size(); i++) {
                    BodyPartEntity bodyPartEntity = (BodyPartEntity) bodyPartsList.get(i).getEntity();

                    String[] splittedFileName = bodyPartsList.get(i).getContentDisposition().getFileName().split("\\.");
                    fileExtention = splittedFileName[splittedFileName.length - 1];
                    filePath = fileNamePrefix + System.currentTimeMillis() + "." + fileExtention;
                    File newFile = File.createTempFile("prefix", "suffix");
                    FileUtils.copyInputStreamToFile(bodyPartEntity.getInputStream(), newFile);
                    fileUploadModel.setError(false);
                    fileUploadModel.setFile(newFile);
                    fileUploadModel.setUploadedFilePath(filePath);
                    fileUploadModel.setFileName(splittedFileName[0]);
                    fileUploadModel.setFileExtension(fileExtention);
                }
            }
        }catch (Exception exception){
            logger.debug("Exception Occured while uploading file. ");
            logger.error("Exception Occured while uploading file : ", exception);
            fileUploadModel.setError(true);
        }
        return fileUploadModel;
    }

}
