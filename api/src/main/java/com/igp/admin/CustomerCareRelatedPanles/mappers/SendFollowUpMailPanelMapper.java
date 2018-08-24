package com.igp.admin.CustomerCareRelatedPanles.mappers;

import com.igp.admin.CustomerCareRelatedPanles.utils.SendFollowUpMailPanelUtil;
import com.igp.handles.admin.models.Mail.MailTemplateModel;
import com.igp.handles.admin.utils.MailUtil.MailConstants;
import com.igp.handles.admin.utils.MailUtil.MailUtil;
import com.igp.handles.vendorpanel.models.Order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by shanky on 23/8/18.
 */
public class SendFollowUpMailPanelMapper {
    private static final Logger logger = LoggerFactory.getLogger(SendFollowUpMailPanelMapper.class);
    public Map<String,String> uploadFolloUpTrackingNumberFile(Map<Integer, Map<String, String>> listOfAwb,String issue){
        Map<String,String> result=new HashMap<>();
        MailTemplateModel mailTemplateModel=null;
        MailUtil mailUtil=new MailUtil();
        Order order=null;
        String emailBody=null;
        StringBuilder recipientAddress=new StringBuilder();
        StringBuilder subject=new StringBuilder();
        SendFollowUpMailPanelUtil sendFollowUpMailPanelUtil=new SendFollowUpMailPanelUtil();
        try{
            if(issue.equalsIgnoreCase("Address_Related")){
                mailTemplateModel=mailUtil.getMailTemplateFromDb(MailConstants.FOLLOW_UP_MAIL_ADDRESS_RELATED);
                subject.append("Incomplete address. ");
            }else if(issue.equalsIgnoreCase("Customer_Not_Found")){
                mailTemplateModel=mailUtil.getMailTemplateFromDb(MailConstants.FOLLOW_UP_MAIL_CUSTOMMER_NOT_FOUND);
                subject.append("Recipient unavailable. ");
            }
            for(Map.Entry<Integer,Map<String,String>> entry:listOfAwb.entrySet()){
                Map<String,String> columnNameToAWBMap=entry.getValue();
                for(Map.Entry<String,String> entry1:columnNameToAWBMap.entrySet()){
                    String awb=new BigDecimal(entry1.getValue()).toPlainString();
                    order=sendFollowUpMailPanelUtil.getOrderDetailsBasedOnAwbNumber(awb);

                    if(order!=null){
                        if(order.getDeliveryName()!=null){
                            recipientAddress.append(order.getDeliveryName());
                        }else {
                            recipientAddress.append("Name : ");
                        }
                        recipientAddress.append("<Br>");
                        recipientAddress.append("Mobile No : ");
                        if(order.getDeliveryMobile()!=null){
                            recipientAddress.append(order.getDeliveryMobile());
                        }else {
                            recipientAddress.append("");
                        }
                        recipientAddress.append("<Br>");
                        if(order.getDeliveryStreetAddress()!=null){
                            recipientAddress.append(order.getDeliveryStreetAddress());
                        }else{
                            recipientAddress.append("Address : ");
                        }
                        recipientAddress.append("<Br>");
                        if(order.getDeliveryCity()!=null){
                            recipientAddress.append(order.getDeliveryCity());
                        }else{
                            recipientAddress.append("City : ");
                        }
                        recipientAddress.append(",");
                        if(order.getDeliveryPostcode()!=null){
                            recipientAddress.append(order.getDeliveryPostcode());
                        }else{
                            recipientAddress.append(" Pincode : ");
                        }
                        recipientAddress.append("<Br>");
                        if(order.getDeliveryState()!=null){
                            recipientAddress.append(order.getDeliveryState());
                        }else {
                            recipientAddress.append("State : ");
                        }
                        recipientAddress.append(",");
                        if(order.getDeliveryCountry()!=null){
                            recipientAddress.append(order.getDeliveryCountry());
                        }else{
                            recipientAddress.append(" Country : ");
                        }


                        emailBody=mailTemplateModel.getContent().replace("(<orders_id>)",String.valueOf(order.getOrderId()));
                        emailBody=emailBody.replace("<address as printed on label including recipient name>",recipientAddress.toString());

                        subject.append("Order #");
                        subject.append(String.valueOf(order.getOrderId()));
                        subject.append(" AWB #");
                        subject.append(awb);
                    }

                    if(order != null && mailUtil.sendGenericMail("",subject.toString(),emailBody,order.getDeliveryEmail(),false)){
                        result.put(awb,"Mail Sent Successfully");
                    }else{
                        result.put(awb,"Mail Not Sent");
                    }
                }
            }
        }catch (Exception exception){
            logger.error("Exeception occured while sending followup mails to customers",exception);
        }
        return result;
    }
}
