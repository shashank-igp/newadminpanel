package com.igp.admin.CustomerCareRelatedPanles.mappers;

import com.igp.admin.CustomerCareRelatedPanles.utils.SendFollowUpMailPanelUtil;
import com.igp.handles.admin.models.Mail.MailTemplateModel;
import com.igp.handles.admin.utils.MailUtil.MailConstants;
import com.igp.handles.admin.utils.MailUtil.MailUtil;
import com.igp.handles.vendorpanel.models.Order.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by shanky on 23/8/18.
 */
public class SendFollowUpMailPanelMapper {
    private static final Logger logger = LoggerFactory.getLogger(SendFollowUpMailPanelMapper.class);
    public List<String> uploadFolloUpTrackingNumberFile(Map<Integer, Map<String, String>> listOfAwb,String issue){
        List<String> result=new ArrayList<>();
        MailTemplateModel mailTemplateModel=null;
        MailUtil mailUtil=new MailUtil();
        Order order=null;
        String emailBody=null;
        StringBuilder recipientAddress=null;
        StringBuilder subject=null;
        SendFollowUpMailPanelUtil sendFollowUpMailPanelUtil=new SendFollowUpMailPanelUtil();
        try{
            if(issue.equalsIgnoreCase("AddressRelated")){
                mailTemplateModel=mailUtil.getMailTemplateFromDb(MailConstants.FOLLOW_UP_MAIL_ADDRESS_RELATED);
            }else if(issue.equalsIgnoreCase("CustomerNotFound")){
                mailTemplateModel=mailUtil.getMailTemplateFromDb(MailConstants.FOLLOW_UP_MAIL_CUSTOMMER_NOT_FOUND);
            }
            for(Map.Entry<Integer,Map<String,String>> entry:listOfAwb.entrySet()){
                Map<String,String> columnNameToAWBMap=entry.getValue();
                for(Map.Entry<String,String> entry1:columnNameToAWBMap.entrySet()){
                    recipientAddress=new StringBuilder();
                    subject=new StringBuilder();
                    String awb=null;
                    try{
                        awb=new BigDecimal(entry1.getValue()).toPlainString();
                    }catch (RuntimeException runTimeException){
                        awb=entry1.getValue();
                    }
                    if(issue.equalsIgnoreCase("AddressRelated")){
                        subject.append("Incomplete address. ");
                    }else if(issue.equalsIgnoreCase("CustomerNotFound")){
                        subject.append("Recipient unavailable. ");
                    }
                    if( awb != null && !awb.equalsIgnoreCase("")){
                        order=sendFollowUpMailPanelUtil.getOrderDetailsBasedOnAwbNumber(awb);
                    }
                    if(order!=null && mailTemplateModel != null){
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

//                        logger.debug("Mail Template :- "+mailTemplateModel.toString());

                        logger.debug("Order Model : "+order.toString());
                        logger.debug("Recipient Address : "+recipientAddress.toString());
                        if(mailTemplateModel.getContent() !=null ){
                            emailBody=mailTemplateModel.getContent().replace("(<orders_id>)",String.valueOf(order.getOrderId()));
                            emailBody=emailBody.replace("<address as printed on label including recipient name>",recipientAddress.toString());

                        }
                        subject.append("Order #");
                        subject.append(String.valueOf(order.getOrderId()));
                        subject.append(" AWB #");
                        subject.append(awb);
//                        logger.debug("Subject :- "+subject.toString());
                    }

                    if(order != null && subject !=null && emailBody !=null && order.getCustomersEmail() !=null && mailUtil.sendGenericMail("",subject.toString(),emailBody,order.getCustomersEmail(),false)){
                        result.add(awb+"-Mail Sent Successfully");
                    }else{
                        result.add(awb+"-Mail Not Sent");
                    }
                }
            }
        }catch (Exception exception){
            logger.error("Exeception occured while sending followup mails to customers",exception);
        }
        return result;
    }
}
