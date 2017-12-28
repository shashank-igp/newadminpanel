package com.igp.handles.utils.Reports;

import com.igp.config.instance.Database;
import com.igp.handles.models.Report.OrderTaxReport;
import com.igp.handles.models.Report.PayoutAndTaxReportSummaryModel;
import com.igp.handles.models.Report.SummaryModel;
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

    public PayoutAndTaxReportSummaryModel getPayoutAndTaxes(int fkAssociateId,int orderId,String orderDateFrom,
        String orderDateTo,String startLimit,String endLimit){
        PayoutAndTaxReportSummaryModel payoutAndTaxReportSummaryModel = new PayoutAndTaxReportSummaryModel();
        Connection connection = null;
        String statement;
        String whereClause="";
        List <SummaryModel> summaryModelList=new ArrayList<>();
        ResultSet resultSet = null;
        String queryTotal="";
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
                sb.append(" and vsp.assign_time >= "+orderDateFrom+" ");
            }
            if(orderDateTo != null && !orderDateTo.isEmpty()){
                sb.append(" and vsp.assign_time <= "+orderDateTo+" ");
            }

            connection = Database.INSTANCE.getReadOnlyConnection();
            statement = "select o.orders_id  as orderId, sum(gvd.taxable) as taxableAmount ,sum(gvd.amt) as totalAmount, "
                + " (sum(gvd.igst)+sum(gvd.sgst)+sum(gvd.cgst)) as tax , o.delivery_postcode as pincode, "
                + " date_format(vsp.assign_time,'%Y-%d-%m') datePurchased, gvd.invoice_num invoiceNum , o.orders_status status "
                + " , thp.orders_id as paymentCheckOrderId from orders o left join tax_handels_payout thp on o.orders_id = thp.orders_id   join gst_vendors_dom gvd "
                + " on o.orders_id = gvd.order_id join vendor_assign_price vsp on o.orders_id = vsp.orders_id where "
                + " gvd.vendorID = "+fkAssociateId+"  "+sb.toString()+" group by o.orders_id  limit "+startLimit+","+endLimit+" ";


            preparedStatement = connection.prepareStatement(statement);
            logger.debug("sql query in getPayoutAndTaxes "+preparedStatement);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                OrderTaxReport orderTaxReport=new OrderTaxReport();
                orderTaxReport.setOrderId(resultSet.getInt("orderId"));
                orderTaxReport.setTaxableAmount(resultSet.getDouble("taxableAmount"));
                orderTaxReport.setTotalAmount(resultSet.getDouble("totalAmount"));
                orderTaxReport.setTax(resultSet.getDouble("tax"));
                orderTaxReport.setPincode(resultSet.getString("pincode"));
                orderTaxReport.setDatePurchased(resultSet.getString("datePurchased"));
                orderTaxReport.setInvoiceNumber(resultSet.getString("invoiceNum"));
                orderTaxReport.setOrderStatus(resultSet.getString("status"));
                if(resultSet.getInt("paymentCheckOrderId")==0){
                    orderTaxReport.setPaymentStatus("Pending");
                }else {
                    orderTaxReport.setPaymentStatus("paid");
                }
                orderReportObjectModelList.add(orderTaxReport);
                totalOrderId++;
                totaltaxableAmount=totaltaxableAmount+orderTaxReport.getTaxableAmount();
                totalAmount=totalAmount+orderTaxReport.getTotalAmount();
            }
            SummaryModel summaryModel=new SummaryModel();
            SummaryModel summaryModel1=new SummaryModel();
            SummaryModel summaryModel2=new SummaryModel();


            summaryModel.setLabel("Total Orders");
            summaryModel.setValue(totalOrderId+"");

            summaryModel1.setLabel("Total Taxable Amount");
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
        }
        return payoutAndTaxReportSummaryModel;
    }
}
