package com.igp.handles.utils.Reports;

import com.igp.config.instance.Database;
import com.igp.handles.models.Report.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by shanky on 27/12/17.
 */
public class PayoutAndTaxesReport {

    private static final Logger logger = LoggerFactory.getLogger(PayoutAndTaxesReport.class);

    public PayoutAndTaxReportSummaryModel getPayoutAndTaxes(int fkAssociateId,int orderId,String orderDateFrom, String orderDateTo,
        String orderDeliveryDateFrom, String orderDeliveryDateTo,String startLimit,String endLimit){

        PayoutAndTaxReportSummaryModel payoutAndTaxReportSummaryModel = new PayoutAndTaxReportSummaryModel();
        Connection connection = null;
        String statement;
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        List<OrderTaxReport> orderReportObjectModelList=new ArrayList<>();
        double totaltaxableAmount=0.0,totalAmount=0.0;
        int totalOrderId=0;
        StringBuilder sb=new StringBuilder("");

        try{

            if(orderId!=0){
                sb.append(" and o.orders_id = "+orderId+" ");
            }
            if(orderDateFrom != null && !orderDateFrom.isEmpty()){
                sb.append(" and date_format(o.date_purchased,'%Y-%d-%m') >= '"+orderDateFrom+"' ");
            }
            if(orderDateTo != null && !orderDateTo.isEmpty()){
                sb.append(" and date_format(o.date_purchased,'%Y-%d-%m') <= '"+orderDateTo+"' ");
            }
            if(orderDeliveryDateFrom != null && !orderDeliveryDateFrom.isEmpty()){
                sb.append(" and date_format(o.date_of_delivery,'%Y-%d-%m') >= '"+orderDeliveryDateFrom+"' ");
            }
            if(orderDeliveryDateTo != null && !orderDeliveryDateTo.isEmpty()){
                sb.append(" and date_format(o.date_of_delivery,'%Y-%d-%m') <= '"+orderDeliveryDateTo+"' ");
            }

            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select o.orders_id as orderId, sum(gvd.taxable) as taxableAmount ,sum(gvd.amt) as totalAmount, "
                + "(sum(gvd.igst)+sum(gvd.sgst)+sum(gvd.cgst)) as tax , o.delivery_postcode as pincode,  "
                + "date_format(o.date_purchased,'%Y-%d-%m') datePurchased,date_format(o.date_of_delivery,'%Y-%d-%m') "
                + " dateOfDelivery , gvd.invoice_num invoiceNum , thp.orders_id as paymentCheckOrderId "
                + " , min(case when op.orders_product_status = 'Processed' then 1 when op.orders_product_status = 'Confirmed' "
                + " then 2 when op.orders_product_status = 'Shipped' then 3 else 4 end ) status, max(op.delivery_status) "
                + " deliveryStatus from orders o  left join tax_handels_payout thp on o.orders_id = thp.orders_id "
                + " join gst_vendors_dom gvd on o.orders_id =gvd.order_id join orders_products op on op.orders_id = o.orders_id "
                + "  where gvd.vendorID = "+fkAssociateId+"  "+sb.toString()+" group by o.orders_id "
                + "  limit "+startLimit+","+endLimit+" ";


            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getPayoutAndTaxes "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String status=resultSet.getString("status");
                int deliveryStatusFlag=resultSet.getInt("deliveryStatus");

                OrderTaxReport orderTaxReport=new OrderTaxReport();
                orderTaxReport.setOrderId(resultSet.getInt("orderId"));
                orderTaxReport.setTaxableAmount(resultSet.getDouble("taxableAmount"));
                orderTaxReport.setTotalAmount(resultSet.getDouble("totalAmount"));
                orderTaxReport.setTax(resultSet.getDouble("tax"));
                orderTaxReport.setPincode(resultSet.getString("pincode"));
                orderTaxReport.setDatePurchased(resultSet.getString("datePurchased"));
                orderTaxReport.setInvoiceNumber(resultSet.getString("invoiceNum"));


                if(status.equalsIgnoreCase("3")){
                    if(deliveryStatusFlag==1){
                        orderTaxReport.setOrderStatus("Delivered");
                    }else {
                        orderTaxReport.setOrderStatus("Out For Delivery");
                    }
                }else if(status.equalsIgnoreCase("2")){
                    orderTaxReport.setOrderStatus("Confirmed");
                }else if(status.equalsIgnoreCase("1")){
                    orderTaxReport.setOrderStatus("Processed");
                }else {
                    orderTaxReport.setOrderStatus("");
                }


                orderTaxReport.setDeliveryDate(resultSet.getString("dateOfDelivery"));
                if(resultSet.getInt("paymentCheckOrderId")==0){
                    orderTaxReport.setPaymentStatus("Pending");
                }else {
                    orderTaxReport.setPaymentStatus("Paid");
                }
                orderReportObjectModelList.add(orderTaxReport);
                totalOrderId++;
                totaltaxableAmount=totaltaxableAmount+orderTaxReport.getTaxableAmount();
                totalAmount=totalAmount+orderTaxReport.getTotalAmount();
            }
            SummaryModel summaryModel=new SummaryModel();
            SummaryModel summaryModel1=new SummaryModel();
            SummaryModel summaryModel2=new SummaryModel();


            summaryModel.setLabel("Orders");
            summaryModel.setValue(totalOrderId+"");

            summaryModel1.setLabel("Taxable Amount");
            summaryModel1.setValue(totaltaxableAmount+"");

            summaryModel2.setLabel("Total Amount");
            summaryModel2.setValue(totalAmount+"");

            summaryModelList.add(summaryModel);
            summaryModelList.add(summaryModel1);
            summaryModelList.add(summaryModel2);

            payoutAndTaxReportSummaryModel.setOrderTaxReportList(orderReportObjectModelList);
            payoutAndTaxReportSummaryModel.setSummaryModelList(summaryModelList);

        }catch (Exception exception){
            logger.error("Error in getPayoutAndTaxes ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return payoutAndTaxReportSummaryModel;
    }

    public VendorInvoiceModel getInvoicePdfDate(int fkAssociateId,int orderId){
        VendorInvoiceModel vendorInvoiceModel=new VendorInvoiceModel();

        Connection connection = null;
        String statement,invoiceNumber="",datePurchased="";
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        VendorInfoModel vendorInfoModel=new VendorInfoModel(),mumbaiWarehouseInfoModel=new VendorInfoModel();
        List<OrderProductInvoiceModel> orderProductInvoiceModelList=new ArrayList<>();
        double taxableAmount=0.0,unitPrice=0.0,igst=0.0,cgst=0.0,sgst=0.0,taxRate=0.0,grandTotal=0.0,totalNetAmount=0.0,totalTaxAmount=0.0;


        try{
            connection = Database.INSTANCE.getReadOnlyConnection();
            getVendorInfo(fkAssociateId,connection,vendorInfoModel);
            getVendorInfo(354,connection,mumbaiWarehouseInfoModel);

            statement = "select o.*,gvd.*,date_format(o.date_purchased,'%Y-%m-%d') as datePurchased from orders o join gst_vendors_dom gvd on o.orders_id = gvd.order_id where o.orders_id = ? "
                + " and gvd.vendorId = ? ";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,orderId);
            preparedStatement.setInt(2,fkAssociateId);
            logger.debug("sql query in getInvoicePdfDate "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                String taxType="",taxAmount="";

                taxableAmount=resultSet.getDouble("gvd.taxable");
                taxRate=resultSet.getDouble("gvd.comp_tax_rate");
                int quantity=1;
                unitPrice=taxableAmount/quantity;
                igst=resultSet.getDouble("gvd.igst");
                cgst=resultSet.getDouble("gvd.cgst");
                sgst=resultSet.getDouble("gvd.sgst");
                invoiceNumber=resultSet.getString("gvd.invoice_num");
                datePurchased=resultSet.getString("datePurchased");

                if(igst!=0.000){
                    taxType+="igst "+taxRate+" %";
                    taxAmount+=igst+"";
                }else {
                    taxType+="cgst "+(taxRate/2)+" %<Br>";
                    taxType+="sgst "+(taxRate/2)+" %";

                    taxAmount+=cgst+" <Br>";
                    taxAmount+=sgst+" <Br>";
                }

                OrderProductInvoiceModel orderProductInvoiceModel=new OrderProductInvoiceModel();

                orderProductInvoiceModel.setProductName(resultSet.getString("gvd.tax_cat"));
                orderProductInvoiceModel.setUnitPrice(unitPrice);
                orderProductInvoiceModel.setQuantity(quantity);
                orderProductInvoiceModel.setNetAmount(taxableAmount);
                orderProductInvoiceModel.setTaxCode(resultSet.getString("gvd.hsn_no"));
                orderProductInvoiceModel.setTaxTypeMap(taxType);
                orderProductInvoiceModel.setTaxrate(taxRate);
                orderProductInvoiceModel.setTaxAmount(taxAmount);
                orderProductInvoiceModel.setTotalAmount(resultSet.getDouble("gvd.amt"));
                orderProductInvoiceModelList.add(orderProductInvoiceModel);

                grandTotal+=orderProductInvoiceModel.getTotalAmount();
                totalNetAmount+=orderProductInvoiceModel.getNetAmount();
                totalTaxAmount+=igst+sgst+cgst;
            }
            vendorInvoiceModel.setOrderId(orderId);
            vendorInvoiceModel.setInvoiceNumber(invoiceNumber);
            vendorInvoiceModel.setDatePurchased(datePurchased);
            vendorInvoiceModel.setBillingAddressModel(mumbaiWarehouseInfoModel);
            vendorInvoiceModel.setSellerAddressModel(vendorInfoModel);
            vendorInvoiceModel.setOrderProductInvoiceModelList(orderProductInvoiceModelList);
            vendorInvoiceModel.setTotal(grandTotal);
            vendorInvoiceModel.setTotalNetAmount(totalNetAmount);
            vendorInvoiceModel.setTotalTaxAmount(totalTaxAmount);

        }catch (Exception exception){
            logger.error("Error in getInvoicePdfDate ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
            Database.INSTANCE.closeConnection(connection);
        }
        return vendorInvoiceModel;
    }
    public void getVendorInfo(int fkAssociateId,Connection connection,VendorInfoModel vendorInfoModel){
        String statement;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try{
            statement = "select * from associate a left join associate_finance_info afi on a.associate_id = afi.fk_associate_id "
                + " where a.associate_id = ?";
            preparedStatement = connection.prepareStatement(statement);
            preparedStatement.setInt(1,fkAssociateId);
            logger.debug("sql query in getVendorInfo "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                vendorInfoModel.setName(resultSet.getString("a.associate_name"));
                vendorInfoModel.setEmail(resultSet.getString("a.associate_email"));
                vendorInfoModel.setAddress(resultSet.getString("a.associate_address"));
                vendorInfoModel.setContactDetails(resultSet.getString("a.associate_phone"));
                vendorInfoModel.setGstNumber(resultSet.getString("afi.associate_gstn")==null ? "":resultSet.getString("afi.associate_gstn"));
                vendorInfoModel.setPan(resultSet.getString("afi.associate_pan") ==null ? "" : resultSet.getString("afi.associate_pan"));
            }

        }catch (Exception exception){
            logger.error("Error in getVendorInfo ",exception);
        }finally {
            Database.INSTANCE.closeStatement(preparedStatement);
            Database.INSTANCE.closeResultSet(resultSet);
        }
    }
}
