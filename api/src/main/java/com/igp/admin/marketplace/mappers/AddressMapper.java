package com.igp.admin.marketplace.mappers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.igp.admin.marketplace.models.AddressModel;
import com.igp.config.instance.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Created by suditi on 11/1/18.
 */
public class AddressMapper {
    private static final Logger logger = LoggerFactory.getLogger(AddressMapper.class);

    public AddressModel getByAddressId(Integer id) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        AddressModel addressModel = null;
        ResultSet resultSet = null;
        String salute;
        /*currently not checking the addressId to userId match.
        Need to check for the match and then send the addressModel.
        Otherwise anyone can check for any address since the addressId are exposed in numbers.
         */
        try {
            connection = Database.INSTANCE.getReadOnlyConnection();
            String statement = "SELECT * FROM address_book as a LEFT JOIN address_book_extra_info as b on a.address_book_id = b.address_book_id  WHERE a.address_book_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.first()) {
                addressModel = new AddressModel();
                addressModel.setId(resultSet.getString("a.address_book_id"));
                if(resultSet.getString("a.entry_gender").equalsIgnoreCase("m") || resultSet.getString("a.entry_gender").equalsIgnoreCase("Mr."))
                    salute = "Mr.";
                else
                    salute = "Ms.";
                addressModel.setTitle(salute);
                addressModel.setFirstname(resultSet.getString("a.entry_firstname"));
                addressModel.setLastname(resultSet.getString("a.entry_lastname"));
                addressModel.setStreetAddress(resultSet.getString("a.entry_street_address"));
                addressModel.setPostcode(resultSet.getString("a.entry_postcode"));
                addressModel.setCity(resultSet.getString("a.entry_city"));
                addressModel.setState(resultSet.getString("a.entry_state"));
                addressModel.setCountryId(resultSet.getString("a.entry_country_id"));
                addressModel.setRelation(resultSet.getString("a.entry_relation"));
                addressModel.setEmail(resultSet.getString("a.entry_email_address"));
                addressModel.setMobile(resultSet.getString("a.mobile"));
                addressModel.setMobilePrefix(resultSet.getString("b.int_mob_prefix"));
                addressModel.setAddressType(resultSet.getInt("address_type"));
            }
        } catch (Exception exception) {
            logger.error("Error", exception);
        } finally {
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeConnection(connection);
        }
        return addressModel;
    }

}
